package com.group.tourstravel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ToursDbHelper extends SQLiteOpenHelper {

    private static final String TAG= ToursDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME="Travel.db";
    private static final int DATABASE_VERSION=1;

    Context context;
    SQLiteDatabase db;
    ContentResolver mContentResolver;

    public Resources mResources;

    public ToursDbHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
        mContentResolver = context.getContentResolver();
        db= this.getWritableDatabase();
    }

    public  void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TOURS_TABLE = "CREATE TABLE " + ToursContract.toursEntry.TABLE_NAME + "(" +
                ToursContract.toursEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                ToursContract.toursEntry.COLUMN_NAME + "TEXT UNIQUE NOT NULL," +
                ToursContract.toursEntry.COLUMN_DESCRIPTIONS + "TEXT NOT NULL," +
                ToursContract.toursEntry.COLUMN_IMAGE + " TEXT NOT NULL," +
                ToursContract.toursEntry.COLUMN_PRICE + " REAL NOT NULL," +
                ToursContract.toursEntry.COLUMN_USERRATING + "INTEGER NOT NULL " + ");";


        final String SQL_CREATE_CART_TOURS_TABLE = "CREATE TABLE " + ToursContract.toursEntry.CART_TABLE + "(" +
                ToursContract.toursEntry._CARTID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                ToursContract.toursEntry.COLUMN_CART_NAME + "TEXT UNIQUE NOT NULL," +
                ToursContract.toursEntry.COLUMN_CART_IMAGE + "TEXT NOT NULL," +
                ToursContract.toursEntry.COLUMN_CART_QUANTITY + "INTEGER NOT NULL," +
                ToursContract.toursEntry.COLUMN_CART_TOTALPRICE + " REAL NOT NULL" + ");";

        db.execSQL(SQL_CREATE_TOURS_TABLE);
        db.execSQL(SQL_CREATE_CART_TOURS_TABLE);
        Log.d(TAG, "DATABASES CREATE SUCCESSFULLY");

        try {
            readToursFromResources(db);
        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();

        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ToursContract.toursEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ToursContract.toursEntry.CART_TABLE);
        onCreate(db);
    }

    private void readToursFromResources(SQLiteDatabase db) throws  IOException, JSONException{
        StringBuilder builder = new StringBuilder();
        InputStream in = mResources.openRawResource(R.raw.travels);
        BufferedReader reader = new BufferedReader( new InputStreamReader(in));

        String line;
        while ((line = reader.readLine()) !=null){
            builder.append(line + "\n");
        }

        final  String rawJson = builder.toString();

        final String BGS_TRAVELS="travels";

        final String BGS_TRAVELSNAME= "travelsIn";
        final String BGS_DESCRIPTION="Description";
        final String BGS_IMAGEURL="imageUrl";
        final String BGS_PRICE ="price";
        final String BGS_USERRATING="travelRatings";


        try {
            JSONObject travelJson = new JSONObject(rawJson);
            JSONArray travelArray = travelJson.getJSONArray(BGS_TRAVELS);

            for (int i=0; i<travelArray.length(); i++){
                String travelsName;
                String description;
                String imageUrl;
                Double price;
                int userRating;

                JSONObject travelDetails = travelArray.getJSONObject(i);

                travelsName =travelDetails.getString(BGS_TRAVELSNAME);
                description = travelDetails.getString(BGS_DESCRIPTION);
                imageUrl =travelDetails.getString(BGS_IMAGEURL);
                price =travelDetails.getDouble(BGS_PRICE);
                userRating=travelDetails.getInt(BGS_USERRATING);

                ContentValues travelValues = new ContentValues();

                travelValues.put(ToursContract.toursEntry.COLUMN_NAME, travelsName);
                travelValues.put(ToursContract.toursEntry.COLUMN_DESCRIPTIONS, description);
                travelValues.put(ToursContract.toursEntry.COLUMN_IMAGE, imageUrl);
                travelValues.put(ToursContract.toursEntry.COLUMN_PRICE,price);
                travelValues.put(ToursContract.toursEntry.COLUMN_USERRATING, userRating);

                db.insert(ToursContract.toursEntry.TABLE_NAME, null, travelValues);

                Log.d(TAG, "INSERTED SUCCESSFULLY"+ travelValues);
            }
            Log.d(TAG, "INSERTED SUCCESSFULLY" + travelArray.length());

        }catch (JSONException e){
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }



    }

}
