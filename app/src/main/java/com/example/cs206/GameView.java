package com.example.cs206;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;
import android.view.WindowMetrics;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int TIMER_DURATION = 60000;
    private static final int TIMER_INTERVAL = 1000;
    private static final int TEXT_SIZE = 60;

    private Player player;
    private Enemy enemy;
    private Paint paint;
    private Canvas canvas;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private volatile boolean isGameRunning;
    private Bitmap background;
    private Thread gameThread;
    private int height;
    private int width;
    private Context context;

    /**
     * Constructor for the GameView class.
     *
     * @param context The context of the application.
     * @param player The player object.
     * @param enemy The enemy object.
     */
    public GameView(Context context, Player player, Enemy enemy) {
        super(context);
        getHolder().addCallback(this);
        this.context = context;
        this.player = player;
        this.enemy = enemy;
        paint = new Paint();

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        Bitmap originalBackground = BitmapFactory.decodeResource(getResources(), R.drawable.sg_2);
        background = Bitmap.createScaledBitmap(originalBackground, screenWidth,  (int)(screenHeight*0.75), false);

        countDownTimer = new CountDownTimer(TIMER_DURATION, TIMER_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
            }

            public void onFinish() {
                Intent intent = new Intent(context, EndGameActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("endgame", "timesup");
                context.startActivity(intent);
            }
        }.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isGameRunning = true;
        gameThread = new Thread(() -> {
            while (isGameRunning) {
                if (!holder.getSurface().isValid()) {
                    continue;
                }
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {
                    synchronized (holder) {
                        updateCanvas(canvas);
                    }
                    holder.unlockCanvasAndPost(canvas);
                    invalidate();
                }
            }
        });
        gameThread.start();
    }

    private void updateCanvas(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawBitmap(background, 0, 0, null);
        player.draw(canvas, paint);
        enemy.draw(canvas, paint);
        enemy.drawHealthBar(canvas, paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(TEXT_SIZE);
        Paint timeAndHealth = new Paint();
        canvas.drawText("Time left: " + timeLeftInMillis / 1000, 10 , 60, paint);
        canvas.drawText("Player health: " + player.getHealth() + "%", width+500 , 60, paint);
    }

    public long getTimeLeftInMillis() {
        return (timeLeftInMillis / 1000);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        isGameRunning = false;
        countDownTimer.cancel();
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
        // Add code here to stop the game
        surfaceDestroyed(getHolder());
    }
}