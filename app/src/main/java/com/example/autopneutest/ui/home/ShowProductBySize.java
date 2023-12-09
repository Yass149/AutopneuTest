package com.example.autopneutest.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.autopneutest.QuantityStorage;
import com.example.autopneutest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ShowProductBySize extends AppCompatActivity {

    private Button btnBack;
    private TextView descriptionTextView;
    private EditText quantityEditText;
    private Button sendToAdminButton;
    private QuantityStorage quantityStorage;
    private EditText addressEditText;
    private EditText fullNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_by_size);

        // Retrieve the image name from the Intent
        Intent intent = getIntent();
        String imageName = intent.getStringExtra("imageName");

        // Initialize quantityEditText and sendToAdminButton
        quantityEditText = findViewById(R.id.quantityEditText);
        sendToAdminButton = findViewById(R.id.sendToAdminButton);
        addressEditText = findViewById(R.id.addressEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        quantityStorage = QuantityStorage.getInstance();

        // Use the image name to display the corresponding image
        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        // Check if the resource exists before setting the image
        if (resourceId != 0) {
            ImageView imageView = findViewById(R.id.imageView); // Replace with your actual ImageView ID
            imageView.setImageResource(resourceId);
        } else {
            // Handle the case where the resource does not exist
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
        }

        sendToAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered quantity from quantityEditText
                String quantity = quantityEditText.getText().toString().trim();

                if (!quantity.isEmpty()) {
                    // Log for debugging
                    Log.d("ShowProductBySize", "Quantity entered: " + quantity);

                    // Send quantity to Firestore
                    sendQuantityToFirestore(imageName, quantity);

                    Toast.makeText(ShowProductBySize.this, "Message Sent", Toast.LENGTH_SHORT).show();
                } else {
                    // Log for debugging
                    Log.d("ShowProductBySize", "Quantity is empty");

                    // Display a message if quantity is empty
                    Toast.makeText(ShowProductBySize.this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Get the back button
        btnBack = findViewById(R.id.backButton1);

        // Set a click listener on the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity to go back
                finish();
            }
        });

        // Initialize descriptionTextView
        descriptionTextView = findViewById(R.id.descriptionTextView1);

        // Set description text
        setDescriptionText(imageName);
    }

    private void setDescriptionText(String imageName) {
        String descriptionText = "";

        // Set the description text based on the selected image
        if (imageName.equals("p235_55_r17")) {
            descriptionText = "PIRELLI SCORPION 235/55R17\nPrix: 1 650 MAD";
        } else if (imageName.equals("p235_65_r16")) {
            descriptionText = "MICHELIN 235/65R16 C 115R\nPrix: 1 950 MAD";
        } else if (imageName.equals("p235_65_r17")) {
            descriptionText = "HANKOOK DYNAPRO HP2 RA33 235/65R17 108H XL\nPrix: 1 350 MAD";
        } else if (imageName.equals("p195_65_r16")) {
            descriptionText = "MICHELIN 195/65R16 92V\nPrix: 1 380 MAD";
        } else if (imageName.equals("p195_55_r16")) {
            descriptionText = "FIRESTONE RHAWK 195/55R16\nPrix: 950 MAD";
        }

        // Set the description text to the TextView
        descriptionTextView.setText(descriptionText);
    }

    private void sendQuantityToFirestore(String imageName, String quantity) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String address = addressEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();
        // Create a Map to store the data
        Map<String, Object> data = new HashMap<>();
        data.put("productId", imageName);
        data.put("value", quantity);
        data.put("address", address);
        data.put("fullName", fullName); // Store quantity as a string

        // Add the data to Firestore
        db.collection("quantities").document().set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Log for debugging
                            Log.d("ShowProductBySize", "Quantity sent to Firestore: " + quantity);
                        } else {
                            // Log for debugging
                            Log.e("ShowProductBySize", "Error sending quantity to Firestore", task.getException());
                        }
                    }
                });
    }

}
