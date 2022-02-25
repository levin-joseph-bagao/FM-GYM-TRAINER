package com.example.fmgymtrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
    }

    public void reg(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void log(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}