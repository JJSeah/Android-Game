package com.example.cs206;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button leftButton;
    Button rightButton;
    Button upButton;
    Button downButton;
    ImageView avatar;
    private float xPosition = 0;
    private float yPosition = 0;

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

        leftButton = (Button) findViewById(R.id.buttonLeft);
        rightButton = (Button) findViewById(R.id.buttonRight);
        upButton = (Button) findViewById(R.id.buttonUp);
        downButton = (Button) findViewById(R.id.buttonDown);
        avatar = (ImageView) findViewById(R.id.avatar);

        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xPosition -= 10;
                avatar.setTranslationX(xPosition);
                return false;
            }
        });

        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xPosition += 10;
                avatar.setTranslationX(xPosition);
                return false;
            }
        });


        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                
                yPosition -= 10;
                avatar.setTranslationY(yPosition);
                return false;
            }
        });


        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                yPosition += 10;
                avatar.setTranslationY(yPosition);
                return false;
            }
        });

    }
}