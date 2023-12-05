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

public class ShowProductBySize extends AppCompatActivity {

    Button btnBack;
    TextView descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product_by_size);

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
}
