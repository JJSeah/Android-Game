package com.example.cs206;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
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
    private Thread gameThread;

    private Context context;


    public GameView(Context context, Player player, Enemy enemy) {
        super(context);
        getHolder().addCallback(this);
        this.context = context;
        this.player = player;
        this.enemy = enemy; // Initialize the enemy here
        paint = new Paint();

        // Get the size of the screen
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        // Load and scale the background to fit the screen
        Bitmap originalBackground = BitmapFactory.decodeResource(getResources(), R.drawable.sg);
        background = Bitmap.createScaledBitmap(originalBackground, screenWidth,  (int)(screenHeight*0.75), false);

        // Initialize the CountDownTimer
        countDownTimer = new CountDownTimer(60000, 1000) { // 60 seconds timer
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
            }

            public void onFinish() {
                //Start the end game activity
//              Intent intent = new Intent(context, EndGameActivity.class);
                Intent intent = new Intent(context, EndGameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Add this line
                context.startActivity(intent);
            }
        }.start(); // Start the timer
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isSurfaceActive = true;
        gameThread = new Thread(() -> {
            while (isSurfaceActive) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    synchronized (holder) {
                        onDraw(canvas);
                    }
                    holder.unlockCanvasAndPost(canvas);
                    invalidate();
                }
            }
        });
        gameThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Clear the canvas
        Paint paint = new Paint();
        canvas.drawBitmap(background, 0, 0, null);

        player.draw(canvas, paint);

        // Draw the enemy
        enemy.draw(canvas, paint);
        enemy.drawHealthBar(canvas, paint);

        // Draw the timer
        paint.setColor(Color.BLACK); // Change this to your preferred color
        paint.setTextSize(50); // Change this to your preferred text size
        canvas.drawText("Time left: " + timeLeftInMillis / 1000, 10, 50, paint);
    }

    public long getTimeLeftInMillis() {
        return (timeLeftInMillis / 1000);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        isSurfaceActive = false;// Set to false when the SurfaceView is destroyed
        countDownTimer.cancel(); // Stop the CountDownTimer
        try {
            gameThread.join();
            System.out.println("Game thread stopped");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        // Add code here to pause the game
    }

    public void resume() {
        // Add code here to resume the game
    }

    public void stop() {
        // Add code here to stop the game}
    }

}