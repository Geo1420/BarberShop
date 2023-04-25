package com.example.barbershop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName,etEmail;
    private EditText etPhone, etPassword, etRepPassword;
    private FirebaseAuth authUser;
    private FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = findViewById(R.id.register_first_name_editText);
        etLastName = findViewById(R.id.register_last_name_editText);
        etEmail = findViewById(R.id.register_email_editText);
        etPhone = findViewById(R.id.register_phone_editText);
        etPassword = findViewById(R.id.register_password_editText);
        etRepPassword = findViewById(R.id.register_confirm_password_editText);


        Button buttonRegister = findViewById(R.id.register_button);
        buttonRegister.setOnClickListener(view -> {

            //Obtain the entered data
            String textFirstName = etFirstName.getText().toString();
            String textLastName = etLastName.getText().toString();
            String textEmail = etEmail.getText().toString();
            String textMobile = etPhone.getText().toString();
            String textPassword = etPassword.getText().toString();
            String textConfirmPwd = etRepPassword.getText().toString();

            //verify if the fields are empty
            verifyInputFields(textFirstName,textLastName, textEmail, textMobile, textPassword, textConfirmPwd);

        });
    }
    private void verifyInputFields(String textFirstName,String textLastName, String textEmail, String textMobile, String textPassword, String textConfirmPwd)
    {
        if(TextUtils.isEmpty(textFirstName)) {
            Toast.makeText(RegisterActivity.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
            etFirstName.setError("Required field");
            etFirstName.requestFocus();
        }else if(TextUtils.isEmpty(textLastName)){
            Toast.makeText(RegisterActivity.this, "Please enter last email", Toast.LENGTH_SHORT).show();
            etLastName.setError("Required field");
            etLastName.requestFocus();
        } else if(TextUtils.isEmpty(textEmail)){
            Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            etEmail.setError("Required field");
            etEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(textPassword)){
            Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            etPassword.setError("Required field");
            etPassword.requestFocus();
        }
        else if(TextUtils.isEmpty(textConfirmPwd)){
            Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            etRepPassword.setError("Required field");
            etRepPassword.requestFocus();
        }else if(TextUtils.isEmpty(textMobile)){
            Toast.makeText(RegisterActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            etPhone.setError("Required field");
            etPhone.requestFocus();
        }else if(textMobile.length() != 10){
            Toast.makeText(RegisterActivity.this, "Please re-enter your phone number", Toast.LENGTH_SHORT).show();
            etPhone.setError("Required 10 digits");
            etPhone.requestFocus();
        }else if(!textPassword.equals(textConfirmPwd)){
            Toast.makeText(RegisterActivity.this, "Password not the same", Toast.LENGTH_SHORT).show();
            etRepPassword.setError("Password not the same");
            etRepPassword.requestFocus();
        }else {
            registerUser(textEmail, textMobile);
        }
    }

    private void registerUser( String textEmail, String textPassword)
    {
        authUser = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        authUser.createUserWithEmailAndPassword(textEmail,textPassword).addOnSuccessListener(authResult -> {
            //called when a user is created successfully
            FirebaseUser user = authUser.getCurrentUser();
            Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
            DocumentReference documentReference = fStore.collection("users").document(user.getUid());
            //store the data
            Map<String,Object> userInfo = new HashMap<>();
            userInfo.put("firstName",etFirstName.getText().toString());
            userInfo.put("lastName", etLastName.getText().toString());
            userInfo.put("email", etEmail.getText().toString());
            userInfo.put("phone", etPhone.getText().toString());
            userInfo.put("purl", "null");
            userInfo.put("isUser", "1");
            documentReference.set(userInfo);
            Intent goToLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(goToLoginIntent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(RegisterActivity.this, "Account already taken!", Toast.LENGTH_SHORT).show();
            Intent goToLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(goToLoginIntent);
            finish();
        });
    }

    public void goToLogin(View view) {
        Intent goToLoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(goToLoginIntent);
    }
}