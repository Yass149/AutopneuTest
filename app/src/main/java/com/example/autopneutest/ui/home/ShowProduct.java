package com.example.autopneutest.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autopneutest.R;

public class ShowProduct extends AppCompatActivity {

    Button btnBack;
    TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);

        // Retrieve the image name from the Intent
        Intent intent = getIntent();
        String imageName = intent.getStringExtra("imageName");

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

        // Get the back button
        btnBack = findViewById(R.id.backButton);

        // Set a click listener on the back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the current activity to go back
                finish();
            }
        });

        // Initialize descriptionTextView
        descriptionTextView = findViewById(R.id.descriptionTextView);

        // Set description text
        setDescriptionText(imageName);
    }

    private void setDescriptionText(String imageName) {
        String descriptionText = "";

        // Set the description text based on the selected image
        if (imageName.equals("mercedes_c300_2022")) {
            descriptionText = "BRIDGESTONE ALENZA 225/45R18     Prix: 1600 MAD";
        } else if (imageName.equals("mercedes_e350_2022")) {
            descriptionText = "BRIDGESTONE POTENZA RE980AS+ 245/45R18 XL    Prix: 1900 MAD";
        } else if (imageName.equals("bmw_x1")) {
            descriptionText = "TOYO PROXES SPORT A/S 225/50R18      Prix: 2100 MAD";
        } else if (imageName.equals("bmw_m4_2022")) {
            descriptionText = "CONTINENTAL'S SPORTCONTACT 6 XL 225/35R20       Prix: 3300 MAD";
        } else if (imageName.equals("dacia_logan")) {
            descriptionText = "PIRELLI 185/65R15 88T        Prix: 700 MAD";
        } else if (imageName.equals("dacia_sandero")) {
            descriptionText = "GITISYNERGY 205/60R16 92H        Prix: 750 MAD";
        }

        // Set the description text to the TextView
        descriptionTextView.setText(descriptionText);
    }
}
