package com.example.autopneutest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        Button btnDisconnect = findViewById(R.id.btnDisconnect);

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle disconnect action here
                disconnect();
            }
        });
    }

    private void disconnect() {
        // Add code to perform disconnection actions
        // For example, you can clear user session, log out, or navigate to a login screen
        // Uncomment and modify the example code based on your application's authentication or session management requirements.

        // Example: Navigating to LoginActivity
        startActivity(new Intent(MainAdminActivity.this, WelcomeActivity.class));

        // Optional: Close the current activity
        finish();
    }

    public void seeAppointmentActivity(View view) {
        startActivity(new Intent(MainAdminActivity.this, SeeAppointmentActivity.class));
    }

    public void seeScheduleActivity(View view) {
        startActivity(new Intent(MainAdminActivity.this, SeeScheduleActivity.class));
    }
}
