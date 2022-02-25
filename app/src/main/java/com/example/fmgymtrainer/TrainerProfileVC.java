package com.example.fmgymtrainer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TrainerProfileVC extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth logAuth;
    FirebaseDatabase fbBase;
    FirebaseUser user;
    DatabaseReference dbRef, dbRef1;
    String userID;
    TextView viewName, viewName2, viewEmail2, viewAddress, viewGender, viewAge, viewNumber, viewBirth;
    ImageView profilepic, coverpic;
    ProgressDialog progressDialog;

    Button vidBtn;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    Dialog dialog;

    String hisUid;
    String myUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_profile_vc);

        logAuth = FirebaseAuth.getInstance();
        user = logAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child("Trainer");
        dbRef1 = FirebaseDatabase.getInstance().getReference("Users").child("Trainee");
        userID = user.getUid();

        profilepic = findViewById(R.id.profile_pic);
        vidBtn = findViewById(R.id.vidBtn);
        progressDialog = new ProgressDialog(this);

        dialog = new Dialog(this);

        viewName = findViewById(R.id.displayName);
        viewName2 = findViewById(R.id.profile_username);
        viewEmail2 = findViewById(R.id.profile_email);
        viewAddress = findViewById(R.id.displayAddress);
        viewGender = findViewById(R.id.displayGender);
        viewAge = findViewById(R.id.displayAge);
        viewBirth = findViewById(R.id.displayBirth);
        viewNumber = findViewById(R.id.displayNumber);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        vidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TrainerProfileVC.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerProfileVC.this, ViewProfile.class);
                startActivity(intent);
            }
        });

        updateNavHeader();

        Intent intent = getIntent();

        hisUid = intent.getStringExtra("hisUid");

        fbBase = FirebaseDatabase.getInstance();

        Query query = dbRef.orderByChild("uid").equalTo(hisUid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    String  name, username, gender, age, address, birthday, number, email, profile;

                    name = "" +ds.child("name").getValue();
                    username = "" +ds.child("username").getValue();
                    gender= "" +ds.child("gender").getValue();
                    age = "" +ds.child("age").getValue();
                    address = "" +ds.child("address").getValue();
                    number = "+" +ds.child("number").getValue();
                    birthday = "" +ds.child("birthday").getValue();
                    email = "" +ds.child("email").getValue();
                    profile = "" +ds.child("profile").getValue();

                    viewName.setText(name);
                    viewName2.setText(username);
                    viewGender.setText(gender);
                    viewAge.setText(age);
                    viewNumber.setText(number);
                    viewAddress.setText(address);
                    viewBirth.setText(birthday);
                    viewEmail2.setText(email);

                    Glide.with(TrainerProfileVC.this).load(profile).into(profilepic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.navigation_home:
                Intent intent = new Intent(TrainerProfileVC.this, TraineeDashboard.class);
                startActivity(intent);
                break;

            case R.id.navigation_profile:
                Intent intent3 = new Intent(TrainerProfileVC.this, TraineeProfile.class);
                startActivity(intent3);
                break;

            case R.id.navigation_search:
                Intent intent1 = new Intent(TrainerProfileVC.this, TrainerList.class);
                startActivity(intent1);
                break;

            case R.id.navigation_logout:
                Toast.makeText(TrainerProfileVC.this, "Logout Successfully!", Toast.LENGTH_LONG).show();
                logAuth.signOut();
                Intent intent2 = new Intent(TrainerProfileVC.this, Login.class);
                startActivity(intent2);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader(){


        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        ImageView navProfile = headerView.findViewById(R.id.profile_pic);
        TextView navUsername = headerView.findViewById(R.id.profile_username);
        TextView navEmail = headerView.findViewById(R.id.displayEmail);

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerProfileVC.this, TrainerProfile.class);
                startActivity(intent);
            }
        });

        dbRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.fmgymtrainer.Trainer user = snapshot.getValue(Trainer.class);

                if (user != null){
                    String  username, email, profile;

                    username = user.username;
                    email = user.email;
                    profile = user.profile;


                    navUsername.setText(username);
                    navEmail.setText(email);

                    Glide.with(TrainerProfileVC.this).load(profile).into(navProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrainerProfileVC.this, "An error occurred!", Toast.LENGTH_LONG).show();
            }
        });

        dbRef1.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                com.example.fmgymtrainer.Trainee user = snapshot.getValue(Trainee.class);

                if (user != null) {
                    String username, email, profile;

                    username = user.username;
                    email = user.email;
                    profile = user.profile;


                    navUsername.setText(username);
                    navEmail.setText(email);

                    Glide.with(TrainerProfileVC.this).load(profile).into(navProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrainerProfileVC.this, "An error occurred!", Toast.LENGTH_LONG).show();
            }
        });
    }
}