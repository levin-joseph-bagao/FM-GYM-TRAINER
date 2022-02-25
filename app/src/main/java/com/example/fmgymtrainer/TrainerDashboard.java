package com.example.fmgymtrainer;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TrainerDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth logAuth;
    FirebaseUser user;
    DatabaseReference dbRef;
    String userID;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_dashboard);

        logAuth = FirebaseAuth.getInstance();
        user = logAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child("Trainer");
        userID = user.getUid();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);


        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigation_home);


        updateNavHeader();

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


    public void updateNavHeader(){


        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        ImageView navProfile = headerView.findViewById(R.id.profile_pic);
        TextView navUsername = headerView.findViewById(R.id.profile_username);
        TextView navEmail = headerView.findViewById(R.id.displayEmail);

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrainerDashboard.this, TrainerProfile.class);
                startActivity(intent);
            }
        });

        Query query = dbRef.orderByChild("email");
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

                    Glide.with(TrainerDashboard.this).load(profile).into(navProfile);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TrainerDashboard.this, "An error occurred!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_home:
                Intent intent4 = new Intent(TrainerDashboard.this, TrainerDashboard.class);
                startActivity(intent4);
                break;

            case R.id.navigation_profile:
                Intent intent = new Intent(TrainerDashboard.this, TrainerProfile.class);
                startActivity(intent);
                break;

            case R.id.navigation_search:
                Intent intent1 = new Intent(TrainerDashboard.this, UserList.class);
                startActivity(intent1);
                break;

            case R.id.navigation_logout:
                Toast.makeText(TrainerDashboard.this, "Logout Successfully!", Toast.LENGTH_LONG).show();
                logAuth.signOut();
                Intent intent2 = new Intent(TrainerDashboard.this, Login.class);
                startActivity(intent2);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}