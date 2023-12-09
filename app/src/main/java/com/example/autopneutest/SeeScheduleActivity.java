package com.example.autopneutest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SeeScheduleActivity extends AppCompatActivity {

    private FirebaseFirestore db;
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

        db = FirebaseFirestore.getInstance();
        appointmentsList = new ArrayList<>();
        listViewAppointments = findViewById(R.id.listViewAppointments);
        calendarView = findViewById(R.id.calendarView);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("appointments", MODE_PRIVATE);

        // Load appointments from SharedPreferences
        loadAppointmentsFromSharedPreferences();

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

        // Set up the listener for item clicks in the ListView
        listViewAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click, e.g., show a dialog to confirm deletion
                showDeleteConfirmationDialog(position);
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

        // Load appointments from Firestore
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear) {
            clearAppointments();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadAppointmentsFromFirebase(String clientName) {
        db.collection("appointments")
                .whereEqualTo("clientname", clientName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            appointmentsList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Appointment appointment = document.toObject(Appointment.class);
                                appointmentsList.add(appointment);
                            }

                            // Save appointments to SharedPreferences
                            saveAppointmentsToSharedPreferences();

                            // Update the UI with client names
                            updateUI();
                        } else {
                            Log.e("SeeScheduleActivity", "Error retrieving appointments: " + task.getException());
                        }
                    }
                });
    }

    private void updateListForSelectedDate(Calendar selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDateString = dateFormat.format(selectedDate.getTime());

        db.collection("appointments")
                .whereEqualTo("clientname", mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                        appointmentsList.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
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

                // Save the Appointment to Firestore
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
        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        appointment.setAppointmentId(documentReference.getId());
                        appointmentsList.add(appointment);

                        // Save appointments to SharedPreferences
                        saveAppointmentsToSharedPreferences();

                        Toast.makeText(SeeScheduleActivity.this, "Appointment saved successfully", Toast.LENGTH_SHORT).show();

                        // Update the UI with the new data
                        updateUI();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SeeScheduleActivity", "Error saving appointment: " + e.getMessage());
                    }
                });
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

    private void clearAppointments() {
        // Show a confirmation dialog before clearing all appointments
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete all appointments?");
        builder.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Clear appointmentsList and update the UI
                appointmentsList.clear();

                // Notify the adapter or update UI components
                updateUI();

                // You may also want to remove data from Firestore if needed
                // ...

                // Clear appointments from SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("appointments");
                editor.apply();

                Toast.makeText(SeeScheduleActivity.this, "All appointments deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this appointment?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAppointment(position);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteAppointment(int position) {
        if (position >= 0 && position < appointmentsList.size()) {
            // Remove the selected appointment from Firestore
            db.collection("appointments")
                    .document(appointmentsList.get(position).getAppointmentId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Remove the selected appointment locally
                            Appointment deletedAppointment = appointmentsList.remove(position);

                            // Notify the adapter or update UI components
                            updateUI();

                            // Save appointments to SharedPreferences
                            saveAppointmentsToSharedPreferences();

                            Toast.makeText(SeeScheduleActivity.this, "Appointment deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("SeeScheduleActivity", "Error deleting appointment: " + e.getMessage());
                        }
                    });
        }
    }
}
