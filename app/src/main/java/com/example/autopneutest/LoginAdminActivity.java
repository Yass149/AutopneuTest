package com.example.autopneutest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAdminActivity extends AppCompatActivity {

    TextInputEditText editTextAdminEmail, editTextAdminPassword;
    Button buttonLog, backButton;  // Added backButton
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        mAuth = FirebaseAuth.getInstance();
        editTextAdminEmail = findViewById(R.id.mail);
        editTextAdminPassword = findViewById(R.id.pass);
        buttonLog = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backButton);  // Added backButton

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Your existing code for login
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle back button click to go back to customer login activity
                Intent intent = new Intent(LoginAdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
