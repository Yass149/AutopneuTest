package com.example.autopneutest;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Appointment {
    private String orderDate;
    private String address;
    private String clientName;
    private String appointmentId;

    // Default constructor (required for Firebase)
    public Appointment() {
    }

    // Constructor for creating appointments
    public Appointment(String clientName, String orderDate, String address) {
        this.clientName = clientName;
        this.orderDate = orderDate;
        this.address = address;
    }

    // Getters and setters

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }
    // Exclude annotation to prevent 'clientName' from being serialized to Firestore
    @Exclude
    public static Appointment fromDocumentSnapshot(DocumentSnapshot document) {
        Appointment appointment = new Appointment();
        if (document.exists()) {
            Map<String, Object> data = document.getData();
            if (data != null) {
                appointment.clientName = (String) data.get("clientName");
                appointment.orderDate = (String) data.get("orderDate");
                appointment.address = (String) data.get("address");
            }
        }
        return appointment;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("clientName", clientName);
        map.put("orderDate", orderDate);
        map.put("address", address);
        return map;
    }

    @Override
    public String toString() {
        return "Client Name: " + clientName + "\nOrder Date: " + orderDate + "\nAddress: " + address;
    }
}
