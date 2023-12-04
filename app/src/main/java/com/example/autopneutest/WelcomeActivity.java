package com.example.autopneutest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the title and set the window to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Check if the user is already signed in
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            // If the user is already signed in, sign them out
            auth.signOut();
        }
    }

    public void register(View view) {
        startActivity(new Intent(WelcomeActivity.this, RegistrationActivity.class));
    }

    public void login(View view) {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
    }
}
