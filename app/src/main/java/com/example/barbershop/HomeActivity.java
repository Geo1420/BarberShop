package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void loadHistoricActivity(View view) {
        Intent historicIntent =  new Intent(getApplicationContext(), HistoricActivity.class);
        startActivity(historicIntent);
    }

    public void loadBookActivity(View view) {
        Intent bookIntent =  new Intent(getApplicationContext(), BookingActivity.class);
        startActivity(bookIntent);
    }

    public void loadNotificationActivity(View view) {
        Intent notificationIntent =  new Intent(getApplicationContext(), NotificationActivity.class);
        startActivity(notificationIntent);
    }

    public void loadProfileActivity(View view) {
        Intent profileIntent =  new Intent(getApplicationContext(), UserProfileActivity.class);
        startActivity(profileIntent);
    }

    public void logOut(View view) {
        Intent logOutIntent =  new Intent(getApplicationContext(), MainActivity.class);
        startActivity(logOutIntent);
    }
}