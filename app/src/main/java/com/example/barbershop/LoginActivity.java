package com.example.barbershop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.login_email_editText);
        etPassword = findViewById(R.id.login_password_editText);
    }

    public void loginAccount(View view) {
        if(etEmail.getText().toString().isEmpty())
        {
            etEmail.setError("Email field empty");
        }else if(etPassword.getText().toString().isEmpty()) {
            etPassword.setError("Password field empty");
        }else{

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            LoginAsyncTask task = new LoginAsyncTask(firebaseAuth,firebaseFirestore,getApplicationContext());
            task.execute(email, password);
        }
    }

    public void goToRegisterActivity(View view) {
        Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(registerIntent);
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = firebaseFirestore.collection("users").document(uid);
        //extract data from the document
        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess" + documentSnapshot.getData());

            if(Objects.equals(documentSnapshot.getString("isUser"), "1")) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                finish();
            }
        });
    }
}