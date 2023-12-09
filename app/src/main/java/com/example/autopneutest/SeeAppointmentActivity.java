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

        // Initialize quantityTextView
        quantityTextView = findViewById(R.id.quantityTextView);

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

                    for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED
                                || documentChange.getType() == DocumentChange.Type.MODIFIED) {

                            String productId = documentChange.getDocument().getString("productId");
                            // Check for null before trying to retrieve the String
                            String value = documentChange.getDocument().getString("value");

                            if (value != null) {
                                quantitiesText.append("Product ID: ").append(productId).append(", Quantity: ").append(value).append("\n");
                            }
                        }
                    }

                    // Update UI on the main thread
                    runOnUiThread(() -> {
                        Log.d("SeeAppointmentActivity", "Inside runOnUiThread");
                        quantityTextView.setText(quantitiesText.toString());
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
