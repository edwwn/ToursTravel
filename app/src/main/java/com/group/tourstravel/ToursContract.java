package com.group.tourstravel;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ToursContract {

    private ToursContract(){}


        public static final String CONTENT_AUTHORITY = "com.group.tourstravel";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+
                CONTENT_AUTHORITY );


        public static final String PATH_TOURS ="Travel-path";
        public static final String PATH_CART="cart-path";


        public static final class toursEntry implements BaseColumns{
                public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TOURS);

                public static final Uri CONTENT_URI_CART = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CART);


                public static final String CONTENT_LIST_TYPE=
                        ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +"/" + PATH_TOURS;


                public static final String CONTENT_ITEM_TYPE=
                        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +CONTENT_AUTHORITY +"/" + PATH_TOURS;


                //name of table of Travel
                public static final String TABLE_NAME="travel";
                // name of table of cart
                public static  final String CART_TABLE="cart";


                public final static String _ID= BaseColumns._ID;
                public final static String _CARTID=BaseColumns._ID;

                public static final String COLUMN_NAME= "Travel";
                public static final String COLUMN_DESCRIPTIONS= "descriptions";
                public static final String COLUMN_IMAGE= "imageurl";
                public static final String COLUMN_PRICE= "price";
                public static final String COLUMN_USERRATING= "userrating";



                public static final String COLUMN_CART_NAME= "cartTours";
                public static final String COLUMN_CART_IMAGE= "cartimageurl";
                public static final String COLUMN_CART_QUANTITY= "cartquantity";
                public static final String COLUMN_CART_TOTALPRICE= "carttotalprice";

    }


}
