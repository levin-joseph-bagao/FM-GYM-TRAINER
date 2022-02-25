package com.example.fmgymtrainer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class TraineeEditProfile extends AppCompatActivity {

    FirebaseAuth logAuth;
    FirebaseUser user;
    DatabaseReference dbRef1;
    String userID;

    TextView viewName, viewAddress, viewNumber, viewBirth;
    ImageView profilepics;

    TextView save, cancel, change;

    Uri imageUri;
    String myUri = "";
    StorageTask uploadTask;
    StorageReference storageProfilePicsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_edit_profile);

        profilepics = findViewById(R.id.profile_pic);

        logAuth = FirebaseAuth.getInstance();
        user = logAuth.getCurrentUser();
        dbRef1 = FirebaseDatabase.getInstance().getReference("Users").child("Trainee");
        userID = user.getUid();
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");


        viewName = findViewById(R.id.displayName);
        viewAddress = findViewById(R.id.displayAddress);
        viewBirth = findViewById(R.id.displayBirth);
        viewNumber = findViewById(R.id.displayNumber);

        save = findViewById(R.id.save_btn);
        cancel = findViewById(R.id.cancle_btn);
        change = findViewById(R.id.change_btn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TraineeEditProfile.this, TraineeProfile.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePic();
                Intent intent = new Intent(TraineeEditProfile.this, TraineeProfile.class);
                startActivity(intent);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfile();
            }
        });

        getUserinfo();

        Query query = dbRef1.orderByChild("email");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()){


                    String  name, address, birthday, number, profile;

                    name = "" +ds.child("name").getValue();
                    address = "" +ds.child("address").getValue();
                    number = "+" +ds.child("number").getValue();
                    birthday = "" +ds.child("birthday").getValue();
                    profile = "" +ds.child("profile").getValue();

                    viewName.setText(name);
                    viewNumber.setText(number);
                    viewAddress.setText(address);
                    viewBirth.setText(birthday);


                    try {
                        Picasso.get().load(profile).into(profilepics);
                    }
                    catch (Exception e){
                        Picasso.get().load(R.drawable.ic_baseline_person_24).into(profilepics);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TraineeEditProfile.this, "An error occurred!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void uploadProfilePic() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your Profile Pic");
        progressDialog.setMessage("Please wait, while we are setting your data ");
        progressDialog.show();

        if (imageUri != null)
        {

            final String randomFormat = UUID.randomUUID().toString();
            final StorageReference fileRef = storageProfilePicsRef
                    .child(userID+ randomFormat);

            uploadTask = fileRef.putFile(imageUri);


            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUrl =task.getResult();
                        myUri = downloadUrl.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("profile",myUri);

                        dbRef1.child(userID).updateChildren(userMap);

                        progressDialog.dismiss();

                    }

                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(this, "Profile Picture Update", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserinfo() {

        dbRef1.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    if (dataSnapshot.hasChild("profile")) {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void chooseProfile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode  == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            profilepics.setImageURI(imageUri);
        }
        else {

        }

    }

}