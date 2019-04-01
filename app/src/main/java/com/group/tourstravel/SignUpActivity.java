package com.group.tourstravel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class SignUpActivity extends AppCompatActivity {

    private EditText userName, userEmail, userPassword1, userPassword2;
    private Button buttonSignUp;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userName =findViewById(R.id.UserNameLayout);
        userEmail =findViewById(R.id.UserNameEmail);
        userPassword1=findViewById(R.id.UserNamePassword1);
        userPassword2=findViewById(R.id.UserNamePassword2);

        buttonSignUp = findViewById(R.id.buttonSignUp);

        mAuth=FirebaseAuth.getInstance();


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonSignUp.setVisibility(View.INVISIBLE);
                final String username=userName.getText().toString();
                final String email =userEmail.getText().toString();
                final String password1 = userPassword1.getText().toString();
                final String password2 = userPassword2.getText().toString();


                if (email.isEmpty() || username.isEmpty() || password1.isEmpty()|| password2.isEmpty() || !password1.equals(password2)){


                    showMessage("Please Verify all Fields");

                    buttonSignUp.setVisibility(View.VISIBLE);

                }else {

                    CreateUserAccount(username,email,password1);
                }

            }
        });


    }

    private void CreateUserAccount(final String username, String email, String password1) {

        mAuth.createUserWithEmailAndPassword(email,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            showMessage("You have Crated Account Successfully");

                            updateUserInfo( username, mAuth.getCurrentUser());
                        }
                        else {
                            showMessage("Failed to Create Account" + task.getException().getMessage());
                            buttonSignUp.setVisibility(View.VISIBLE);

                        }

                    }
                });





    }
            //update username
    private void updateUserInfo(String username, FirebaseUser currentUser) {

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(username).build();

        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                        showMessage("Registration Successful");

                        updateUiLogon();
                        }
                    }
                });
    }

    private void updateUiLogon() {
        Intent startLoginPage =new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(startLoginPage);
        Toast.makeText(SignUpActivity.this, "Opening Login Page", Toast.LENGTH_SHORT).show();
        finish();


    }

    private void showMessage(String message){

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }
}
