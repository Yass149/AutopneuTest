package com.example.autopneutest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAdminActivity extends AppCompatActivity {

    TextInputEditText editTextAdminEmail, editTextAdminPassword, specialCodeEditText;
    Button buttonLog, backButton;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        mAuth = FirebaseAuth.getInstance();
        editTextAdminEmail = findViewById(R.id.mail);
        editTextAdminPassword = findViewById(R.id.pass);
        specialCodeEditText = findViewById(R.id.specialCode);
        buttonLog = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backButton);

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String adminEmail = String.valueOf(editTextAdminEmail.getText());
                String adminPassword = String.valueOf(editTextAdminPassword.getText());
                String specialCode = String.valueOf(specialCodeEditText.getText()); // Add this line

                // Check if the special code is entered
                Log.d("SpecialCode", "Entered Special Code: " + specialCode);
                if (!TextUtils.isEmpty(specialCode) && specialCode.equals("1341")) {
                    // Special code is correct, proceed with login
                    mAuth.signInWithEmailAndPassword(adminEmail, adminPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginAdminActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginAdminActivity.this, MainAdminActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginAdminActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Special code is incorrect, display an error message
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginAdminActivity.this, "Incorrect special code", Toast.LENGTH_SHORT).show();
                }
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
