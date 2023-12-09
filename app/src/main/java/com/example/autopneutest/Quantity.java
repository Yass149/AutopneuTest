package com.example.autopneutest;

public class Quantity {
    private String value;

    // Empty constructor needed for Firestore
    public Quantity() {
    }

    public Quantity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
