package com.example.fmgymtrainer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    TextInputLayout editEmail, editPassword;
    ProgressDialog progressDialog;
    FirebaseAuth logAuth;
    DatabaseReference dbRef, dbRef1;
    FirebaseFirestore fstrore;

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.RegEmail);
        editPassword = findViewById(R.id.RegPassword);

        progressDialog = new ProgressDialog(this);

        fstrore = FirebaseFirestore.getInstance();
        logAuth = FirebaseAuth.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child("Trainer");
        dbRef1 = FirebaseDatabase.getInstance().getReference("Users").child("Trainee");

        linearLayout = findViewById(R.id.linear);
    }

    public void Login(View view) {
        PerformAuth();
    }

    private void PerformAuth() {

        String email, password;

        email = editEmail.getEditText().getText().toString().trim();
        password = editPassword.getEditText().getText().toString().trim();

        if (!isConnected(Login.this)){
            showCustomDialog();
        }

        else if (email.isEmpty() || password.isEmpty() || password.length()< 8){
            Snackbar.make(linearLayout, "Login is Unsuccessful!", Snackbar.LENGTH_SHORT).show();
        }

        else {
            progressDialog.setMessage("Please wait, Logging in progress");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            logAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    progressDialog.dismiss();
                    Snackbar.make(linearLayout, "Login is Successful!", Snackbar.LENGTH_SHORT).show();
                    userChecking(authResult.getUser().getUid());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.cancel();
                    Snackbar.make(linearLayout, "Email or Password is Invalid!", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void userChecking(String uid) {

        DocumentReference docRef = fstrore.collection("Users").document(uid);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

                if (documentSnapshot.getString("Trainer") != null){
                    progressDialog.dismiss();
                    Intent intent = new Intent(Login.this, TrainerDashboard.class);
                    startActivity(intent);
                }
                if (documentSnapshot.getString("Trainee") != null){
                    progressDialog.dismiss();
                    Intent intent = new Intent(Login.this, TraineeDashboard.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void showCustomDialog() {
        Snackbar.make(linearLayout, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Connect", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .show();
    }

    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileCon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiCon != null && wifiCon.isConnected()) || (mobileCon != null && mobileCon.isConnected())){
            return true;
        }
        else {
            return false;
        }
    }

    public void forg(View view) {

    }

    public void reg(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }
}