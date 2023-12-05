package com.example.autopneutest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;

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
                progressBar.setVisibility(View.VISIBLE);
                String adminEmail = String.valueOf(editTextAdminEmail.getText());
                String adminPassword = String.valueOf(editTextAdminPassword.getText());

                if (TextUtils.isEmpty(adminEmail) || TextUtils.isEmpty(adminPassword)) {
                    Toast.makeText(LoginAdminActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(adminEmail, adminPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginAdminActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginAdminActivity.this, MainAdminActivity.class); // Update with the correct activity class
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginAdminActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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

    }  }