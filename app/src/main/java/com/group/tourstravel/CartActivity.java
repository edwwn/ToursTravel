package com.group.tourstravel;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class CartActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CART_LOADER =0;

    CartAdapter cartAdapter;
    RecyclerView mRecyclerView;
    Double totalPrice;
    Button paymentButton;

    private static PayPalConfiguration config = new PayPalConfiguration()

            .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

            .clientId("AYF6ZtG_od3NYOkt2r8CIj14sUTlLbshqIvV0JO9FZ9qsoSw-vxJlGpQOZ0UMnIivGDHEsCFV92CVSPW");


    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        mRecyclerView = findViewById(R.id.cart_recycler);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        cartAdapter = new CartAdapter(this);
        mRecyclerView.setAdapter(cartAdapter);
        mRecyclerView.addItemDecoration(new  SimpleDividerItemDecoration(this));


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int id = (int) viewHolder.itemView.getTag();
                String stringId =Integer.toString(id);
                Uri uri =ToursContract.toursEntry.CONTENT_URI_CART;
                uri = uri.buildUpon().appendPath(stringId).build();


                getContentResolver().delete(uri,null,null);


                getLoaderManager().restartLoader(CART_LOADER, null, CartActivity.this);

            }
        }).attachToRecyclerView(mRecyclerView);

        getLoaderManager().initLoader(CART_LOADER,null, null);

        paymentButton=findViewById(R.id.buttonPayment);

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection ={
                ToursContract.toursEntry._CARTID,
                ToursContract.toursEntry.COLUMN_CART_NAME,
                ToursContract.toursEntry.COLUMN_CART_IMAGE,
                ToursContract.toursEntry.COLUMN_CART_QUANTITY,
                ToursContract.toursEntry.COLUMN_CART_TOTALPRICE,

        };
        return new CursorLoader(this,
                ToursContract.toursEntry.CONTENT_URI_CART,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cartAdapter.swapCursor(cursor);
        calculateTotal(cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CART_LOADER, null, CartActivity.this);

    }

    public double calculateTotal(Cursor cursor){
        totalPrice=0.00;
        for (int i=0; i<cursor.getCount();i++){
            int price =cursor.getColumnIndex(ToursContract.toursEntry.COLUMN_CART_TOTALPRICE);

            cursor.moveToPosition(i);
            Double travelPrice =cursor.getDouble(price);
            totalPrice+=travelPrice;

        }

        TextView totalCost = findViewById(R.id.TotalPrice);
        String convertPrice = NumberFormat.getCurrencyInstance().format(totalPrice);
        totalCost.setText(convertPrice);
        return  totalPrice;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        cartAdapter.swapCursor(null);
    }

    public void paymentClick(View pressed){

        PayPalPayment payment = new PayPalPayment(new BigDecimal(totalPrice), "USD", "Being Payment for items",
                PayPalPayment.PAYMENT_INTENT_SALE);

         Intent intent = new Intent(this, PayPalPayment.class);

         intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
         intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payment);

         startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm =data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm !=null){
                try {
                    Log.i("PaymentExample",confirm.toJSONObject().toString(4));

                }catch (JSONException e){
                    Log.e("PaymentExample","an extremely unlikely failure occurred:  ", e);

                }
            }
        }
        else if (resultCode==Activity.RESULT_CANCELED){
            Log.i("PaymentExample","The user cancelled: ");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Log.i("PaymentExample","An Invalid Payment or PayPalConfiguration was submitted. Please see docs");
        }
    }

}

