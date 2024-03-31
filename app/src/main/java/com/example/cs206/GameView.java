package com.example.cs206;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private Player player;
    private Enemy enemy;
    private Paint paint;
    private Canvas canvas;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private volatile boolean isSurfaceActive;
    private Bitmap background;




    public GameView(Context context, Player player, Enemy enemy) {
        super(context);
        getHolder().addCallback(this);
        this.player = player;
        this.enemy = enemy; // Initialize the enemy here
        paint = new Paint();

        background = BitmapFactory.decodeResource(getResources(), R.drawable.sg);
        background = Bitmap.createScaledBitmap(background, 1080, 1920, false);

        // Initialize the CountDownTimer
        countDownTimer = new CountDownTimer(60000, 1000) { // 60 seconds timer
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
            }

            public void onFinish() {
                // Start the end game activity
//                Intent intent = new Intent(context, EndGameActivity.class);
                Intent intent = new Intent(context, LeaderboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Add this line
                context.startActivity(intent);
            }
        }.start(); // Start the timer
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isSurfaceActive = true; // Set to true when the SurfaceView is created
        new Thread(() -> {
            while (isSurfaceActive) { // Check this field in your loop
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    synchronized (holder) {
                        onDraw(canvas);
                    }
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Clear the canvas
        Paint paint = new Paint();
        canvas.drawBitmap(background, 0, 0, null);

//        paint.setColor(Color.WHITE); // Change this to your background color
//        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        player.draw(canvas, paint);

        // Draw the enemy
        enemy.update();
        enemy.draw(canvas, paint);
        enemy.drawHealthBar(canvas, paint);

        // Draw the timer
        paint.setColor(Color.BLACK); // Change this to your preferred color
        paint.setTextSize(50); // Change this to your preferred text size
        canvas.drawText("Time left: " + timeLeftInMillis / 1000, 10, 50, paint);

        // invalidate() at the end to redraw the view
        invalidate();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        isSurfaceActive = false; // Set to false when the SurfaceView is destroyed

    }

    public void pause() {
        // Add code here to pause the game
    }

    public void resume() {
        // Add code here to resume the game
    }

    public void stop() {
        // Add code here to stop the game
    }


}