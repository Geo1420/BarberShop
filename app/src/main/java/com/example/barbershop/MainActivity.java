package com.example.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToLogin(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void goToRegister(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}