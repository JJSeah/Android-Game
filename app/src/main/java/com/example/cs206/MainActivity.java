package com.example.cs206;

import com.example.cs206.JoystickView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    ImageView avatar;
    private Handler handler = new Handler();
    private int score = 0; // Add this line
    private TextView scoreTextView; // Add this line
    private GameView gameView;
    private Enemy enemy;
    private Player player;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Update the enemy's position
            enemy.update();

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


        scoreTextView = (TextView) findViewById(R.id.scoreTextView); // Add this line
        updateScore(); // Add this line

        // Initialize the GameView and Enemy instances
        float screenWidth = getResources().getDisplayMetrics().widthPixels;
        float screenHeight = getResources().getDisplayMetrics().heightPixels;
        // Only create a new Enemy if one doesn't already exist
        if (enemy == null) {
            enemy = new Enemy(0, 0, 5, 5, screenWidth, screenHeight);
        }
        player = new Player(screenWidth / 2, screenHeight / 2, 50);
        gameView = new GameView(this, player, enemy);

        // Set the size of the GameView
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) (screenWidth), (int) (screenHeight * 0.75));
        gameView.setLayoutParams(params);

        // Add the GameView to your layout
        ((ViewGroup) findViewById(R.id.main)).addView(gameView);

        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView);
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