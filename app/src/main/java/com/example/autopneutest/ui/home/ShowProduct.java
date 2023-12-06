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

import java.util.ArrayList;
import java.util.List;

public class ShowProduct extends AppCompatActivity {

    private List<String> cartItems; // List to store selected products

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);

        cartItems = new ArrayList<>(); // Initialize the cartItems list

        Intent intent = getIntent();
        String imageName = intent.getStringExtra("imageName");

        int resourceId = getResources().getIdentifier(imageName, "drawable", getPackageName());

        if (resourceId != 0) {
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageResource(resourceId);
        } else {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show();
        }

        Button btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        setDescriptionText(descriptionTextView, imageName);

        Button btnAddToCart = findViewById(R.id.addToCartButton);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(imageName);
            }
        });
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

    private void addToCart(String imageName) {
        // Add the selected product to the cartItems list
        cartItems.add(imageName);
        Toast.makeText(this, "Product added to cart: " + imageName, Toast.LENGTH_SHORT).show();
    }
}
