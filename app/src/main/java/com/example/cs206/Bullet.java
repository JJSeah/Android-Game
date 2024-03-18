package com.example.cs206;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Bullet {
    private float x;
    private float y;
    private float direction;
    private float speed; // Add this line

    public Bullet(float x, float y, float direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = 10; // Initialize the speed here
    }

    public void update() {
        // Update the bullet's position based on its direction
        x += Math.cos(direction) * speed;
        y += Math.sin(direction) * speed;
    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, 10, paint);
    }

    public float getDirection(){
        return direction;
    }
}