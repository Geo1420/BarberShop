package com.example.barbershop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerEditTextFullName, registerEditTextEmail,registerEditTextPwd,registerEditTextConfirmPwd,
            registerEditTextMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEditTextFullName = findViewById(R.id.register_full_name_editText);
        registerEditTextEmail = findViewById(R.id.register_email_editText);
        registerEditTextPwd = findViewById(R.id.register_password_editText);
        registerEditTextConfirmPwd = findViewById(R.id.register_confirm_password_editText);
        registerEditTextMobile = findViewById(R.id.register_phone_editText);

        Button buttonRegister = findViewById(R.id.register_button);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtain the entered data
                String textFullName = registerEditTextFullName.getText().toString();
                String textEmail = registerEditTextEmail.getText().toString();
                String textMobile = registerEditTextMobile.getText().toString();
                String textPassword = registerEditTextPwd.getText().toString();
                String textConfirmPwd = registerEditTextConfirmPwd.getText().toString();

                //verify if the fields are empty
                verifyInputFields(textFullName, textEmail, textMobile, textPassword, textConfirmPwd);

            }
        });
    }

    private void verifyInputFields(String textFullName, String textEmail, String textMobile, String textPassword, String textConfirmPwd)
    {
        if(TextUtils.isEmpty(textFullName)) {
            Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            registerEditTextFullName.setError("Required field");
            registerEditTextFullName.requestFocus();
        }else if(TextUtils.isEmpty(textEmail)){
            Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
            registerEditTextEmail.setError("Required field");
            registerEditTextEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(textPassword)){
            Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            registerEditTextPwd.setError("Required field");
            registerEditTextPwd.requestFocus();
        }
        else if(TextUtils.isEmpty(textConfirmPwd)){
            Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            registerEditTextConfirmPwd.setError("Required field");
            registerEditTextConfirmPwd.requestFocus();
        }else if(TextUtils.isEmpty(textMobile)){
            Toast.makeText(RegisterActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            registerEditTextMobile.setError("Required field");
            registerEditTextMobile.requestFocus();
        }else if(textMobile.length() != 10){
            Toast.makeText(RegisterActivity.this, "Please re-enter your phone number", Toast.LENGTH_SHORT).show();
            registerEditTextMobile.setError("Required 10 digits");
            registerEditTextMobile.requestFocus();
        }else if(!textPassword.equals(textConfirmPwd)){
            Toast.makeText(RegisterActivity.this, "Password not the same", Toast.LENGTH_SHORT).show();
            registerEditTextConfirmPwd.setError("Password not the same");
            registerEditTextConfirmPwd.requestFocus();
        }else {
            registerUser(textFullName, textEmail, textMobile, textPassword, textConfirmPwd);
        }
    }

    private void registerUser(String textFullName, String textEmail, String textMobile, String textPassword, String textConfirmPwd)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    //Enter user data into Firebase
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName, textEmail,textMobile);
                    //Extracting user reference from database
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,"User registered successfully", Toast.LENGTH_SHORT).show();
                                firebaseUser.sendEmailVerification();
                                //Open User profile after successful reg
                                Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                                //prevent user from returning back to Register Activity on pressing back button after registration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(RegisterActivity.this,"User registered failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    try {
                        throw  task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        registerEditTextPwd.setError("Weak password");
                        registerEditTextPwd.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        registerEditTextEmail.setError("Email invalid");
                        registerEditTextEmail.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e){
                        registerEditTextEmail.setError("This account already exists.");
                        registerEditTextEmail.requestFocus();
                    } catch (Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}