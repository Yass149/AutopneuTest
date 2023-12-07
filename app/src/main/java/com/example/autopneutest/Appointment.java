package com.example.autopneutest;

public class Appointment {
    private String orderdate;
    private String address;
    private String clientname;

    // Default constructor (required for Firebase)
    public Appointment() {
    }

    // Constructor for creating appointments
    public Appointment(String clientname, String orderdate, String address) {
        this.clientname = clientname;
        this.orderdate = orderdate;
        this.address = address;
    }

    // Getters and setters
    public String getClientName() {
        return clientname;
    }

    public void setCustomerId(String clientname) {
        this.clientname = clientname;
    }

    public String getOrderDate() {
        return orderdate;
    }

    public void setOrderDate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Client Name: " + clientname + "\nOrder Date: " + orderdate + "\nAddress: " + address;
    }
}
