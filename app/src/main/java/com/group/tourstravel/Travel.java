package com.group.tourstravel;

import android.database.Cursor;

public class Travel {

    public int id;

    public String name;
    public String description;
    public String imageUrl;
    public Double price;
    public int userRating;

    public Travel(Cursor cursor){
        this.name =cursor.getString(cursor.getColumnIndex(ToursContract.toursEntry.COLUMN_NAME));
        this.description =cursor.getString(cursor.getColumnIndex(ToursContract.toursEntry.COLUMN_DESCRIPTIONS));
        this.imageUrl =cursor.getString(cursor.getColumnIndex(ToursContract.toursEntry.COLUMN_IMAGE));
        this.price =cursor.getDouble(cursor.getColumnIndex(ToursContract.toursEntry.COLUMN_PRICE));
        this.userRating =cursor.getInt(cursor.getColumnIndex(ToursContract.toursEntry.COLUMN_USERRATING));


    }


}
