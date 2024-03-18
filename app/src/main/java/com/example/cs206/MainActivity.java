package com.example.cs206;

import com.example.cs206.JoystickView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
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

        avatar = (ImageView) findViewById(R.id.avatar);

        JoystickView joystick = (JoystickView) findViewById(R.id.joystickView);
        joystick.setJoystickListener(new JoystickView.JoystickListener() {
            @Override
            public void onJoystickMoved(float xPercent, float yPercent) {
                xPosition = xPercent;
                yPosition = yPercent;

                float newXPosition = avatar.getX() + xPosition * 10;
                float newYPosition = avatar.getY() - yPosition * 10;

                // Check if the new position is within the screen bounds
                if (newXPosition >= 0 && newXPosition <= findViewById(R.id.main).getWidth() - avatar.getWidth()) {
                    avatar.setX(newXPosition);
                }
                if (newYPosition >= 0 && newYPosition <= findViewById(R.id.main).getHeight() - avatar.getHeight()) {
                    avatar.setY(newYPosition);
                }

                // Calculate the avatar's position as a percentage of the screen width and height
                float xPercentage = avatar.getX() / findViewById(R.id.main).getWidth();
                float yPercentage = avatar.getY() / findViewById(R.id.main).getHeight();

                // Use these percentages to create a color
                int red = (int) (xPercentage * 255);
                int blue = (int) (yPercentage * 255);
                int color = 0xFF000000 | (red << 16) | blue;

                // Update the color of the joystick
                joystick.setColor(color);
            }
        });
    }
}