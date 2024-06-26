package com.example.cs206;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import java.util.Random;

public class Enemy {
    private float x;
    private float y;
    private float speedX;
    private float speedY;
    private float screenWidth;
    private float screenHeight;
    private int health = 100; // Initial health
    private long lastUpdateTime;
    private Paint paint; // Used for drawing the enemy
    private Bitmap enemyImage;
    /**
     * Constructor for the Enemy class.
     *
     * @param x Initial X position of the enemy.
     * @param y Initial Y position of the enemy.
     * @param speedX Initial horizontal speed of the enemy.
     * @param speedY Initial vertical speed of the enemy.
     * @param screenWidth Width of the screen.
     * @param screenHeight Height of the screen.
     * @param res Resources object to load the enemy image.
     */
    public Enemy(float x, float y, float speedX, float speedY, float screenWidth, float screenHeight, Resources res) {
        this.x = x;
        this.y = y;
        this.speedX = speedX *20;
        this.speedY = speedY *20;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.lastUpdateTime = System.currentTimeMillis(); // Initialize the last update time

        // Initialize the Paint object
        paint = new Paint();
        paint.setColor(Color.RED); // Set the color to red
        paint.setShader(new LinearGradient(0, 0, 100, 100, Color.RED, Color.BLUE, Shader.TileMode.MIRROR)); // Set a gradient shader

        // Load the enemy image
        enemyImage = BitmapFactory.decodeResource(res, R.drawable.game);
        enemyImage = Bitmap.createScaledBitmap(enemyImage, 100, 100, false); // Assuming the enemy size is 100x100
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }


    public void update() {
        long currentTime = System.currentTimeMillis();
        float timeDelta = (currentTime - lastUpdateTime) / 1000f; // Calculate the time delta in seconds

        // Update the enemy's position based on its speed
        x += speedX * timeDelta;
        y += speedY * timeDelta;

        // Check for boundary and reverse direction if necessary
        if (x < 0 || x > screenWidth * 0.95) {
            speedX = -speedX;
        }
        if (y < 0 || y > screenHeight * 0.70) { // Limit the enemy's y position to 3/4 of the screen height
            speedY = -speedY;
        }

        lastUpdateTime = currentTime; // Update the last update time
    }

    public void draw(Canvas canvas, Paint paint) {
        // Draw the enemy image at the enemy's position
        canvas.drawBitmap(enemyImage, x, y, null);
    }



public void drawHealthBar(Canvas canvas, Paint paint) {
    int startColor;
    int endColor;

    if (health <= 30) {
        startColor = Color.RED;
        endColor = Color.YELLOW;
    } else if (health <= 70) {
        startColor = Color.YELLOW;
        endColor = Color.GREEN;
    } else {
        startColor = Color.GREEN;
        endColor = Color.BLUE;
    }

    Shader shader = new LinearGradient(x, y - 20, x + health, y - 10, startColor, endColor, Shader.TileMode.CLAMP);
    paint.setShader(shader);

    canvas.drawRect(x, y - 20, x + health, y - 10, paint);
}

    //get health
    public int getHealth() {
        return health;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public float getRadius() {
        return 50;
    }
}