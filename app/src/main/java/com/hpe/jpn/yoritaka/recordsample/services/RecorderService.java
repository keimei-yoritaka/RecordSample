package com.hpe.jpn.yoritaka.recordsample.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.hpe.jpn.yoritaka.recordsample.MyAudioRecord;
import com.hpe.jpn.yoritaka.recordsample.MyMediaRecorder;
import com.hpe.jpn.yoritaka.recordsample.utils.Logger;

import java.io.File;

/**
 * Created by YoritakaK on 1/24/2017.
 */

public class RecorderService extends Service {
    private final IBinder recBinder = new MyBinder();
    private MediaPlayer mediaPlayer;
    private MyMediaRecorder myMediaRecorder;
    private MyAudioRecord myAudioRecord;

    private String fileName;

    public RecorderService(){
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.i("RecorderService::onBind");
        return recBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.i("RecorderService::onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Logger.i("RecorderService::onDestroy");
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fileName = getExternalFilesDir(null).toString() + "/test.wav";
        Logger.i("RecorderService::onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.i("RecorderService:onStartCommand.");
        return START_NOT_STICKY;
        //return START_NOT_STICKY;
    }

    public class MyBinder extends Binder {
        public RecorderService getService() {
            return RecorderService.this;
        }
    }

    public boolean isRecording() {
        return (mediaPlayer!= null && mediaPlayer.isPlaying()) ? true : false;
    }

    public void startPlay() {
        Logger.i("RecorderService::startPlay");
        //mediaPlayer = MediaPlayer.create(this, R.raw.bensound_clearday);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(100, 100);
            mediaPlayer.prepare();
        } catch (Exception e) {
            Logger.e("Filed in MP.setDataSource.", e);
        }
        mediaPlayer.start();
    }

    public void stopPlay() {
        Logger.i("RecorderService::stopPlay");
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void startRecordWithMR() {
        Logger.i("RecorderService::startRecordWithMR");

        if (myMediaRecorder == null) {
            myMediaRecorder = new MyMediaRecorder(fileName);
            myMediaRecorder.startMediaRecord();
        }
    }

    public void stopRecordWithMR() {
        Logger.i("RecorderService::stopRecordWithMR");
        if (myMediaRecorder != null) {
            myMediaRecorder.stopMediaRecord();
        }
        myMediaRecorder = null;
    }

    public void startRecordWithAR() {
        Logger.i("RecorderService::startRecordWithAR");
        if (myAudioRecord == null) {
            myAudioRecord = new MyAudioRecord(fileName);
            myAudioRecord.initAudioRecord();
            myAudioRecord.startAudioRecord();
        }
    }

    public void stopRecordWithAR() {
        Logger.i("RecorderService::stopRecordWithAR");
        if (myAudioRecord != null) {
            myAudioRecord.stopAudioRecord();
        }
        myAudioRecord = null;
    }
}
