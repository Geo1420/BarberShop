package com.example.barbershop;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UserProfileActivity extends AppCompatActivity {

    private static final int  CAMERA_PERMISSION_CODE = 1;
    private static final int  CAMERA_REQUEST_CODE= 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


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
}