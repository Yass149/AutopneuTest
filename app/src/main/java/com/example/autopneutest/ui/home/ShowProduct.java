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

import androidx.appcompat.app.AppCompatActivity;

import com.example.autopneutest.QuantityStorage;
import com.example.autopneutest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ShowProduct extends AppCompatActivity {

    private EditText editTextMessage; // List to store selected products
    private EditText fullNameEditText; // Add this line
    private EditText addressEditText;
    private QuantityStorage quantityStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);

        quantityStorage = QuantityStorage.getInstance(); // Get the QuantityStorage instance

        Intent intent = getIntent();
        String imageName = intent.getStringExtra("imageName");

        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        if (resourceId != 0) {
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageResource(resourceId);
        } else {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
        }
        editTextMessage = findViewById(R.id.editTextMessage);
        fullNameEditText = findViewById(R.id.fullNameEditText); // Add this line
        addressEditText = findViewById(R.id.addressEditText);
        Button btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView descriptionTextView = findViewById(R.id.descriptionTextView1);
        setDescriptionText(descriptionTextView, imageName);
    }

    private void setDescriptionText(TextView descriptionTextView, String imageName) {
        String descriptionText = "";

        if (imageName.equals("mercedes_c300_2022")) {
            descriptionText = "BRIDGESTONE ALENZA 225/45R18\nPrix: 1 600 MAD";
        } else if (imageName.equals("mercedes_e350_2022")) {
            descriptionText = "BRIDGESTONE POTENZA RE980AS+ 245/45R18 XL\nPrix: 1 900 MAD";
        } else if (imageName.equals("bmw_x1")) {
            descriptionText = "TOYO PROXES SPORT A/S 225/50R18\nPrix: 2 100 MAD";
        } else if (imageName.equals("bmw_m4_2022")) {
            descriptionText = "CONTINENTAL'S SPORTCONTACT 6 XL 225/35R20\nPrix: 3 300 MAD";
        } else if (imageName.equals("dacia_logan")) {
            descriptionText = "PIRELLI 185/65R15 88T\nPrix: 700 MAD";
        } else if (imageName.equals("dacia_sandero")) {
            descriptionText = "GITISYNERGY 205/60R16 92H\nPrix: 750 MAD";
        }

        descriptionTextView.setText(descriptionText);
    }

    public void sendMessage(View view) {
        // This method is called when the user clicks a button to send the message
        String message = editTextMessage.getText().toString();

        // Add the quantity to Firestore using QuantityStorage
        Intent intent = getIntent();
        String productId = intent.getStringExtra("imageName");

        String fullName = fullNameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        sendQuantityToFirestore(productId, message, fullName, address);

        // For now, let's just display a Toast message with the entered text
        Toast.makeText(this, "Quantity added: " + message, Toast.LENGTH_SHORT).show();
    }
    private void sendQuantityToFirestore(String productId, String quantity, String fullName, String address) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a Map to store the data
        Map<String, Object> data = new HashMap<>();
        data.put("productId", productId);
        data.put("value", quantity);
        data.put("fullName", fullName);
        data.put("address", address);


        // Add the data to Firestore
        db.collection("quantities").document().set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Log for debugging
                            Log.d("ShowProduct", "Quantity sent to Firestore: " + quantity + ", " + fullName + ", " + address);
                        } else {
                            // Log for debugging
                            Log.e("ShowProduct", "Error sending quantity to Firestore", task.getException());
                        }
                    }
                });
    }
}

