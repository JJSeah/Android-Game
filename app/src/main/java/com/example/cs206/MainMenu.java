package com.example.cs206;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This is the main menu class for the application.
 * It extends AppCompatActivity, which is a base class for activities that use the support library action bar features.
 */
public class MainMenu extends AppCompatActivity {

    /**
     * This is the onCreate method which is called when the activity is starting.
     * This is where most initialization should go.
     * @param savedInstanceState This is a reference to a Bundle object that is passed into the onCreate method of every Android Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        // Set onClickListener for the start button to start a new activity
        findViewById(R.id.startButton).setOnClickListener(v -> startNewActivity(MainActivity.class, false));

        // Set onClickListener for the leaderboard button to start a new activity
        findViewById(R.id.leaderboardButton).setOnClickListener(v -> startNewActivity(LeaderboardActivity.class, true));
    }

    /**
     * This method is used to start a new activity.
     * @param cls The class of the activity to start.
     * @param clearBackStack A boolean indicating whether to clear the back stack.
     */
    private void startNewActivity(Class<?> cls, boolean clearBackStack) {
        Intent intent = new Intent(MainMenu.this, cls);
        if (clearBackStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        startActivity(intent);
        if (clearBackStack) {
            finish();
        }
    }
}