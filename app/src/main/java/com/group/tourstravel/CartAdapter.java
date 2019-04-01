package com.group.tourstravel;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Cursor mCursor;
    private Context mContext;


    public CartAdapter(Context mContext){this.mContext=mContext;}


    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.cart_item,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(CartAdapter.CartViewHolder holder, int position, List<Object> payloads){
        int idIndex = mCursor.getColumnIndex(ToursContract.toursEntry._CARTID);
        int travelName= mCursor.getColumnIndex(ToursContract.toursEntry.COLUMN_CART_NAME);
        int image = mCursor.getColumnIndex(ToursContract.toursEntry.COLUMN_CART_IMAGE);
        int quantity = mCursor.getColumnIndex(ToursContract.toursEntry.COLUMN_CART_QUANTITY);
        int price = mCursor.getColumnIndex(ToursContract.toursEntry.COLUMN_CART_TOTALPRICE);

        mCursor.moveToPosition(position);


        final int id = mCursor.getInt(idIndex);
        String name = mCursor.getString(travelName);
        String travelImage= mCursor.getString(image);
        int travelQuantity = mCursor.getInt(quantity);
        Double travelPrice=mCursor.getDouble(price);

        DecimalFormat precision = new DecimalFormat("0.00");

        holder.itemView.setTag(id);
        holder.traName.setText(name);
        holder.traQuantity.setText("Quantity ordering:" + String.valueOf(travelQuantity));
        holder.traPrice.setText("$" + precision.format(travelPrice));


        String poster = "http://boombox.ng/images/travel" + travelImage;

        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(holder.image);
    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {

        if (mCursor==c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor=c;

        if (c!=null){
           this.notifyDataSetChanged();
        }
        return temp;
    }


    public class CartViewHolder extends RecyclerView.ViewHolder {

        TextView traName, traQuantity, traPrice;
        ImageView image;

        public  CartViewHolder(View itemView){

            super(itemView);

            traName = itemView.findViewById(R.id.ToursName);
            traQuantity=itemView.findViewById(R.id.Quantity);
            traPrice=itemView.findViewById(R.id.PRICE);
            image=itemView.findViewById(R.id.cartImage);


        }

    }
}


