package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton, registerButton;
        //set the buttons
        loginButton = findViewById(R.id.main_login_button);
        registerButton = findViewById(R.id.main_register_button);
    }

    public void goToLogin(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void goToRegister(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}