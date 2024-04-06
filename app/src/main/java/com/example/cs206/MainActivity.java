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

import java.util.Iterator;
import android.media.MediaPlayer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity {
    ImageView avatar;
    private Handler handler = new Handler();
    private DatabaseHelper dbHelper;
    private Button shootButton;
    private int score = 0; // Add this line
    private TextView scoreTextView; // Add this line
    private GameView gameView;
    private MusicPlayer musicPlayer;
    private Enemy enemy;
    private Player player;
    private float screenWidth;
    private float screenHeight;
    private ProgressBar reloadProgressBar;
    private MediaPlayer mediaPlayer;

    // Create a ScheduledExecutorService with a fixed thread pool
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    private boolean isActivityVisible;
    private boolean isCollisionRunnableRunning = false;
    private boolean isSurfaceViewActive = false;
    private EnemyThread enemyThread; // Make enemyThread a member variable



    private Runnable bulletCollisionRunnable = new Runnable() {
        @Override
        public void run() {
            // Use an Iterator to safely remove items during iteration
            Iterator<Bullet> iterator = player.getBullets().iterator();
            while (iterator.hasNext()) {
                Bullet bullet = iterator.next();
                bullet.move();
                boolean collidesWithEnemy = bullet.collidesWith(enemy);
                if (collidesWithEnemy || bullet.collidesWithWall(screenWidth, screenHeight)) {
                    iterator.remove(); // Safe removal during iteration
                    if (collidesWithEnemy){
                        score++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateScore();
                            }
                        });
                        vibrate();
                    }
                }
            }
            handler.postDelayed(this, 100);
        }
    };


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
    private Runnable collisionRunnable = new Runnable() {
        @Override
        public void run() {
            // Check for collision between player and enemy
            if (isColliding(player, enemy)) {
                Log.d("COLLISION", "Collision detected");
                player.takeDamage(10); // Assume the player takes 10 damage when colliding with an enemy
                if (player.getHealth() <= 0) {
                    enemyThread.interrupt();
                    didNotWin();
                }
            }
            isCollisionRunnableRunning = false;
            // Check if the enemy's health is 0
            if (enemy.getHealth() <= 0) {
                updateScore(); // Update the score display=
                // Player is dead, handle game over
                handleGameOver();
                dbHelper.insertDatabase(gameView.getTimeLeftInMillis(),score);
            } else {
                // Redraw the GameView
                gameView.invalidate();

                // Schedule the next update
                handler.postDelayed(this, 100);
            }
        }
    };
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isCollisionRunnableRunning) {
                isCollisionRunnableRunning = true;
                new Thread(collisionRunnable).start();
            }

            // Move the bullet and check for collisions
            if (!player.getBullets().isEmpty()) {
                // Submit the bulletCollisionRunnable to the executorService
                executorService.submit(bulletCollisionRunnable);
            }

        }
    };

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

        // Initialize the MusicPlayer to play music
        musicPlayer = new MusicPlayer(this);
        musicPlayer.playMusic();

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

        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Shoot a bullet when the shoot button is clicked
                float direction = (float) -Math.PI / 2; // -90 degrees in radians

                // Shoot in the direction
                player.shoot(direction);

                // Call reloadRunnable after shooting a bullet
                reloadHandler.removeCallbacks(reloadRunnable);
                reloadHandler.post(reloadRunnable);
            }
        });

        // Initialize the GameView and Enemy instances
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        // Only create a new Enemy if one doesn't already exist
        if (enemy == null) {
            System.out.println("Creating new enemy");
            enemy = new Enemy(0, 0, 5, 5, screenWidth, screenHeight, getResources());
            enemyThread = new EnemyThread(enemy);
            enemyThread.start();
        }
        player = new Player(screenWidth / 2, screenHeight / 2, 50, screenWidth, screenHeight, getResources());

        // Now that the Player object is initialized, you can call updateHealth
        player.updateHealth();


        gameView = new GameView(this, player, enemy);
        isSurfaceViewActive = true;

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


                if (isActivityVisible) {
                    // Update the player's position based on the joystick's direction
                    updatePlayerPosition(direction);
                    handler.removeCallbacks(runnable);
                    handler.post(runnable);
                }
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
                // Start the update loop only if the activity is visible
                if (isActivityVisible) {
                    handler.removeCallbacks(runnable);
                    handler.post(runnable);
                }
            }
        });
    }

    private void didNotWin() {

        Log.d("Collision", "Player died before:");
        if (handler != null) {
            handler.removeCallbacks(collisionRunnable);
            handler.removeCallbacks(runnable);
            handler.removeCallbacks(bulletCollisionRunnable);
        }

        if (reloadHandler != null) {
            reloadHandler.removeCallbacks(reloadRunnable);
        }

        // Shutdown the executorService
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }

        // Stop the music
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        if (isSurfaceViewActive) {
            gameView.surfaceDestroyed(gameView.getHolder());
            isSurfaceViewActive = false;
        }

        Intent intent = new Intent(MainActivity.this, EndGameActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Add this line
        startActivity(intent);
        finish();
    }

    private void handleGameOver() {
        // Stop the update loop
        if (handler != null) {
            handler.removeCallbacks(collisionRunnable);
            handler.removeCallbacks(runnable);
            handler.removeCallbacks(bulletCollisionRunnable);
        }

        if (reloadHandler != null) {
            reloadHandler.removeCallbacks(reloadRunnable);
        }

        // Shutdown the executorService
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }

        // Stop the music
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Start the end game activity
        startLeaderboardActivity();
    }

    private void startLeaderboardActivity() {
        Log.d("MainActivity", "Starting LeaderboardActivity");
        Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // remove back stack
        startActivity(intent);
        finish();
    }
    private void updateScore() {
        scoreTextView.setText("Score: " + score);
    }
    private void updatePlayerPosition(float direction) {
        // Calculate the new position based on the joystick's direction
        float newXPosition = player.getX() + (float) Math.cos(direction) * 2; // Increase the multiplier to increase the speed
        float newYPosition = player.getY() - (float) Math.sin(direction) * 2; // Increase the multiplier to increase the speed

        // Check if the new position is within the screen bounds
        if (newXPosition - player.getRadius() >= 0 && newXPosition + player.getRadius() <= screenWidth * 0.99) {
            player.setX(newXPosition);
        }
        if (newYPosition - player.getRadius() >= 0 && newYPosition + player.getRadius() <= screenHeight * 0.75){
            player.setY(newYPosition);
        }
    }
    private boolean isColliding(Player player, Enemy enemy) {
        // Implement your collision detection logic here
        // This is a simple example using circular collision detection
        float distance = (float) Math.sqrt(Math.pow(player.getX() - enemy.getX(), 2) + Math.pow(player.getY() - enemy.getY(), 2));

        return distance-50 < (player.getRadius() + enemy.getRadius());
    }
    private void vibrate() {
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
    @Override
    protected void onResume() {
        super.onResume();
        // Start the game thread, music, and executor service here
        musicPlayer.resumeMusic();
        isActivityVisible = true;
        // Start the executorService
        executorService = Executors.newScheduledThreadPool(5);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityVisible = false;

        // Stop the music when the activity is paused
        musicPlayer.pauseMusic();

        // Stop the update loop when the activity is paused

        handler.removeCallbacks(runnable);
        handler.removeCallbacks(bulletCollisionRunnable);
        handler.removeCallbacks(collisionRunnable);
        reloadHandler.removeCallbacks(reloadRunnable);
        executorService.shutdownNow();
        if (enemyThread != null) {
            enemyThread.stopThread();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        // Stop the update loop when the activity is stopped
        handler.removeCallbacks(runnable);
        reloadHandler.removeCallbacks(reloadRunnable);

        // Shutdown the executorService when the activity is stopped
        executorService.shutdownNow();
        if (enemyThread != null) {
            enemyThread.stopThread();
        }

    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "onDestroy started");

        super.onDestroy();

        // Stop the music when the activity is destroyed
        musicPlayer.stopMusic();

        // Stop the update loop when the activity is destroyed
        handler.removeCallbacks(runnable);
        handler.removeCallbacks(bulletCollisionRunnable);
        handler.removeCallbacks(collisionRunnable);
        reloadHandler.removeCallbacks(reloadRunnable);
        if (enemyThread != null) {
            enemyThread.stopThread();
        }

        // Shutdown the executorService when the activity is destroyed
        executorService.shutdown();
        Log.d("MainActivity", "onDestroy ended");

    }
}