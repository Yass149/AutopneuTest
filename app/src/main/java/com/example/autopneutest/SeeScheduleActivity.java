package com.example.autopneutest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SeeScheduleActivity extends AppCompatActivity {

    private DatabaseReference appointmentsRef;
    private FirebaseAuth mAuth;

    private ListView listViewAppointments;
    private List<String> appointmentsList;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_schedule);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        appointmentsList = new ArrayList<>();
        listViewAppointments = findViewById(R.id.listViewAppointments);
        calendarView = findViewById(R.id.calendarView);

        appointmentsRef = FirebaseDatabase.getInstance().getReference().child("appointments");

        retrieveAppointments(currentUser.getUid());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set up the listener for date changes in the CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the selected date change, e.g., update the list based on the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                updateListForSelectedDate(selectedDate);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void retrieveAppointments(String adminId) {
        appointmentsRef.orderByChild("adminId").equalTo(adminId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentsList.clear();

                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    appointmentsList.add(appointmentSnapshot.getValue(String.class));
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SeeScheduleActivity.this, android.R.layout.simple_list_item_1, appointmentsList);
                listViewAppointments.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SeeScheduleActivity", "Error retrieving appointments: " + databaseError.getMessage());
            }
        });
    }

    private void updateListForSelectedDate(Calendar selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDateString = dateFormat.format(selectedDate.getTime());

        appointmentsRef.orderByChild("adminId").equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appointmentsList.clear();

                        for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                            // Assuming you have a 'date' field in your appointment model
                            String appointmentDate = appointmentSnapshot.child("date").getValue(String.class);

                            // Filter appointments for the selected date
                            if (selectedDateString.equals(appointmentDate)) {
                                appointmentsList.add(appointmentSnapshot.getValue(String.class));
                            }
                        }

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
