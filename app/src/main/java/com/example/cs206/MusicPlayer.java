package com.example.cs206;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {

    private MediaPlayer mediaPlayer;
    private final Context context;

    public MusicPlayer(Context context) {
        this.context = context;
    }

    public void playMusic() {
        final Thread thread = new Thread(() -> {
            mediaPlayer = MediaPlayer.create(context, R.raw.game_music);
            mediaPlayer.setLooping(true); // Set looping if needed
            mediaPlayer.start();
        });

        thread.start();
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
