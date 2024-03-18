package com.example.cs206;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Enemy {
    private float x;
    private float y;
    private float speedX;
    private float speedY;
    private float screenWidth;
    private float screenHeight;
    private int health = 100; // Initial health

    public Enemy(float x, float y, float speedX, float speedY, float screenWidth, float screenHeight) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update() {
        x += speedX;
        y += speedY;

        // Check for boundary and reverse direction if necessary
        if (x < 0 || x > screenWidth) {
            speedX = -speedX;
        }
        if (y < 0 || y > screenHeight * 0.75) { // Limit the enemy's y position to 3/4 of the screen height
            speedY = -speedY;
        }
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.RED);
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
}