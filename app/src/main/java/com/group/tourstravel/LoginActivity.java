package com.group.tourstravel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailSignIn, passwordSignIn;
    private ProgressBar progressBar;
    private Button buttonSignIn;
    private FirebaseAuth signAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailSignIn=findViewById(R.id.LoginEmail);
        passwordSignIn=findViewById(R.id.LoginPassword);
        progressBar=findViewById(R.id.progressBar);
        buttonSignIn=findViewById(R.id.buttonSignIn);
        signAuth = FirebaseAuth.getInstance();



        progressBar.setVisibility(View.INVISIBLE);


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                buttonSignIn.setVisibility(View.INVISIBLE);

                final String mail=emailSignIn.getText().toString();
                final String password=passwordSignIn.getText().toString();

                if (mail.isEmpty() || password.isEmpty()){

                    showMessage("Please Verify All Fields");
                }else {

                    signIn(mail,password);
                }

            }
        });

    }

    private void signIn(String mail, String password) {

        signAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    buttonSignIn.setVisibility(View.VISIBLE);

                    upDateUiDashboard();
                }else {

                    showMessage(task.getException().getMessage());

                }
            }
        });
    }

    private void upDateUiDashboard() {

        Intent StartDashBoardActivity = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(StartDashBoardActivity);

        finish();
    }

    private void showMessage(String text) {

        Toast.makeText(getApplicationContext(), text,Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = signAuth.getCurrentUser();

        if (user !=null){

            // user is in system start DashboardActivity
            upDateUiDashboard();

        }
    }
}
