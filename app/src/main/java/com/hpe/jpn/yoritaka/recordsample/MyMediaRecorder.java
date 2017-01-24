package com.hpe.jpn.yoritaka.recordsample;

import android.media.MediaRecorder;

import com.hpe.jpn.yoritaka.recordsample.utils.Logger;

import java.io.File;

/**
 * Created by YoritakaK on 1/18/2017.
 */

public class MyMediaRecorder {
    private MediaRecorder mediaRecorder;
    private String filePath;
    private boolean recordingFlag;

    public MyMediaRecorder(String recFile) {
        this.filePath = recFile;
    }

    public void startMediaRecord() {
        try {
            File mediaFile = new File(filePath);
            Logger.i("MyMediaRecorder is now starting to record voice to " + filePath);
            if (mediaFile.exists()) {
                Logger.i("MyMediaRecorder found old file. Now deleting.");
                mediaFile.delete();
            }
            mediaFile = null;

            mediaRecorder = new MediaRecorder();
            //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); // DEFAULT => wav
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.prepare();
            mediaRecorder.start();
            Logger.i("MyMediaRecorder started recording.");
        } catch (Exception e) {
            Logger.e("Exception occurred in recording.", e);
        }
        recordingFlag = true;
    }

    public void stopMediaRecord() {
        if (mediaRecorder == null) {
            // Toast.makeText(MainActivity.class., "mediaRecorder is null", Toast.LENGTH_SHORT).show();
        } else {
            try {
                mediaRecorder.stop();
                mediaRecorder.reset();
            } catch (Exception e) {
                Logger.e("Exception occurred in stopping the mediaRecorder.", e);
            }
        }
        recordingFlag = false;
    }

    public boolean isRecording() {
        return recordingFlag;
    }
}
