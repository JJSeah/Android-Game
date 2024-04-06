package com.example.cs206;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private final Context context;
    private Thread thread;
    private boolean isPlaying = false; // Add this line

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
        isPlaying = true; // Add this line
    }

    public void resumeMusic() {
        if (mediaPlayer != null) {
            Log.d("MUSIC", "Resumed music");
            mediaPlayer.start();
            isPlaying = true; // Add this line
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            Log.d("MUSIC", "Paused music");
            mediaPlayer.pause();
            isPlaying = false; // Add this line
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null && isPlaying) { // Modify this line
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false; // Add this line
        }

        try {
            thread.join();
            Log.d("MUSIC", "Stopped music and killed thread");
        } catch (InterruptedException e) {
            Log.d("MUSIC", "Error stopping music");
        }
    }
}