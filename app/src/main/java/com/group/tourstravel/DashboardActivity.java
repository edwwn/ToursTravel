package com.group.tourstravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    Button buttonBooking,buttonProfile,buttonNotifications,buttonGallery,buttonMap,buttonWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        buttonBooking =findViewById(R.id.ButtonBookings);
        buttonProfile = findViewById(R.id.ButtonProfile);
        buttonNotifications =findViewById(R.id.ButtonNotifications);
        buttonGallery= findViewById(R.id.ButtonGallery);
        buttonMap = findViewById(R.id.ButtonMap);
        buttonWeather = findViewById(R.id.ButtonWeather);


        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBookings = new Intent(DashboardActivity.this, Bookings.class);
                startActivity(openBookings);

                Toast.makeText(DashboardActivity.this, "Bookings is Opening", Toast.LENGTH_SHORT).show();

            }
        });
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openProfile = new Intent(DashboardActivity.this, Profile.class);
                startActivity(openProfile);

                Toast.makeText(DashboardActivity.this, "Profile is Opening", Toast.LENGTH_SHORT).show();
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalley = new Intent(DashboardActivity.this, Gallery.class);
                startActivity(openGalley);

                Toast.makeText(DashboardActivity.this, "Gallery is Opening", Toast.LENGTH_SHORT).show();
            }
        });
        buttonNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNotifications = new Intent(DashboardActivity.this, Notifications.class);
                startActivity(openNotifications);

                Toast.makeText(DashboardActivity.this, "Notifications is Opening", Toast.LENGTH_SHORT).show();

            }
        });
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMap = new Intent(DashboardActivity.this, MapsActivity.class);
                startActivity(openMap);

                Toast.makeText(DashboardActivity.this, "Maps are Opening", Toast.LENGTH_SHORT).show();
            }
        });
        buttonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openWeather = new Intent(DashboardActivity.this, Weather.class);
                startActivity(openWeather);

                Toast.makeText( DashboardActivity.this, "Weather is Opening",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
