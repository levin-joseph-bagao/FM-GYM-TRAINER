package com.example.fmgymtrainer;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

public class ViewProfile extends AppCompatActivity {

    FirebaseAuth logAuth;
    FirebaseUser user;
    DatabaseReference dbRef, dbRef1;
    String userID;

    ImageView profilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        profilepic = findViewById(R.id.profile_pic);
        logAuth = FirebaseAuth.getInstance();
        user = logAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child("Trainer");
        dbRef1 = FirebaseDatabase.getInstance().getReference("Users").child("Trainee");
        userID = user.getUid();

        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .build();

        SlidrConfig config_1 = new SlidrConfig.Builder()
                .position(SlidrPosition.BOTTOM)
                .build();

        Slidr.attach(this, config);

        Slidr.attach(this, config_1);
        dbRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Trainer user = snapshot.getValue(Trainer.class);

                if (user != null){
                    String  profile;


                    profile = user.profile;


                    Glide.with(ViewProfile.this).load(profile).into(profilepic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProfile.this, "An error occurred!", Toast.LENGTH_LONG).show();
            }
        });

        dbRef1.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Trainee user = snapshot.getValue(Trainee.class);

                if (user != null){
                    String  profile;


                    profile = user.profile;


                    Glide.with(ViewProfile.this).load(profile).into(profilepic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewProfile.this, "An error occurred!", Toast.LENGTH_LONG).show();
            }
        });

    }
}