package com.example.barbershop;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {
    private ArrayList<String> availableHours;
    private Calendar calendar;
    private Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        ListView listView = findViewById(R.id.list_available_hours);

        // Populate the available hours
        availableHours = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        while (calendar.get(Calendar.HOUR_OF_DAY) <= 17) {
            availableHours.add(sdf.format(calendar.getTime()));
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }
        // Set the adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_hour, R.id.tv_hour, availableHours);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Handle the selection
            String selectedHour = availableHours.get(position);
            calendar.set(Calendar.HOUR_OF_DAY,position +9);
            calendar.set(Calendar.MINUTE, 0);
            view.setActivated(true);
            Toast.makeText(BookingActivity.this, "You selected " + selectedHour, Toast.LENGTH_SHORT).show();
        });


        dateButton = findViewById(R.id.date_button);
        dateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, (view, year, month, dayOfMonth) -> {
                // Update the calendar with the selected date
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Update the text of the button to display the selected date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                dateButton.setText(dateFormat.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });


        Button bookButton =  findViewById(R.id.appointment_button);
        bookButton.setOnClickListener(view -> {

            String userId = firebaseUser.getUid();
            Timestamp timestamp = new Timestamp(calendar.getTime());

            FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

            DocumentReference subCollectionRef = fireStore.collection("appointments").document();
            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", timestamp);
            data.put("userID", userId);
            subCollectionRef.set(data)
                    .addOnSuccessListener(documentReference -> Toast.makeText(BookingActivity.this,"Appointment added!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(BookingActivity.this,"Error!", Toast.LENGTH_SHORT).show());
        });

        Button backButton = findViewById(R.id.booking_back_button);
        backButton.setOnClickListener(view -> {
            Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(homeIntent);
        });

    }
}