package com.example.fmgymtrainer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TraineeProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth logAuth;
    FirebaseUser user;
    DatabaseReference dbRef1;
    String userID;
    TextView viewName, viewName2, viewEmail2, viewAddress, viewGender, viewAge, viewNumber, viewBirth;
    ImageView profilepic, coverpic;
    FloatingActionButton fabs;
    ProgressDialog progressDialog;

    Dialog dialog;



    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_profile);

        logAuth = FirebaseAuth.getInstance();
        user = logAuth.getCurrentUser();
        dbRef1 = FirebaseDatabase.getInstance().getReference("Users").child("Trainee");
        userID = user.getUid();

        profilepic = findViewById(R.id.profile_pic);
        fabs = findViewById(R.id.fabtn);
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigation_home);


        updateNavHeader();

        Query query = dbRef1.orderByChild("email");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){

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

                    Glide.with(TraineeProfile.this).load(profile).into(profilepic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TraineeProfile.this, "An error occurred!", Toast.LENGTH_LONG).show();
            }
        });

        fabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomAlertDialog();

            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(TraineeProfile.this, ViewProfile.class);
                startActivity(intent);
            }
        });

        updateNavHeader();
    }

    private void bottomAlertDialog() {

        BottomSheetDialog sheetDialog = new BottomSheetDialog(TraineeProfile.this, R.style.BottomSheetStyle);

        View sheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.bottom_alert_dialog, findViewById(R.id.dialog_container));

        sheetView.findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
            }
        });

        sheetView.findViewById(R.id.editProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
                Intent intent = new Intent(TraineeProfile.this, TraineeEditProfile.class);
                startActivity(intent);
            }
        });

        sheetView.findViewById(R.id.viewProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
                Intent intent = new Intent(TraineeProfile.this, ViewProfile.class);
                startActivity(intent);
            }
        });

        sheetDialog.setContentView(sheetView);
        sheetDialog.show();
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
                Intent intent4 = new Intent(TraineeProfile.this, com.example.fmgymtrainer.TraineeDashboard.class);
                startActivity(intent4);
                break;

            case R.id.navigation_profile:
                Intent intent = new Intent(TraineeProfile.this, TraineeProfile.class);
                startActivity(intent);
                break;


            case R.id.navigation_search:
                Intent intent1 = new Intent(TraineeProfile.this, com.example.fmgymtrainer.TrainerList.class);
                startActivity(intent1);
                break;

            case R.id.navigation_logout:
                Toast.makeText(TraineeProfile.this, "Logout Successfully!", Toast.LENGTH_LONG).show();
                logAuth.signOut();
                Intent intent2 = new Intent(TraineeProfile.this, Login.class);
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
                Intent intent = new Intent(TraineeProfile.this, TraineeProfile.class);
                startActivity(intent);
            }
        });

        Query query = dbRef1.orderByChild("email");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){

                    String  username, email, profile;

                    username = "" +ds.child("username").getValue();
                    email = "" +ds.child("email").getValue();
                    profile = "" +ds.child("profile").getValue();


                    navUsername.setText(username);
                    navEmail.setText(email);

                    Glide.with(TraineeProfile.this).load(profile).into(navProfile);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TraineeProfile.this, "An error occurred!", Toast.LENGTH_LONG).show();
            }
        });
    }
}