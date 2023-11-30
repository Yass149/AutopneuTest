package com.example.autopneutest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
    }

    public void seeAppointmentActivity(View view) {
        startActivity(new Intent(MainAdminActivity.this, SeeAppointmentActivity.class));
    }

    public void seeScheduleActivity(View view) {
        startActivity(new Intent(MainAdminActivity.this, SeeScheduleActivity.class));
    }
}