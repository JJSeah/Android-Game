package com.example.cs206;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player {
    private float x;
    private float y;
    private float radius;
    private float direction;
    private float screenWidth;
    private float screenHeight;

    private List<Bullet> bullets;

    private long lastFireTime = 0;
    private static final long FIRE_COOLDOWN = 1000; // Cooldown time in milliseconds

    private boolean isReloading = false;
    private ScheduledExecutorService scheduler;


    public Player(float x, float y, float radius, float screenWidth, float screenHeight) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.direction = 0;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.bullets = new ArrayList<>();
        this.scheduler = Executors.newScheduledThreadPool(1);

    }

    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        canvas.drawCircle(x, y, radius, paint);

        // Draw all the bullets
        for (Bullet bullet : bullets) {
            bullet.update();
            bullet.draw(canvas, paint);
        }
    }

    public synchronized void shoot(float direction) {
        // Only fire a bullet if not currently reloading
        if (!isReloading) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFireTime >= FIRE_COOLDOWN) {
                Bullet bullet = new Bullet(x, y, direction, screenWidth, screenHeight);
                bullets.add(bullet);
                lastFireTime = currentTime;
                reload(); // Start reloading after shooting
            }
        }
    }


    public int getReloadProgress() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastFire = currentTime - lastFireTime;
        int progress = (int) ((timeSinceLastFire / (float) FIRE_COOLDOWN) * 100);
        return Math.min(progress, 100);
    }

public synchronized void reload() {
    isReloading = true;
    final int intervalSteps = 10; // Number of steps for the reload progress
    final long timeDelta = FIRE_COOLDOWN / intervalSteps; // Time for each step

    // Use an array to hold the Future
    final Future<?>[] future = new Future<?>[1];

    // Schedule a task to run every timeDelta milliseconds
    future[0] = scheduler.scheduleAtFixedRate(new Runnable() {
        private int steps = 0;
        @Override
        public void run() {
            steps++;
            if (steps >= intervalSteps) {
                isReloading = false;
                future[0].cancel(false); // Cancel the task when reloading is complete
            }
        }
    }, timeDelta, timeDelta, TimeUnit.MILLISECONDS);
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

    public List<Bullet> getBullets() {
        return bullets;
    }
}