package com.example.cs206;

import com.example.cs206.JoystickView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {
    ImageView avatar;
    private Handler handler = new Handler();
    private DatabaseHelper dbHelper;
    private Button shootButton;
    private int score = 0; // Add this line
    private TextView scoreTextView; // Add this line
    private GameView gameView;
    private Enemy enemy;
    private Player player;
    private float screenWidth;
    private float screenHeight;
    private ProgressBar reloadProgressBar;

private Handler reloadHandler = new Handler();
private Runnable reloadRunnable = new Runnable() {
    @Override
    public void run() {
        // Update the reload progress bar
        int reloadProgress = player.getReloadProgress();
        reloadProgressBar.setProgress(reloadProgress);

        // Schedule the next update
        reloadHandler.postDelayed(this, 100);
    }
};
private Runnable runnable = new Runnable() {
    @Override
    public void run() {
        // Update the enemy's position
        enemy.update();

        // Move the bullet and check for collisions
        if (!player.getBullets().isEmpty()) {
            Bullet bullet = player.getBullets().get(0);
            bullet.move();
            if (bullet.collidesWith(enemy) || bullet.collidesWithWall(screenWidth, screenHeight)) {
                player.getBullets().remove(bullet);
                if (bullet.collidesWith(enemy)){
                    score++; // Increment the score
                    updateScore(); // Update the score display
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                    // Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        Log.d("Vibration", "Vibration for 500 milliseconds (Oreo and above)");
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                        Log.d("Vibration", "Vibration for 500 milliseconds (below Oreo)");
                    }
                }
            }
        }
        // Check if the enemy's health is 0
        if (enemy.getHealth() <= 0) {
            score++; // Increment the score
            updateScore(); // Update the score display
            // Start the end game activity
            Intent intent = new Intent(MainActivity.this, EndGameActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Redraw the GameView
            gameView.invalidate();

            // Schedule the next update
            handler.postDelayed(this, 100);
        }
        // Update the reload progress bar
        int reloadProgress = player.getReloadProgress();
        reloadProgressBar.setProgress(reloadProgress);
    }
};
    private void updateScore() {
        scoreTextView.setText("Score: " + score);
        dbHelper.insertOrUpdateScore(score); // Save the score to the database
    }
    private void updatePlayerPosition(float direction) {
        // Calculate the new position based on the joystick's direction
        float newXPosition = player.getX() + (float) Math.cos(direction) * 20; // Increase the multiplier to increase the speed
        float newYPosition = player.getY() - (float) Math.sin(direction) * 20; // Increase the multiplier to increase the speed

        // Check if the new position is within the screen bounds
        if (newXPosition - player.getRadius() >= 0 && newXPosition + player.getRadius() <= screenWidth) {
            player.setX(newXPosition);
        }
        if (newYPosition - player.getRadius() >= 0 && newYPosition + player.getRadius() <= screenHeight * 0.75){
            player.setY(newYPosition);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Keep the screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dbHelper = new DatabaseHelper(this); // Initialize the DatabaseHelper

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        reloadProgressBar = findViewById(R.id.reloadProgressBar);
        shootButton = (Button) findViewById(R.id.Y);
        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        updateScore();
        reloadHandler.post(reloadRunnable);

        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the joystick's position
                float joystickX = joystick.getNormalizedX();
                float joystickY = joystick.getNormalizedY();

                // Calculate the direction based on the joystick's position
                float direction = (float) Math.atan2(joystickY - player.getY(), joystickX - player.getX());

                // Shoot in the direction of the joystick
                player.shoot(direction);
            }
        });



        // Initialize the GameView and Enemy instances
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        // Only create a new Enemy if one doesn't already exist
        if (enemy == null) {
            enemy = new Enemy(0, 0, 5, 5, screenWidth, screenHeight);
            EnemyThread enemyThread = new EnemyThread(enemy);
            enemyThread.start();
        }
        player = new Player(screenWidth / 2, screenHeight / 2, 50, screenWidth, screenHeight);
        gameView = new GameView(this, player, enemy);

        // Set the size of the GameView
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) (screenWidth), (int) (screenHeight * 0.75));
        gameView.setLayoutParams(params);

        // Add the GameView to your layout
        ((ViewGroup) findViewById(R.id.main)).addView(gameView);

        // Set the joystick's listener
        joystick.setJoystickListener(new JoystickView.JoystickListener() {
            @Override
            public void onJoystickMoved(float xPercent, float yPercent) {
                // Calculate the direction based on the joystick's movement
                float direction = (float) Math.atan2(yPercent, xPercent);

                // Update the player's position based on the joystick's direction
                updatePlayerPosition(direction);

                // Start the update loop
                handler.removeCallbacks(runnable);
                handler.post(runnable);
            }

            @Override
            public void onJoystickReleased() {
                // Stop the update loop when the joystick is released
                handler.removeCallbacks(runnable);
            }

            @Override
            public void onJoystickHold(float xPercent, float yPercent) {
                // Calculate the direction based on the joystick's movement
                float direction = (float) Math.atan2(yPercent, xPercent);

                // Update the player's position based on the joystick's direction
                updatePlayerPosition(direction);
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the update loop when the activity is destroyed
        handler.removeCallbacks(runnable);
        reloadHandler.removeCallbacks(reloadRunnable);

    }
}