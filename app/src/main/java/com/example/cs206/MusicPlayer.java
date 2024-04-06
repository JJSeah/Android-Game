package com.example.cs206;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private final Context context;
    private Thread thread;

    public MusicPlayer(Context context) {
        this.context = context;
        thread = new Thread(() -> {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_music);
            mediaPlayer.setLooping(true); // Set looping if needed
        });
    }

    public void playMusic() {
        Log.d("MUSIC", "Started thread");
        thread.start();
    }

    public void resumeMusic() {
        if (mediaPlayer != null) {
            Log.d("MUSIC", "Resumed music");
            mediaPlayer.start();
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            Log.d("MUSIC", "Paused music");
            mediaPlayer.pause();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        try {
            thread.join();
            Log.d("MUSIC", "Stopped music and killed thread");
        } catch (InterruptedException e) {
            Log.d("MUSIC", "Error stopping music");
        }
    }
}
