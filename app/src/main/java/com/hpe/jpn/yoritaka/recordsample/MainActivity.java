package com.hpe.jpn.yoritaka.recordsample;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hpe.jpn.yoritaka.recordsample.services.RecorderService;
import com.hpe.jpn.yoritaka.recordsample.utils.Logger;

public class MainActivity extends AppCompatActivity {
    private Button btnRecStartMR;
    private Button btnRecStopMR;
    private Button btnRecStartAR;
    private Button btnRecStopAR;
    private Button btnPlayStart;
    private Button btnPlayStop;

    private ButtonClickListener btnClickListener;

    private RecorderService recorderService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.i("ServiceConnection was connected.");
            recorderService = ((RecorderService.MyBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.i("ServiceConnection was disconnected.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.i("RecordSample is starting..");
        btnRecStartMR = (Button) findViewById(R.id.btnRecStartMR);
        btnRecStopMR = (Button) findViewById(R.id.btnRecStopMR);
        btnRecStartAR = (Button) findViewById(R.id.btnRecStartAR);
        btnRecStopAR = (Button) findViewById(R.id.btnRecStopAR);
        btnPlayStart = (Button) findViewById(R.id.btnPlayStart);
        btnPlayStop = (Button) findViewById(R.id.btnPlayStop);

        btnClickListener = new ButtonClickListener();
        btnRecStartMR.setOnClickListener(btnClickListener);
        btnRecStopMR.setOnClickListener(btnClickListener);
        btnRecStartAR.setOnClickListener(btnClickListener);
        btnRecStopAR.setOnClickListener(btnClickListener);
        btnPlayStart.setOnClickListener(btnClickListener);
        btnPlayStop.setOnClickListener(btnClickListener);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("MainActivity::onResume");
        if (recorderService == null) {
            doBindService();
        }
        Logger.i("Starting service in MainActivity::onResume");
        startService(new Intent(getApplicationContext(), RecorderService.class));
    }

    @Override
    protected void onPause() {
        Logger.i("MainActivity::onPause");
        super.onPause();
        if (recorderService != null) {
            if(!recorderService.isRecording()) {
                Logger.i("call RecorderService::stopSelf()");
                recorderService.stopSelf();
            }
            Logger.i("unbinding service.");
            unbindService(serviceConnection);
            recorderService = null;
        }
    }
    public void doBindService() {
        Logger.i("MainActivity::doBindService()");
        Intent intent = new Intent(this, RecorderService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick (View v) {
            int id = v.getId();
            if (recorderService == null) {
                return;
            }

            if (id == btnRecStartMR.getId()) {
                recorderService.startRecordWithMR();
            } else if (id == btnRecStopMR.getId()){
                recorderService.stopRecordWithMR();
            } else if (id == btnRecStartAR.getId()) {
                recorderService.startRecordWithAR();
            } else if (id == btnRecStopAR.getId()) {
                recorderService.stopRecordWithAR();
            } else if (id == btnPlayStart.getId()) {
                recorderService.startPlay();
            } else if (id ==btnPlayStop.getId()) {
                recorderService.stopPlay();
            }

        }

    }
}
