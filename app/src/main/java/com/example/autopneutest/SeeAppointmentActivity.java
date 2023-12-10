package com.example.autopneutest;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class SeeAppointmentActivity extends AppCompatActivity {

    private TextView quantityTextView;
    private TextView addressTextView;
    private TextView fullnameTextView;
    private String address = ""; // Declare at class level
    private String fullName = ""; // Declare at class level

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_appointment);
        Log.d("SeeAppointmentActivity", "Activity created");

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize TextViews
        quantityTextView = findViewById(R.id.quantityTextView);
        addressTextView = findViewById(R.id.addressTextView);
        fullnameTextView = findViewById(R.id.fullnameTextView);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("quantities").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("SeeAppointmentActivity", "Firestore Error: ", e);
                    return;
                }

                if (querySnapshot != null) {
                    StringBuilder quantitiesText = new StringBuilder();

                    // Inside the loop where you retrieve data
                    for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED
                                || documentChange.getType() == DocumentChange.Type.MODIFIED) {

                            String productId = documentChange.getDocument().getString("productId");
                            String value = documentChange.getDocument().getString("value");
                            address = documentChange.getDocument().getString("address");
                            fullName = documentChange.getDocument().getString("fullName");

                            Log.d("SeeAppointmentActivity", "ProductId: " + productId +
                                    ", Value: " + value +
                                    ", Address: " + address +
                                    ", FullName: " + fullName);
                            Log.d("SeeAppointmentActivity", "Document ID: " + documentChange.getDocument().getId() +
                                    ", ProductId: " + productId +
                                    ", Value: " + value +
                                    ", Address: " + address +
                                    ", Fullname before: " + fullName);

                            fullName = documentChange.getDocument().getString("fullName");

                            Log.d("SeeAppointmentActivity", "FullName after: " + fullName);


                            // Inside the loop where you process Firestore changes
                            if (value != null) {
                                quantitiesText.append("Product ID: ").append(productId)
                                        .append(", Quantity: ").append(value)
                                        .append(", Address: ").append(address)
                                        .append(", FullName: ").append(fullName)
                                        .append("\n");

                                // Log the fullname before UI update
                                Log.d("SeeAppointmentActivity", "FullName before UI update: " + fullName);


                            }

                        }
                    }



                    // Update UI on the main thread
                    runOnUiThread(() -> {
                        Log.d("SeeAppointmentActivity", "Inside runOnUiThread");
                        quantityTextView.setText(quantitiesText.toString());
                        addressTextView.setText("Address: " + address);
                        fullnameTextView.setText("Fullname: " + fullName);
                        Log.d("SeeAppointmentActivity", "Quantities updated: " + quantitiesText.toString());
                    });
                } else {
                    Log.d("SeeAppointmentActivity", "No quantities available.");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
