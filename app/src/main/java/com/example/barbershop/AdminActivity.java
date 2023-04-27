package com.example.barbershop;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    private FirebaseAuth authUser;
    private FirebaseFirestore fireStore;
    private static final String BARBERSHOP = "barbershop";
    private static final String BARBERSHOP_ID = "XmENPoxM7VEmLSq0u14v";
    private EditText etAddress, etPhone, etBarberName, etProgram, etHaircutTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        authUser = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();

        etAddress = findViewById(R.id.admin_address_editText);
        etPhone = findViewById(R.id.admin_phone_editText);
        etBarberName = findViewById(R.id.admin_barberName_edittext);
        etProgram = findViewById(R.id.admin_program_editText);
        etHaircutTime = findViewById(R.id.admin_haircut_time_editText);

        DocumentReference documentReference = fireStore.collection(BARBERSHOP).document(BARBERSHOP_ID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the user data here
                etAddress.setText(documentSnapshot.getString("address"));
                etPhone.setText(documentSnapshot.getString("phone"));
                etBarberName.setText(documentSnapshot.getString("barberName"));
                etProgram.setText(documentSnapshot.getString("program"));
                etHaircutTime.setText(documentSnapshot.getString("haircutTime"));
            } else {
                Log.d(TAG, "No such document");
            }
        });

        Button saveChanges = findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(view -> {
            DocumentReference documentReference1 = fireStore.collection(BARBERSHOP).document(BARBERSHOP_ID);
            Map<String,Object> barberShopData =  new HashMap<>();
            barberShopData.put("address", etAddress.getText().toString());
            barberShopData.put("phone",etPhone.getText().toString());
            barberShopData.put("barberName", etBarberName.getText().toString());
            barberShopData.put("program",etProgram.getText().toString());
            barberShopData.put("haircutTime", etHaircutTime.getText().toString());
            documentReference1.update(barberShopData).addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(), "Changes saved!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error updating the profile!", Toast.LENGTH_SHORT).show());
        });

        Button logOut = findViewById(R.id.admin_logout_button);
        logOut.setOnClickListener(view -> {
            authUser.signOut();
            Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
            Intent logOutIntent =  new Intent(getApplicationContext(), MainActivity.class);
            startActivity(logOutIntent);
        });


        Button bookButton = findViewById(R.id.book_button);
        bookButton.setOnClickListener(view -> {
            Intent goToBookIntent = new Intent(getApplicationContext(), BookingActivity.class);
            startActivity(goToBookIntent);
        });
    }
}