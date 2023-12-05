package com.example.autopneutest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SeeScheduleActivity extends AppCompatActivity {

    private DatabaseReference appointmentsRef;
    private FirebaseAuth mAuth;

    private ListView listViewAppointments;
    private List<String> appointmentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_schedule);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Redirect to login or handle the case where the admin is not signed in
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        appointmentsList = new ArrayList<>();
        listViewAppointments = findViewById(R.id.listViewAppointments);

        // Set up the reference to the appointments node in your database
        appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");

        // Retrieve and display the appointments for the current admin
        retrieveAppointments(currentUser.getUid());

        // Setup Toolbar and Back Button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button in the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void retrieveAppointments(String adminId) {
        // Query the appointments for the current admin
        appointmentsRef.orderByChild("adminId").equalTo(adminId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentsList.clear();

                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    // Assuming you have a model class for appointments, adjust the code accordingly
                    // For example, if your Appointment class has a 'time' field, you can retrieve it like this:
                    // String time = appointmentSnapshot.child("time").getValue(String.class);
                    // appointmentsList.add(time);

                    // Add the appointment details to the list (modify based on your data structure)
                    appointmentsList.add(appointmentSnapshot.getValue(String.class));
                }

                // Update the ListView with the appointments
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SeeScheduleActivity.this, android.R.layout.simple_list_item_1, appointmentsList);
                listViewAppointments.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SeeScheduleActivity", "Error retrieving appointments: " + databaseError.getMessage());
            }
        });
    }
}
