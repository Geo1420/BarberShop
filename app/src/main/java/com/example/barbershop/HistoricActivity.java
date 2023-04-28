package com.example.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barbershop.models.AppointmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HistoricActivity extends AppCompatActivity {

    private static final String APPOINTMENTS = "appointments";
    private ArrayAdapter<AppointmentModel> adapter;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        FirebaseFirestore mDb = FirebaseFirestore.getInstance();

        ListView appointmentListView = findViewById(R.id.appointmentsListView);
        adapter = new ArrayAdapter<>(HistoricActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>());
        appointmentListView.setAdapter(adapter);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            userID = user.getUid();
        }
        //retrieve the data
        CollectionReference usersRef = mDb.collection(APPOINTMENTS);
        Query query = usersRef.whereEqualTo("userID", userID);

        query.get().addOnSuccessListener(querySnapshot -> {
            if (!querySnapshot.isEmpty()) {
                // The query returned at least one document
                ArrayList<AppointmentModel> appointments = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    // You can access the data of each document using documentSnapshot.getData()
                    AppointmentModel a = new AppointmentModel(documentSnapshot.getTimestamp("timestamp"),documentSnapshot.getString("userID"));
                    appointments.add(a);
                    // Do something with the data...
                }
                appointments = sortTimestampList(appointments);
                adapter.addAll(appointments);
            } else {
                // No documents were found that match the query
                Log.d("TAG", "No matching documents");
            }
        }).addOnFailureListener(e -> {
            // There was an error while retrieving the documents
            Log.w("TAG", "Error getting documents", e);
        });

        Button backButton = findViewById(R.id.historic_back_button);
        backButton.setOnClickListener(view -> {
            Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(homeIntent);
        });
    }
    public ArrayList<AppointmentModel> sortTimestampList(ArrayList<AppointmentModel> timestampList) {
        // Define a custom Comparator to compare Timestamp values
        Comparator<AppointmentModel> timestampComparator = (timestamp1, timestamp2) -> timestamp1.getAppointmentTime().compareTo(timestamp2.getAppointmentTime());

        // Sort the list using the custom Comparator
        Collections.sort(timestampList, timestampComparator);
        Collections.reverse(timestampList);
        // Return the sorted list
        return timestampList;
    }
}