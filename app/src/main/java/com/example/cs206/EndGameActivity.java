package com.example.cs206;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the EndGameActivity class for the application.
 * It represents the end game screen that is displayed when the game ends.
 */
public class EndGameActivity extends AppCompatActivity {
    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        EdgeToEdge.enable(this);

        String description = getIntent().getExtras().getString("endgame");
        String gameLostMessage = getString(R.string.game_lost_died);

        if (description.equals("died")) {
            TextView textView = (TextView) findViewById(R.id.lost_description);
            textView.setText(gameLostMessage);
        }

        Button restartButton = (Button) findViewById(R.id.restartButton);
        Button menuButton = (Button) findViewById(R.id.menuButton);
        /**
         * This method is used to start a new activity.
         * @param cls The class of the activity to start.
         */
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndGameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /**
         * This method is used to start a new activity.
         * @param cls The class of the activity to start.
         */
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start MainActivity
                Intent intent = new Intent(EndGameActivity.this, MainMenu.class);

                // If you don't want to create a new instance of MainActivity on top of the existing instances in the task,
                // add the following flags to the intent.
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                // Start the MainActivity
                startActivity(intent);
                finish();
            }
        });
    }
}