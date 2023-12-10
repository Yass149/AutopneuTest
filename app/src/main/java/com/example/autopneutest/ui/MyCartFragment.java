package com.example.autopneutest.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autopneutest.R;

import java.util.ArrayList;
import java.util.List;

public class MyCartFragment extends Fragment {

    private List<CartItem> cartItems;
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        initializeRecyclerView(view);
        return view;
    }

    private void initializeRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize your cartItems (retrieve from wherever you store them)
        cartItems = getCartItems();

        // Initialize and set your CartAdapter
        cartAdapter = new CartAdapter(cartItems);
        recyclerView.setAdapter(cartAdapter);
    }

    // Your getCartItems method goes here
    private List<CartItem> getCartItems() {
        // Assuming you have a list of CartItems stored locally
        List<CartItem> cartItems = new ArrayList<>();

        // Example CartItems
        CartItem item1 = new CartItem("Product 1", 20.0, 2);
        CartItem item2 = new CartItem("Product 2", 30.0, 1);
        CartItem item3 = new CartItem("Product 3", 15.0, 3);

        // Add items to the list
        cartItems.add(item1);
        cartItems.add(item2);
        cartItems.add(item3);

        return cartItems;
    }
}
