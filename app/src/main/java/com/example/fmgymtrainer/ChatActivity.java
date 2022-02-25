package com.example.fmgymtrainer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText sendMessage;
    TextView nameChat, status;
    ImageButton sendBtn;
    Button vidBtn;

    ImageView chatProfile;

    String hisUid;
    String myUid;

    FirebaseAuth logAuth;
    FirebaseDatabase fbBase;
    FirebaseUser user;
    DatabaseReference dbRef, dbRef1;
    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.chatConvo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));


        logAuth = FirebaseAuth.getInstance();
        user = logAuth.getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child("Trainer");
        dbRef1 = FirebaseDatabase.getInstance().getReference("Users").child("Trainee");


        nameChat = findViewById(R.id.hisName);
        chatProfile = findViewById(R.id.chatProfiles);
        status = findViewById(R.id.hisStatus);
        sendMessage = findViewById(R.id.chatEdit);
        sendBtn = findViewById(R.id.sendBtns);

        vidBtn = findViewById(R.id.vidBtn);

        checkUserStatus();

        vidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChatActivity.this, ZoomApp.class);
                startActivity(intent);

            }
        });

        Intent intent = getIntent();

        hisUid = intent.getStringExtra("hisUid");

        fbBase = FirebaseDatabase.getInstance();

        Query query = dbRef.orderByChild("uid").equalTo(hisUid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    String  name, profile;

                    name = "" +ds.child("name").getValue();
                    profile = "" +ds.child("profile").getValue();

                    nameChat.setText(name);

                    Glide.with(ChatActivity.this).load(profile).into(chatProfile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = sendMessage.getText().toString().trim();

                if (TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this, "Cannot send the empty message", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendMessages(message);
                }
            }
        });

    }

    private void sendMessages(String message) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);

        dbRef1.child(userID).child("Chat").push().setValue(hashMap);

        sendMessage.setText("");

    }

    private void checkUserStatus() {

        FirebaseUser user = logAuth.getCurrentUser();

        if (user != null){
            myUid = user.getUid();
        }
        else {
            startActivity(new Intent(getApplicationContext(), com.example.fmgymtrainer.ChatList.class));
        }

    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    public void back(View view) {
        Intent intent = new Intent(ChatActivity.this, com.example.fmgymtrainer.TrainerProfileVC.class);
        startActivity(intent);
    }
}