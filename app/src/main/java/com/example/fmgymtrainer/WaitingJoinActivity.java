package com.example.fmgymtrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import us.zoom.androidlib.utils.ZmMimeTypeUtils;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.ZoomSDK;

public class WaitingJoinActivity extends AppCompatActivity implements MeetingServiceListener, View.OnClickListener {

    TextView MeetingTopic, MeetingDate, MeetingTime, MeetingID;
    Button LeaveMeeting;

    String topic, date, time;
    long meetingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_join);

        Intent intent = getIntent();

        topic = intent.getStringExtra(ZmMimeTypeUtils.EXTRA_TOPIC);
        date = intent.getStringExtra(ZmMimeTypeUtils.EXTRA_DATE);
        time = intent.getStringExtra(ZmMimeTypeUtils.EXTRA_TIME);

        meetingId = intent.getLongExtra(ZmMimeTypeUtils.EXTRA_MEETING_ID, 0);

        MeetingTopic = findViewById(R.id.tvmeetingtopic);
        MeetingDate = findViewById(R.id.tvmeetingdate);
        MeetingTime = findViewById(R.id.tvmeetingtime);
        MeetingID = findViewById(R.id.tvmeetingid);

        LeaveMeeting = findViewById(R.id.leaveBtn);

        if (topic != null){
            MeetingTopic.setText("Meeting Topic: " +topic);
        }
        if (date != null){
            MeetingDate.setText("Meeting Date: " +date);
        }
        if (time != null){
            MeetingTime.setText("Meeting Topic: " +time);
        }
        if (meetingId > 0){
            MeetingID.setText("Meeting Topic: " +meetingId);
        }

        LeaveMeeting.setOnClickListener(this);

        ZoomSDK zoomSdk = ZoomSDK.getInstance();
        MeetingService service = zoomSdk.getMeetingService();

        if (service != null){
            service.addListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.leaveBtn);{
            OnClick();
        }
    }

    @Override
    protected void onDestroy() {
        ZoomSDK sdk = ZoomSDK.getInstance();

        if (sdk.isInitialized()){
            MeetingService service = sdk.getMeetingService();

            service.removeListener(this);
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int i, int i1) {
        if (meetingStatus != MeetingStatus.MEETING_STATUS_WAITINGFORHOST){
            finish();
        }
    }

    public void OnClick(){
        ZoomSDK zoomSdk = ZoomSDK.getInstance();
        MeetingService service = zoomSdk.getMeetingService();

        if (service != null){
            service.leaveCurrentMeeting(false);
        }
        finish();
    }
}