package com.group.tourstravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonStartLogin,buttonStartSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonStartLogin = findViewById(R.id.LoginStart);
        buttonStartSignUp = findViewById(R.id.SignUpStart);

        buttonStartLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openLogin = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(openLogin);

            }
        });


        buttonStartSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSignUp= new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(openSignUp);

            }
        });


    }

}








