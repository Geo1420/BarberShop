package com.example.barbershop;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barbershop.models.AppointmentModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AppointmentsActivity extends AppCompatActivity {

    private static final String APPOINTMENTS = "appointments";
    private ArrayAdapter<AppointmentModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = fireStore.collection(APPOINTMENTS);

        ListView appointmentListView = findViewById(R.id.all_appointments_listView);
        adapter = new ArrayAdapter<>(AppointmentsActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>());
        appointmentListView.setAdapter(adapter);

        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<AppointmentModel> appointments = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    AppointmentModel appointmentModel = new AppointmentModel(document.getTimestamp("timestamp"));
                    appointments.add(appointmentModel);
                }
                appointments = sortTimestampList(appointments);
                adapter.addAll(appointments);
            } else {
                Toast.makeText(AppointmentsActivity.this, "Appointments not found", Toast.LENGTH_SHORT).show();
            }
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