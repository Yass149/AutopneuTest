package com.example.autopneutest.ui;

public class CartItem {
    private String productName;
    private double price;
    private int quantity;

    public CartItem() {
        // Required default constructor for Firebase
    }

    public CartItem(String productName, double price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // You can add setters if needed

    @Override
    public String toString() {
        return "CartItem{" +
                "productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
