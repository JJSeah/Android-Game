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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EndGameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        EdgeToEdge.enable(this);

        Button restartButton = (Button) findViewById(R.id.restartButton);
        Button menuButton = (Button) findViewById(R.id.menuButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EndGameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

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

        // Calculate the top margin to position the button at the third quarter of the screen height
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int screenHeight = displayMetrics.heightPixels;
//        int topMargin = (int) (screenHeight * 0.7);
//        int menuTopMargin = (int) (screenHeight * 0.8);;
//
//        // Set the parameters for the button
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) restartButton.getLayoutParams();
//        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        layoutParams.topMargin = topMargin;
//        restartButton.setLayoutParams(layoutParams);
//
//        // Set the parameters for the button
//        RelativeLayout.LayoutParams menuLayoutParams = (RelativeLayout.LayoutParams) menuButton.getLayoutParams();
//        menuLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        menuLayoutParams.topMargin = menuTopMargin;
//        menuButton.setLayoutParams(menuLayoutParams);
    }

//    private void setBackground() {
//        // Get the size of the screen
//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        int screenWidth = metrics.widthPixels;
//        int screenHeight = metrics.heightPixels;
//
//        // Load and scale the background to fit the screen
//        Bitmap originalBackground = BitmapFactory.decodeResource(getResources(), R.drawable.sg_3);
//        Bitmap background = Bitmap.createScaledBitmap(originalBackground, screenWidth, screenHeight, false);
//
//        // Set the scaled bitmap as the source for the ImageView
//        backgroundImageView.setImageBitmap(background);
//    }
}