package com.example.cs206;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

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



    public Enemy(float x, float y, float speedX, float speedY, float screenWidth, float screenHeight) {
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

    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        float timeDelta = (currentTime - lastUpdateTime) / 1000f; // Calculate the time delta in seconds

        x += speedX * timeDelta;
        y += speedY * timeDelta;

        // Check for boundary and reverse direction if necessary
        if (x < 0 || x > screenWidth) {
            speedX = -speedX;
        }
        if (y < 0 || y > screenHeight * 0.75) { // Limit the enemy's y position to 3/4 of the screen height
            speedY = -speedY;
        }

        lastUpdateTime = currentTime; // Update the last update time
    }

    public void draw(Canvas canvas, Paint paint) {
//        paint.setColor(Color.RED);
        canvas.drawRect(x, y, x + 50, y + 50, paint);
    }

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    public void drawHealthBar(Canvas canvas, Paint paint) {
        paint.setColor(Color.GREEN);
        canvas.drawRect(x, y - 20, x + health, y - 10, paint);

    }

    public boolean checkCollision(Player player) {
        float distance = (float) Math.sqrt(Math.pow(player.getX() - x, 2) + Math.pow(player.getY() - y, 2));
        return distance < player.getRadius() + 50; // 50 is the radius of the enemy
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