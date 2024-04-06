package com.example.cs206;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * This is the Bullet class for the application.
 * It represents a bullet shot by the player in the game.
 */
public class Bullet {
    private static final float SPEED = 10; // Bullet speed
    private static final int COLOR = Color.WHITE; // Bullet color

    private float x;
    private float y;
    private float direction;
    private float radius = 70;
    private float screenWidth;
    private float screenHeight;

    /**
     * Bullet constructor.
     * @param x The initial x-coordinate of the bullet.
     * @param y The initial y-coordinate of the bullet.
     * @param direction The direction of the bullet.
     * @param screenWidth The width of the screen.
     * @param screenHeight The height of the screen.
     */
    public Bullet(float x, float y, float direction, float screenWidth, float screenHeight) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Updates the bullet's position based on its direction and speed.
     */
    public void update() {
        x += Math.cos(direction) * SPEED;
        y += Math.sin(direction) * SPEED;
    }

    /**
     * Draws the bullet on the canvas.
     * @param canvas The canvas to draw on.
     * @param paint The paint used to draw.
     */
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(COLOR);
        canvas.drawCircle(x, y, 10, paint);
    }

    /**
     * Checks if the bullet is out of bounds.
     * @return True if the bullet is out of bounds, false otherwise.
     */
    public boolean isOutOfBounds() {
        return x + radius < 0 || x - radius > screenWidth+70 || y + radius < 0 || y - radius > screenHeight;
    }

    /**
     * Checks if the bullet hits an enemy.
     * @param enemy The enemy to check collision with.
     * @return True if the bullet hits the enemy, false otherwise.
     */
    public boolean hitsEnemy(Enemy enemy) {
        float dx = x - enemy.getX();
        float dy = y - enemy.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        boolean collision = distance <= radius + enemy.getRadius();

        // If a collision is detected, decrease the enemy's health
        if (collision) {
            enemy.decreaseHealth(10); // Decrease health by 10, adjust this value as needed
        }
        return collision;
    }
}