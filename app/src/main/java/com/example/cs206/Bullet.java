package com.example.cs206;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bullet {
    private float x;
    private float y;
    private float direction;
    private float speed;
    private  float radius = 70;
    private float screenWidth;
    private float screenHeight;

    public Bullet(float x, float y, float direction, float screenWidth, float screenHeight) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 10; // Initialize the speed here
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void update() {
        // Update the bullet's position based on its direction
        x += Math.cos(direction) * speed;
        y += Math.sin(direction) * speed;
    }
    public void move() {
        // Update the bullet's position based on its direction
        x += Math.cos(direction) * speed;
        y += Math.sin(direction) * speed;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.WHITE); // Change this line
        canvas.drawCircle(x, y, 10, paint);
    }

    public float getDirection(){
        return direction;
    }



    public boolean collidesWithWall(float screenWidth, float screenHeight) {
        // Check if the bullet collides with the wall
        return x - radius < 0 || x + radius > screenWidth || y - radius < 0 || y + radius > screenHeight;
    }

    public boolean collidesWith(Enemy enemy) {
        // Check if the bullet collides with the enemy
        float dx = x - enemy.getX();
        float dy = y - enemy.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        boolean collision = distance <= radius + enemy.getRadius();

        // If a collision is detected, decrease the enemy's health and print a debug message
        if (collision) {
            enemy.decreaseHealth(10); // Decrease health by 10, adjust this value as needed
        }
        return collision;
    }

}