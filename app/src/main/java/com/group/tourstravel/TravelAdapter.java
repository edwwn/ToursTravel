package com.group.tourstravel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.card.payment.i18n.I18nManager;

import static com.group.tourstravel.DetailActivity.TRAVEL_DESCRIPTIONS;
import static com.group.tourstravel.DetailActivity.TRAVEL_IMAGE;
import static com.group.tourstravel.DetailActivity.TRAVEL_NAME;
import static com.group.tourstravel.DetailActivity.TRAVEL_PRICE;
import static com.group.tourstravel.DetailActivity.TRAVEL_RATING;

public class TravelAdapter  extends RecyclerView.Adapter<TravelAdapter.ViewHolder> {

    Cursor dataCursor;
    Context context;
    int id;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, userrating,description, price;
        public ImageView thumbnail;

        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.title);
           // userrating = (TextView) itemView.findViewById(R.id.userrating);

            thumbnail =itemView.findViewById(R.id.id_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= getAdapterPosition();
                    if (pos !=RecyclerView.NO_POSITION){
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(TRAVEL_NAME,getItem(pos).name);
                        intent.putExtra(TRAVEL_DESCRIPTIONS,getItem(pos).description);
                        intent.putExtra(TRAVEL_IMAGE,getItem(pos).imageUrl);
                        intent.putExtra(TRAVEL_PRICE,getItem(pos).price);
                        intent.putExtra(TRAVEL_RATING,getItem(pos).userRating);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public TravelAdapter (Activity mContext, Cursor cursor){
        dataCursor = cursor;
        context=mContext;

    }

    @Override
    public TravelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tours_card, parent,false);
        return new ViewHolder(cardView);
    }


    @Override
    public void onBindViewHolder(TravelAdapter.ViewHolder holder, int position) {
        dataCursor.moveToPosition(position);

        id = dataCursor.getInt(dataCursor.getColumnIndex(ToursContract.toursEntry._ID));
        String mName = dataCursor.getString(dataCursor.getColumnIndex(ToursContract.toursEntry.COLUMN_NAME));
        String mDescription = dataCursor.getString(dataCursor.getColumnIndex(ToursContract.toursEntry.COLUMN_DESCRIPTIONS));
        String mImageUrl = dataCursor.getString(dataCursor.getColumnIndex(ToursContract.toursEntry.COLUMN_IMAGE));
        int mPrice = dataCursor.getInt(dataCursor.getColumnIndex(ToursContract.toursEntry.COLUMN_PRICE));
        int mUserRating = dataCursor.getInt(dataCursor.getColumnIndex(ToursContract.toursEntry.COLUMN_USERRATING));

        holder.name.setText(mName);

        String poster ="http://boombox.ng/images/travels" + mImageUrl;

        Glide.with(context)
                .load(poster)
                .placeholder(R.drawable.load)
                .into(holder.thumbnail);
    }


    public Cursor swapCursor(Cursor cursor){
        if (dataCursor == cursor){
            return null;

        }
        Cursor oldCursor = dataCursor;
        this.dataCursor=cursor;

        if (cursor !=null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }


    @Override
    public int getItemCount() {

        return (dataCursor==null) ? 0 : dataCursor.getCount();
    }


    public Travel getItem(int position){
        if (position<0 || position>=getItemCount()){
            throw new IllegalArgumentException("Item Position is Out of the adapters Range");
        }else  if (dataCursor.moveToPosition(position)){
            return new Travel(dataCursor);

        }
        return null;
    }



}

