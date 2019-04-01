package com.group.tourstravel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.group.tourstravel.ToursContract.toursEntry.CART_TABLE;

public class DetailActivity extends AppCompatActivity {

    public static final String TRAVEL_NAME="TravelName";
    public static final String TRAVEL_DESCRIPTIONS="TravelDescription";
    public static final String TRAVEL_RATING="TravelRating";
    public static final String TRAVEL_IMAGE="TravelImageUrl";
    public static final String TRAVEL_PRICE="TravelPrice";

    private ImageView mImage;

    String travelName, description, travelImage;
    int rating;
    Double price;

    private int mQuantity= 1;
    private double mTotalPrice;
    String imagePath;
    TextView costTextView;
    ContentResolver mContentResolver;
    private SQLiteDatabase mDb;


    private int mNotificationsCount;
    Button addToCartButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContentResolver= this.getContentResolver();
        ToursDbHelper dbHelper =new ToursDbHelper(this);
        mDb=dbHelper.getWritableDatabase();


        mImage = findViewById(R.id.travels_Image);
        Intent intentThatStartedThisActivity = getIntent();
        addToCartButton= findViewById(R.id.cartButton);

        costTextView=findViewById(R.id.cost_text_view);

        if (intentThatStartedThisActivity.hasExtra(TRAVEL_NAME)){
            travelName=getIntent().getExtras().getString(TRAVEL_NAME);
            description=getIntent().getExtras().getString(TRAVEL_DESCRIPTIONS);
            rating=getIntent().getExtras().getInt(TRAVEL_RATING);
            travelImage=getIntent().getExtras().getString(TRAVEL_IMAGE);
            price=getIntent().getExtras().getDouble(TRAVEL_PRICE);

            TextView desc =findViewById(R.id.description);
            desc.setText(description);

            TextView fragmentPrice = findViewById(R.id.price);
            DecimalFormat precision =new DecimalFormat("0.00");
            fragmentPrice.setText("$"+ precision.format(price));

            float f =Float.parseFloat(Double.toString(rating));

            setTitle(travelName);

            RatingBar ratingBar =findViewById(R.id.rating_level);
            ratingBar.setRating(f);

            imagePath ="http://boombox.ng/images/travels/" + travelImage;

            Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.load)
                    .into(mImage);

        }

        if (mQuantity==1){

            mTotalPrice = price;
            displayCost(mTotalPrice);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item=menu.findItem(R.id.action_notifications);

        LayerDrawable icon = (LayerDrawable) item.getIcon();

        Utils.setBadgeCount(this,icon, mNotificationsCount);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_notifications:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            default:
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        new FetchCountTask().execute();
    }

    public void increment(View view){

        price = getIntent().getExtras().getDouble(TRAVEL_PRICE);
        mQuantity = mQuantity +1;
        displayQuantity(mQuantity);
        displayCost(mTotalPrice);


    }

    public void decrement(View view){

        if (mQuantity>1){
            mQuantity = mQuantity - 1;
            displayQuantity(mQuantity);
            mTotalPrice=mQuantity*price;
            displayCost(mTotalPrice);
        }

    }

    public void displayQuantity(int numberOfItems){
        TextView quantityTextView =findViewById(R.id.quantityLabel);
        quantityTextView.setText(String.valueOf(numberOfItems));

    }

    public void displayCost(double TotalPrice){

        String convertPrice= NumberFormat.getCurrencyInstance().format(TotalPrice);
        costTextView.setText(convertPrice);

    }

    private void addValuesToCart(){

        ContentValues cartValues =new ContentValues();

        cartValues.put(ToursContract.toursEntry.COLUMN_CART_NAME, travelName);
        cartValues.put(ToursContract.toursEntry.COLUMN_CART_IMAGE, travelImage);
        cartValues.put(ToursContract.toursEntry.COLUMN_CART_QUANTITY, mQuantity);
        cartValues.put(ToursContract.toursEntry.COLUMN_CART_TOTALPRICE, mTotalPrice);

        mContentResolver.insert(ToursContract.toursEntry.CONTENT_URI, cartValues);

        Toast.makeText(this, "Successfully Added to Cart", Toast.LENGTH_SHORT).show();


    }

    public void addToCart(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.add_to_cart);
        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addValuesToCart();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog!=null){
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        }


    private void updateNotificationsBadge(int count){

        mNotificationsCount=count;

        invalidateOptionsMenu();

    }

    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            String countQuery = "SELECT  * FROM " + CART_TABLE;
            Cursor cursor = mDb.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            return count;

        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }


}


