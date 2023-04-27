package com.example.barbershop;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private TextView tvFirstName, tvLastName, tvEmail;
    private EditText etFirstName, etLastName, etPhone;
    private static final int  CAMERA_PERMISSION_CODE = 1;
    private static final int  CAMERA_REQUEST_CODE= 2;
    private String userID;
    private static final String USERS = "users";
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        tvFirstName = findViewById(R.id.first_name_profile);
        tvLastName = findViewById(R.id.last_name_profile);
        etFirstName = findViewById(R.id.profile_first_name_editText);
        etLastName = findViewById(R.id.profile_last_name_editText);
        tvEmail = findViewById(R.id.profile_email_editText);
        etPhone = findViewById(R.id.profile_phone_editText);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            userID = user.getUid();
        }
        DocumentReference documentReference = fStore.collection(USERS).document(userID);
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Get the user data here
                etFirstName.setText(documentSnapshot.getString("firstName"));
                etLastName.setText(documentSnapshot.getString("lastName"));
                etPhone.setText(documentSnapshot.getString("phone"));
                tvEmail.setText(documentSnapshot.getString("email"));
                tvFirstName.setText(documentSnapshot.getString("firstName"));
                tvLastName.setText(documentSnapshot.getString("lastName"));

            } else {
                Log.d(TAG, "No such document");
            }
        });
        //Camera permission
        Button cameraButton = findViewById(R.id.profile_camera_button);
        cameraButton.setOnClickListener(view -> {
            if(ContextCompat.checkSelfPermission(this,"android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
            }else {
                ActivityCompat.requestPermissions(this,new String[]{"android.permission.CAMERA"},CAMERA_PERMISSION_CODE);
            }
        });

        Button backButton = findViewById(R.id.profile_back_button);
        backButton.setOnClickListener(view -> {
            Intent homeIntent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(homeIntent);
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST_CODE);
            } else {
                // Permission denied
                Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode ==  CAMERA_REQUEST_CODE){
                assert data != null;
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // Set the image to an ImageView
                ImageView profileImage = findViewById(R.id.profile_image);
                profileImage.setImageBitmap(imageBitmap);
            }
        }
    }

    public void saveUserProfile(View view) {

        DocumentReference documentReference = fStore.collection(USERS).document(userID);
        Map<String,Object> profileData =  new HashMap<>();
        profileData.put("firstName", etFirstName.getText().toString());
        profileData.put("lastName",etLastName.getText().toString());
        profileData.put("phone", etPhone.getText().toString());
        documentReference.update(profileData).addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(), "Changes saved!", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error updating the profile!", Toast.LENGTH_SHORT).show());
    }
}