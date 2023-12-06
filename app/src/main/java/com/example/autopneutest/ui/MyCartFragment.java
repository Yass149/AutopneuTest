package com.example.autopneutest.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autopneutest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

// MyCartFragment.java
public class MyCartFragment extends Fragment {

    private List<CartItem> cartItems;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private Button checkoutButton;

    // Firebase
    private DatabaseReference cartRef;
    private ValueEventListener cartListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        cartRef = database.getReference("user_carts").child(userId);

        // Initialize UI elements
        recyclerView = view.findViewById(R.id.recyclerView);
        checkoutButton = view.findViewById(R.id.checkoutButton);

        // Initialize cartItems (retrieve them from Firebase or elsewhere)
        cartItems = new ArrayList<>();

        // Initialize RecyclerView and set up the adapter
        cartAdapter = new CartAdapter(cartItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(cartAdapter);

        // Set up the checkout button click listener
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement the checkout process (e.g., navigate to a checkout activity)
                // You can also handle payment processing and order confirmation here
                // For simplicity, we'll just display a toast message
                Toast.makeText(getActivity(), "Checkout clicked!", Toast.LENGTH_SHORT).show();
            }

        });

        // Set up Firebase real-time updates for the cart
        setUpFirebaseCartListener();

        return view;
    }

    private void setUpFirebaseCartListener() {
        cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear existing cart items
                cartItems.clear();

                // Populate the cartItems from the Firebase snapshot
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CartItem cartItem = dataSnapshot.getValue(CartItem.class);
                    if (cartItem != null) {
                        cartItems.add(cartItem);
                    }
                }

                // Notify the adapter that the data has changed
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
                Toast.makeText(getActivity(), "Failed to retrieve cart data", Toast.LENGTH_SHORT).show();
            }
        };

        // Add the ValueEventListener to the cartRef
        cartRef.addValueEventListener(cartListener);
    }

    // Add other methods for updating, removing items from the cart, etc.
}
