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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth logAuth;
    FirebaseUser users;
    DatabaseReference dbRef, dbRef1;
    String userID;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    MaterialToolbar toolbar;

    List<TraineeListName> names2;
    List<TrainerListName> names;

    TrainerAdapter adapter;

    TraineeAdapter adapters;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.listUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserList.this));

        names = new ArrayList<>();
        names2 = new ArrayList<>();

        logAuth = FirebaseAuth.getInstance();
        users = logAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child("Trainer");
        dbRef1 = FirebaseDatabase.getInstance().getReference("Users").child("Trainee");
        userID = users.getUid();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigation_home);


        updateNavHeader();


        setUpRecyclerView();

    }

    private void updateNavHeader() {

        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        ImageView navProfile = headerView.findViewById(R.id.profile_pic);
        TextView navUsername = headerView.findViewById(R.id.profile_username);
        TextView navEmail = headerView.findViewById(R.id.displayEmail);

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    Glide.with(UserList.this).load(profile).into(navProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserList.this, "An error occurred!", Toast.LENGTH_LONG).show();
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

                    Glide.with(UserList.this).load(profile).into(navProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserList.this, "An error occurred!", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void setUpRecyclerView() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                names.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    TrainerListName user = ds.getValue(TrainerListName.class);

                    if (!user.getName().equals(users.getDisplayName())) {
                        names.add(user);
                    }

                    adapter = new TrainerAdapter(UserList.this, names);

                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                names2.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    TraineeListName user = ds.getValue(TraineeListName.class);

                    if (!user.getName().equals(users.getDisplayName())) {
                        names2.add(user);
                    }

                    adapters = new TraineeAdapter(UserList.this, names2);

                    recyclerView.setAdapter(adapters);

                    adapters.notifyDataSetChanged();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent intent2 = new Intent(UserList.this, TrainerDashboard.class);
                startActivity(intent2);
                break;

            case R.id.navigation_profile:
                Intent intent = new Intent(UserList.this, TrainerProfile.class);
                startActivity(intent);
                break;

            case R.id.navigation_search:
                break;

            case R.id.navigation_logout:
                Toast.makeText(UserList.this, "Logout Successfully!", Toast.LENGTH_LONG).show();
                logAuth.signOut();
                Intent intent1 = new Intent(UserList.this, Login.class);
                startActivity(intent1);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}