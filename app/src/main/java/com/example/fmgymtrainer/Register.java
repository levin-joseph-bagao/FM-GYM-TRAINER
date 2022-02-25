package com.example.fmgymtrainer;

import static java.util.Calendar.DAY_OF_MONTH;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextView viewAge, viewProfile;
    TextInputLayout editName, editUsername, editAddress, editBirth, editNumber, editEmail, editProfile, editCover, editPassword, editconfPassword;
    TextInputEditText editBirthday;
    Button viewdate;
    RadioButton btnMale, btnFemale, btnTrainer, btnTrainee;
    DatabaseReference dbRef, dbRef1;
    String gender, position =" ";
    ProgressDialog progressDialog;
    DatePickerDialog.OnDateSetListener setListener;
    FirebaseAuth logAuth;
    FirebaseFirestore fstrore;

    ScrollView scrollView;

    StorageReference storageProfilePicsRef;

    private Boolean validateName(){
        String val = editName.getEditText().getText().toString();
        String numerror = "[0-9]+@[#-@]";

        if (val.isEmpty()) {
            editName.setError("Field cannot be empty");
            return false;
        }
        else if (val.matches(numerror)) {
            editName.setError("Number is not allowed");
            return false;
        }
        else {
            editName.setError(null);
            editName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = editUsername.getEditText().getText().toString();
        String whitespc = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            editUsername.setError("Field cannot be empty");
            return false;
        }
        else if (val.length()< 4 || val.length()>13) {
            editUsername.setError("Username is too long");
            return false;
        }
        else if (!val.matches(whitespc)) {
            editUsername.setError("White space is not allowed");
            return false;
        }
        else {
            editUsername.setError(null);
            return true;
        }
    }

    private Boolean validateAddress(){
        String val = editAddress.getEditText().getText().toString();

        if (val.isEmpty()) {
            editAddress.setError("Field cannot be empty");
            return false;
        }
        else {
            editAddress.setError(null);
            return true;
        }
    }

    private Boolean validateBirthday(){
        String val = editBirth.getEditText().getText().toString();

        if (val.isEmpty()) {
            editBirth.setError("Field cannot be empty");
            return false;
        }
        else {
            editBirth.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo(){
        String val = editNumber.getEditText().getText().toString();

        if (val.isEmpty()) {
            editNumber.setError("Field cannot be empty");
            return false;
        }
        else if (val.length()< 10 || val.length()>13) {
            editNumber.setError("Phone No. is Invalid");
            return false;
        }
        else {
            editNumber.setError(null);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = editEmail.getEditText().getText().toString();
        String emailpatt = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            editEmail.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(emailpatt)) {
            editEmail.setError("Invalid Email Address");
            return false;
        }
        else {
            editEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String password = editPassword.getEditText().getText().toString();
        String val = editconfPassword.getEditText().getText().toString();
        //String passvalid = "^"+
        //"(?=.*[a-zA-Z])"+
        //"(?=.*[@#$%^&+=])"+
        //"(?=\\S+$)"+
        //".{4,}"+
        //"$";

        if (password.isEmpty()) {
            editPassword.setError("Field cannot be empty");
            return false;
        }
        else if (password.length()< 8) {
            editNumber.setError("Password is too short");
            return false;
        }
        else if (!password.equals(val)) {
            editconfPassword.setError("Wrong Password");
            return false;
        }
        else {
            editPassword.setError(null);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewAge = findViewById(R.id.viewAge);
        viewdate = findViewById(R.id.viewdate);
        viewProfile = findViewById(R.id.viewProfile);
        editName = findViewById(R.id.RegName);
        editUsername = findViewById(R.id.RegUsername);
        btnMale = findViewById(R.id.BtnMale);
        btnFemale = findViewById(R.id.BtnFemale);
        editAddress = findViewById(R.id.RegAddress);
        editBirthday = findViewById(R.id.RegBirth);
        editBirth = findViewById(R.id.RegBirthView);
        btnTrainer = findViewById(R.id.BtnTrainer);
        btnTrainee = findViewById(R.id.BtnTrainee);
        editNumber = findViewById(R.id.RegNumber);
        editEmail = findViewById(R.id.RegEmail);
        editPassword = findViewById(R.id.RegPassword);
        editconfPassword = findViewById(R.id.RegconfPassword);
        progressDialog = new ProgressDialog(this);

        fstrore = FirebaseFirestore.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");
        dbRef1 = FirebaseDatabase.getInstance().getReference("Users");
        logAuth = FirebaseAuth.getInstance();
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("TraineeProfile Pic");
        scrollView = findViewById(R.id.scrollView);

        viewdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(viewdate.getContext(), setListener,year,month,day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                String format = new SimpleDateFormat("MMM dd, yyyy").format(c.getTime());
                editBirthday.setText(format);
                viewAge.setText(Integer.toString(calculateAge(c.getTimeInMillis())));
            }
        };
    }

    int calculateAge(long date){
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_MONTH) < dob.get(DAY_OF_MONTH)){
            age--;
        }
        return age;
    }

    public void Register(View view) {
        PerformAuth();
    }

    private void PerformAuth() {

        String name, username, age, address, birthday, number, email, password, confirmPassword, profile, uid;

        name = editName.getEditText().getText().toString().trim();
        username = editUsername.getEditText().getText().toString().trim();
        age = viewAge.getText().toString().trim();
        address = editAddress.getEditText().getText().toString().trim();
        birthday = editBirthday.getText().toString().trim();
        number = editNumber.getEditText().getText().toString().trim();
        email = editEmail.getEditText().getText().toString().trim();
        password = editPassword.getEditText().getText().toString().trim();
        confirmPassword = editconfPassword.getEditText().getText().toString().trim();
        profile = viewProfile.getText().toString().trim();
        uid = logAuth.getUid();

        if (btnMale.isChecked()) {
            gender = "Male";
        } else if (btnFemale.isChecked()) {
            gender = "Female";
        } else {

        }

        if (btnTrainer.isChecked()) {
            position = "Trainer";
        } else if (btnTrainee.isChecked()) {
            position = "Trainee";
        } else {


        }

        if (!validateName() | !validateAddress() | !validateBirthday() | !validatePhoneNo() | !validateEmail() | !validatePassword()){
            return;
        }
        else {
            progressDialog.setMessage("Please wait");
            progressDialog.setTitle("Registration in process");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            logAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (btnTrainer.isChecked()) {
                                    Trainer users = new Trainer(name, username, age, gender, address, birthday, position, number, email ,profile, uid);

                                    FirebaseUser user = logAuth.getCurrentUser();
                                    Snackbar.make(scrollView, "Registration is Successful!", Snackbar.LENGTH_LONG).show();
                                    DocumentReference docRef = fstrore.collection("Users").document(user.getUid());
                                    Map<String,Object> userinfo = new HashMap<>();
                                    userinfo.put("Fullname", name);
                                    userinfo.put("Username", username);
                                    userinfo.put("Age", age);
                                    userinfo.put("Address", address);
                                    userinfo.put("Birthday", birthday);
                                    userinfo.put("Gender", gender);
                                    userinfo.put("Phone", number);
                                    userinfo.put("Email Address", email);
                                    userinfo.put("Profile", profile);

                                    userinfo.put("Trainer", " ");

                                    dbRef.child("Trainer").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });

                                    docRef.set(userinfo);
                                    startActivity(new Intent(getApplicationContext(), Login.class));

                                } else if (btnTrainee.isChecked()) {

                                    Trainee users = new Trainee(name, username, age, gender, address, birthday, position, number, email ,profile, uid);

                                    FirebaseUser user = logAuth.getCurrentUser();

                                    Snackbar.make(scrollView, "Registration is Successful!", Snackbar.LENGTH_LONG).show();
                                    DocumentReference docRef = fstrore.collection("Users").document(user.getUid());
                                    Map<String,Object> userinfo = new HashMap<>();
                                    userinfo.put("Fullname", name);
                                    userinfo.put("Username", username);
                                    userinfo.put("Age", age);
                                    userinfo.put("Address", address);
                                    userinfo.put("Birthday", birthday);
                                    userinfo.put("Gender", gender);
                                    userinfo.put("Phone", number);
                                    userinfo.put("Email Address", email);
                                    userinfo.put("Profile", profile);

                                    userinfo.put("Trainee","");


                                    dbRef1.child("Trainee")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                }
                                            });
                                    docRef.set(userinfo);
                                    startActivity(new Intent(getApplicationContext(), Login.class));
                                }
                            } else {
                                progressDialog.cancel();
                                Snackbar.make(scrollView, "Email Address already exist", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void log(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
    }
}