package com.example.autopneutest.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.autopneutest.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

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

        // Bind data to the ViewHolder components
        holder.productNameTextView.setText(cartItem.getProductName());
        holder.priceTextView.setText(String.valueOf(cartItem.getPrice()));
        holder.quantityTextView.setText(String.valueOf(cartItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView priceTextView;
        TextView quantityTextView;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your ViewHolder components
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
        }
    }
}
