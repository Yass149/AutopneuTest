package com.example.autopneutest.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.autopneutest.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Locale;

// CartAdapter.java
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;

    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.productNameTextView.setText(cartItem.getProductName());
        holder.priceTextView.setText(String.format(Locale.getDefault(), "Price: %.2f", cartItem.getPrice()));
        holder.quantityTextView.setText(String.format(Locale.getDefault(), "Quantity: %d", cartItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView productNameTextView;
        private TextView priceTextView;
        private TextView quantityTextView;
        private ImageButton removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            removeButton = itemView.findViewById(R.id.removeButton);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Implement logic to remove the item from the cart
                        // You may want to update Firebase and the local list
                        // For simplicity, we'll just display a toast message
                        Toast.makeText(itemView.getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
        }}
    });

}}}

