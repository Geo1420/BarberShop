package com.example.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private TextView tvAddress, tvBarberName, tvProgram, tvHaircutTime;
    private FirebaseAuth authUser;
    private static final String BARBERSHOP = "barbershop";
    private static final String BARBERSHOP_UID = "XmENPoxM7VEmLSq0u14v";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        authUser = FirebaseAuth.getInstance();

        tvAddress = findViewById(R.id.address);
        tvBarberName = findViewById(R.id.barber_name);
        tvProgram = findViewById(R.id.program);
        tvHaircutTime = findViewById(R.id.haircut_time);

        DocumentReference documentReference = fStore.collection(BARBERSHOP).document(BARBERSHOP_UID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            //extract the data
            if(documentSnapshot.exists())
            {
                tvAddress.setText(documentSnapshot.getString("address"));
                tvBarberName.setText(documentSnapshot.getString("barberName"));
                tvProgram.setText(documentSnapshot.getString("program"));
                tvHaircutTime.setText(documentSnapshot.getString("haircutTime"));
            }else {
                Toast.makeText(getApplicationContext(), "No such document in DB", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            //handel the errors
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        });
    }

    public void loadHistoricActivity(View view) {
        Intent historicIntent =  new Intent(getApplicationContext(), HistoricActivity.class);
        startActivity(historicIntent);
    }

    public void loadBookActivity(View view) {
        Intent bookIntent =  new Intent(getApplicationContext(), BookingActivity.class);
        startActivity(bookIntent);
    }

    public void loadProfileActivity(View view) {
        Intent profileIntent =  new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(profileIntent);
    }

    public void logOut(View view) {
        authUser.signOut();
        Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
        Intent logOutIntent =  new Intent(getApplicationContext(), MainActivity.class);
        startActivity(logOutIntent);
    }
}