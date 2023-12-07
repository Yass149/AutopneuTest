package com.example.autopneutest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SeeScheduleActivity extends AppCompatActivity {

    private DatabaseReference ordersRef;
    private FirebaseAuth mAuth;

    private ListView listViewAppointments;
    private List<Appointment> appointmentsList;
    private CalendarView calendarView;
    private AlertDialog alertDialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_schedule);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Redirect to LoginActivity if the user is not authenticated
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        appointmentsList = new ArrayList<>();
        listViewAppointments = findViewById(R.id.listViewAppointments);
        calendarView = findViewById(R.id.calendarView);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("appointments", MODE_PRIVATE);

        // Load appointments from SharedPreferences
        loadAppointmentsFromSharedPreferences();

        ordersRef = FirebaseDatabase.getInstance().getReference().child("order");

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
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                updateListForSelectedDate(selectedDate);
            }
        });

        // Floating Action Button for showing the form
        FloatingActionButton fab = findViewById(R.id.fabAddAppointment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppointmentForm();
            }
        });

        // Load appointments from Firebase
        loadAppointmentsFromFirebase(currentUser.getUid());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load appointments from SharedPreferences when the activity resumes
        loadAppointmentsFromSharedPreferences();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadAppointmentsFromFirebase(String clientName) {
        ordersRef.orderByChild("clientname").equalTo(clientName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                appointmentsList.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Appointment appointment = orderSnapshot.getValue(Appointment.class);
                    appointmentsList.add(appointment);
                }

                // Save appointments to SharedPreferences
                saveAppointmentsToSharedPreferences();

                // Update the UI with client names
                updateUI();
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

        ordersRef.orderByChild("clientname").equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        appointmentsList.clear();

                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            Appointment appointment = orderSnapshot.getValue(Appointment.class);
                            String appointmentDate = appointment.getOrderDate();

                            if (selectedDateString.equals(appointmentDate)) {
                                appointmentsList.add(appointment);
                            }
                        }

                        // Save appointments to SharedPreferences
                        saveAppointmentsToSharedPreferences();

                        ArrayAdapter<Appointment> arrayAdapter = new ArrayAdapter<>(SeeScheduleActivity.this, android.R.layout.simple_list_item_1, appointmentsList);
                        listViewAppointments.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("SeeScheduleActivity", "Error retrieving appointments: " + databaseError.getMessage());
                    }
                });
    }

    private void showAppointmentForm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View formView = getLayoutInflater().inflate(R.layout.form_appointment, null);
        builder.setView(formView);

        EditText editTextClientName = formView.findViewById(R.id.editTextClientName);
        EditText editTextClientAddress = formView.findViewById(R.id.editTextClientAddress);
        DatePicker datePicker = formView.findViewById(R.id.datePicker);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String clientName = editTextClientName.getText().toString();
                String clientAddress = editTextClientAddress.getText().toString();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String selectedDate = year + "-" + String.format(Locale.getDefault(), "%02d", month) + "-" + String.format(Locale.getDefault(), "%02d", day);

                // Create an Appointment object
                Appointment newAppointment = new Appointment(clientName, selectedDate, clientAddress);

                // Save the Appointment to Firebase
                saveAppointmentToFirebase(newAppointment);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveAppointmentToFirebase(Appointment appointment) {
        String orderId = ordersRef.push().getKey();
        ordersRef.child(orderId).setValue(appointment);

        appointmentsList.add(appointment);

        // Save appointments to SharedPreferences
        saveAppointmentsToSharedPreferences();

        Toast.makeText(SeeScheduleActivity.this, "Appointment saved successfully", Toast.LENGTH_SHORT).show();

        // Update the UI with the new data
        updateUI();
    }

    private void saveAppointmentsToSharedPreferences() {
        // Save appointmentsList to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(appointmentsList);
        editor.putString("appointments", json);
        editor.apply();
    }

    private void loadAppointmentsFromSharedPreferences() {
        // Load appointmentsList from SharedPreferences
        String json = sharedPreferences.getString("appointments", "");
        if (!json.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Appointment>>() {}.getType();
            appointmentsList = gson.fromJson(json, type);
        }

        // Update the UI with the loaded data
        updateUI();
    }

    private void updateUI() {
        ArrayAdapter<Appointment> arrayAdapter = new ArrayAdapter<>(SeeScheduleActivity.this, android.R.layout.simple_list_item_1, appointmentsList);
        listViewAppointments.setAdapter(arrayAdapter);
    }
}
