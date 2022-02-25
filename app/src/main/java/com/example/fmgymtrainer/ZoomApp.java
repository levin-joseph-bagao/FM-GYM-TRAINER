package com.example.fmgymtrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;

public class ZoomApp extends AppCompatActivity {

    EditText editName, editNumber, editPassword;
    Button join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_app);

        initializeZoom(this);

        editName = findViewById(R.id.etname);
        editNumber = findViewById(R.id.etmnumber);
        editPassword = findViewById(R.id.etmpassword);

        join = findViewById(R.id.joinBtn);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String MeetingNumber = editNumber.getText().toString();
                String MeetingPassword = editPassword.getText().toString();
                String UserName = editName.getText().toString();

                if (MeetingNumber.trim().length() > 0 && MeetingPassword.trim().length() > 0 && UserName.trim().length() > 0){
                    joinMeeting(ZoomApp.this, MeetingNumber, MeetingPassword, UserName);
                }

            }
        });
    }

    private void initializeZoom(Context context) {
        ZoomSDK sdk = ZoomSDK.getInstance();
        ZoomSDKInitParams params = new ZoomSDKInitParams();
        params.appKey = "j8iOoVzQ5S8Kk5pUix9JIhuflMI59NkGbY4v";
        params.appSecret = "Xx1ZSwo8aqRfnJMwvhX0XezI06Jc8MH5lLrU";
        params.domain = "zoom.us";
        params.enableLog = true;

        ZoomSDKInitializeListener listener = new ZoomSDKInitializeListener() {
            @Override
            public void onZoomSDKInitializeResult(int i, int i1) {

            }

            @Override
            public void onZoomAuthIdentityExpired() {

            }
        };
        sdk.initialize(context, listener, params);
    }

    private void joinMeeting(Context context, String meetingNumber, String meetingPassword, String userName){
        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
        JoinMeetingOptions options = new JoinMeetingOptions();
        JoinMeetingParams params = new JoinMeetingParams();
        params.displayName = userName;
        params.meetingNo = meetingNumber;
        params.password = meetingPassword;

        meetingService.joinMeetingWithParams(context,params,options);
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), TrainerProfileVC.class));
    }
}