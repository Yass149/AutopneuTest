package com.example.autopneutest;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword, editTextReenterPassword, textInputEditTextPhone;
    private Button buttonReg;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.editText3);
        editTextPassword = findViewById(R.id.editText2);
        editTextReenterPassword = findViewById(R.id.reenterPasswordEditText);
        textInputEditTextPhone = findViewById(R.id.textInputEditTextPhone);
        buttonReg = findViewById(R.id.buttonreg);
        progressBar = findViewById(R.id.progressBar);

        // Check if the user is already authenticated
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startMainActivity();
        }

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegistration();
            }
        });
    }

    private void handleRegistration() {
        progressBar.setVisibility(View.VISIBLE);

        String email = String.valueOf(editTextEmail.getText());
        String password = String.valueOf(editTextPassword.getText());
        String reenterPassword = String.valueOf(editTextReenterPassword.getText());
        String phoneNumber = String.valueOf(textInputEditTextPhone.getText());

        // Check if the phone number is exactly 10 digits
        if (phoneNumber.length() != 10) {
            showErrorMessage("Enter a valid 10-digit phone number");
            return;
        }

        // Check if passwords match
        if (!password.equals(reenterPassword)) {
            showErrorMessage("Passwords do not match");
            return;
        }

        // Validate password
        if (!isValidPassword(password)) {
            showErrorMessage("Enter a valid password (minimum 10 characters, at least one special character)");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Account created.", Toast.LENGTH_SHORT).show();
                            startMainActivity();
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 10 && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?].*");
    }

    private void showErrorMessage(String message) {
        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
    }

    private void startMainActivity() {
        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

    public void login(View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }
}
