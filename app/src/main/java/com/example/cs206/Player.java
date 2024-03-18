package com.example.cs206;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player {
    private float x;
    private float y;
    private float radius;
    private float direction; // Add this line

    private List<Bullet> bullets;

    public Player(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.direction = 0;
        this.bullets = new ArrayList<>();

    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void shoot() {
        // Create a new bullet and add it to the list
        Bullet bullet = new Bullet(getX(), getY(), getDirection());
        bullets.add(bullet);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRadius() {
        return radius;
    }

    public float getDirection() { // Add this method
        return direction;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    // Add other methods as necessary...
}