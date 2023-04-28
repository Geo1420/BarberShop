package com.example.barbershop;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private Context context;

    public LoginAsyncTask(FirebaseAuth auth, FirebaseFirestore fStore,Context context) {
        mAuth = auth;
        this.fStore = fStore;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String email = params[0];
        String password = params[1];
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    checkUserAccessLevel(authResult.getUser().getUid());
                }
            });
            return true;
        } catch (Exception e) {
            Log.e("FirebaseLoginTask", "Error occurred while logging in", e);
            return false;
        }
    }
    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("users").document(uid);
        //extract data from the document
        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG", "onSuccess" + documentSnapshot.getData());

            if(Objects.equals(documentSnapshot.getString("isUser"), "1")) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else {
                Intent intent = new Intent(context, AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
}
