package com.group.tourstravel;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class TravelProvider extends ContentProvider {

    public static final String LOG_TAG = TravelProvider.class.getSimpleName();

    private static  final int TRAVELS = 100;

    private static final int TRAVEL_ID= 101;

    private  static final int CART =102;

    private static final int CART_ID = 103;


    private static  final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ToursContract.CONTENT_AUTHORITY, ToursContract.PATH_TOURS,TRAVELS);

        sUriMatcher.addURI(ToursContract.CONTENT_AUTHORITY, ToursContract.PATH_CART, CART);

        sUriMatcher.addURI(ToursContract.CONTENT_AUTHORITY, ToursContract.PATH_TOURS + "/#", TRAVEL_ID);

        sUriMatcher.addURI(ToursContract.CONTENT_AUTHORITY, ToursContract.PATH_CART + "/#", CART_ID);

    }

    private ToursDbHelper toursDbHelper;

    @Override
    public boolean onCreate(){
        toursDbHelper = new ToursDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override

    public  Cursor query (@Nullable Uri uri, @Nullable String[] projection, String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder){
        SQLiteDatabase database = toursDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match){
            case TRAVELS:
                cursor=database.query(ToursContract.toursEntry.TABLE_NAME,projection, selection,selectionArgs,null, null, sortOrder);
                break;

            case CART:
                cursor = database.query(ToursContract.toursEntry.CART_TABLE, projection,selection,selectionArgs, null, null, sortOrder);
                break;

            case  CART_ID:
                selection =ToursContract.toursEntry._CARTID + "=";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor= database.query(ToursContract.toursEntry.CART_TABLE,projection,selection, selectionArgs, null,null,sortOrder);

                break;


            case TRAVEL_ID:
                selection =ToursContract.toursEntry._ID+"=";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ToursContract.toursEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                throw  new IllegalArgumentException("Cannot query Unknown URI" + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public  String getType(@NonNull Uri uri){return null;}


    @Nullable
    @Override
    public  Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues){
        final int match =sUriMatcher.match(uri);
        switch ( match){
            case TRAVELS:
                return insertCart(uri, contentValues);
             default:
                 throw new IllegalArgumentException("Insertion is not supported for" + uri);

        }
    }
    private Uri insertCart(Uri uri,ContentValues Values){
        SQLiteDatabase database = toursDbHelper.getWritableDatabase();

        long id = database.insert(ToursContract.toursEntry.CART_TABLE,null, Values);

        if (id== -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public  int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings){
       final SQLiteDatabase db = toursDbHelper.getWritableDatabase();

       int match = sUriMatcher.match(uri);

       int cartDelete;

       switch (match){
           case CART_ID:
               String id = uri.getPathSegments().get(1);
               cartDelete=db.delete(ToursContract.toursEntry.CART_TABLE, "id=?", new String[]{id});
               break;

           default:
               throw new UnsupportedOperationException("Unkown uri"+ uri);

       }
       if (cartDelete !=0){
           getContext().getContentResolver().notifyChange(uri, null);

       }
       return cartDelete;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


}
