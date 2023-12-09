package com.example.autopneutest;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class QuantityStorage {
    private static QuantityStorage instance;
    private CollectionReference quantitiesCollection;
    private List<String> quantities;
    private ListenerRegistration eventListenerRegistration;

    // Private constructor to enforce singleton pattern
    private QuantityStorage() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        quantitiesCollection = db.collection("quantities");

        quantities = new ArrayList<>();

        // Attach a listener to retrieve data from Firestore
        EventListener<QuerySnapshot> eventListener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // Clear the existing quantities list
                quantities.clear();

                // Iterate through the data and add quantities to the list
                if (queryDocumentSnapshots != null) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        String quantity = snapshot.getString("message");
                        quantities.add(quantity);
                    }

                    // Notify any observers or update UI here
                }
            }
        };

        // Add the listener to the quantitiesCollection
        eventListenerRegistration = quantitiesCollection.addSnapshotListener(eventListener);
    }

    // Public method to get the singleton instance
    public static synchronized QuantityStorage getInstance() {
        if (instance == null) {
            instance = new QuantityStorage();
        }
        return instance;
    }

    // Public method to add a quantity to Firestore
    public void addQuantity(String quantity) {
        quantitiesCollection.add(new Quantity(quantity))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Log for debugging
                            Log.d("QuantityStorage", "Quantity added to Firestore: " + quantity);
                        } else {
                            // Log for debugging
                            Log.e("QuantityStorage", "Error adding quantity to Firestore", task.getException());
                        }
                    }
                });
    }

    // Public method to get the list of quantities
    public List<String> getQuantities() {
        // Retrieve quantities from the local list
        return quantities;
    }

    // Public method to remove the EventListener when the instance is no longer needed
    public void removeEventListener() {
        if (eventListenerRegistration != null) {
            // Remove the listener using the existing ListenerRegistration
            eventListenerRegistration.remove();
        }
    }

    // Public method to clear the local list of quantities
    public void clearQuantities() {
        quantities.clear();
    }
}
