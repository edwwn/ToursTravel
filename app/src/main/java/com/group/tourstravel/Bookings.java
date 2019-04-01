package com.group.tourstravel;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


import static com.group.tourstravel.ToursContract.toursEntry.CART_TABLE;

public class Bookings extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    TravelAdapter travelAdapter;
    private static final int TRAVEL_LOADER=0;
    ToursDbHelper toursDbHelper;
    private SQLiteDatabase mDb;

    private int mNotificationsCount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        ToursDbHelper dbHelper = new ToursDbHelper(this);
        mDb =dbHelper.getReadableDatabase();

        recyclerView = findViewById(R.id.Recycler_View);
        recyclerView.setHasFixedSize(true);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        travelAdapter = new TravelAdapter(this, null);
        recyclerView.setAdapter(travelAdapter);

        getLoaderManager().initLoader(TRAVEL_LOADER, null, this);
        new FetchCountTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item= menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        Utils.setBadgeCount(this, icon, mNotificationsCount);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_notifications:{
                Intent intent = new Intent(this,CartActivity.class);
                startActivity(intent);
                return true;

            }
        }return super.onOptionsItemSelected(item);


    }

    private void updateNotificationsBadge(int count){
        mNotificationsCount = count;
        invalidateOptionsMenu();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){

        String [] projection ={
                ToursContract.toursEntry._ID,
                ToursContract.toursEntry.COLUMN_NAME,
                ToursContract.toursEntry.COLUMN_DESCRIPTIONS,
                ToursContract.toursEntry.COLUMN_IMAGE,
                ToursContract.toursEntry.COLUMN_PRICE,
                ToursContract.toursEntry.COLUMN_USERRATING
        };

        return new CursorLoader( this,
                ToursContract.toursEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        travelAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        travelAdapter.swapCursor(null);
    }

    class FetchCountTask extends AsyncTask<Void, Void, Integer>{

        @Override
        protected Integer doInBackground(Void ...params){
            String countQuery ="SELECT * FROM " + CART_TABLE;
            Cursor cursor = mDb.rawQuery(countQuery, null);
            int count = cursor.getCount();
            return count;

        }
        @Override
        public void onPostExecute(Integer count) {

            updateNotificationsBadge(count);
        }
    }
}
