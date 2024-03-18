package com.example.cs206;

import com.example.cs206.JoystickView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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


public class MainActivity extends AppCompatActivity {
    ImageView avatar;
    private Handler handler = new Handler();
    private Button shootButton;
    private int score = 0; // Add this line
    private TextView scoreTextView; // Add this line
    private GameView gameView;
    private Enemy enemy;
    private Player player;
    private float screenWidth;
    private float screenHeight;
    private ProgressBar reloadProgressBar;


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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
                // Update the player's position based on the joystick's movement
                float newXPosition = player.getX() + xPercent * 20; // Increase the multiplier to increase the speed
                float newYPosition = player.getY() - yPercent * 20; // Increase the multiplier to increase the speed

                // Check if the new position is within the screen bounds
                if (newXPosition >= 0 && newXPosition <= findViewById(R.id.main).getWidth() - player.getRadius()) {
                    player.setX(newXPosition);
                }
                if (newYPosition >= 0 && newYPosition <= findViewById(R.id.main).getHeight() - player.getRadius()) {
                    player.setY(newYPosition);
                }

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
                // Update the player's position based on the joystick's movement
                float newXPosition = player.getX() + xPercent * 20; // Increase the multiplier to increase the speed
                float newYPosition = player.getY() - yPercent * 20; // Increase the multiplier to increase the speed

                // Check if the new position is within the screen bounds
                if (newXPosition >= 0 && newXPosition <= findViewById(R.id.main).getWidth() - player.getRadius()) {
                    player.setX(newXPosition);
                }
                if (newYPosition >= 0 && newYPosition <= findViewById(R.id.main).getHeight() - player.getRadius()) {
                    player.setY(newYPosition);
                }

                // Start the update loop
                handler.removeCallbacks(runnable);
                handler.post(runnable);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the update loop when the activity is destroyed
        handler.removeCallbacks(runnable);
    }
}