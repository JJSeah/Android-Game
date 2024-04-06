package com.example.cs206;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This is the Player class for the application.
 * It represents the player in the game.
 */
public class Player {
    // Player's position, direction, screen dimensions, and other properties
    private float x, y, radius, direction, screenWidth, screenHeight;
    private int health = 100; // Initial health
    private long lastFireTime = 0;
    private static final long FIRE_COOLDOWN = 1000; // Cooldown time in milliseconds
    private AtomicBoolean isReloading = new AtomicBoolean(false);
    private ReentrantLock lock = new ReentrantLock();

    // List of bullets shot by the player
    private List<Bullet> bullets;
    private ScheduledExecutorService scheduler;
    private BlockingQueue<Integer> damageQueue = new LinkedBlockingQueue<>();
    private Bitmap playerImage;
    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 5000; // Cooldown time in milliseconds


    /**
     * Player constructor.
     * @param x The initial x-coordinate of the player.
     * @param y The initial y-coordinate of the player.
     * @param radius The radius of the player.
     * @param screenWidth The width of the screen.
     * @param screenHeight The height of the screen.
     * @param res The resources object to load the player image.
     */
    public Player(float x, float y, float radius, float screenWidth, float screenHeight, Resources res) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.direction = 0;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.bullets = new ArrayList<>();
        this.scheduler = Executors.newScheduledThreadPool(1);

        // Load the player image
        playerImage = BitmapFactory.decodeResource(res, R.drawable.spaceship);
        playerImage = Bitmap.createScaledBitmap(playerImage, (int) (2 * radius), (int) (2 * radius), false);
    }

    // Player's position and health methods
    public float getX() { return x; }
    public float getY() { return y; }
    public float getRadius() { return radius; }
    public float getDirection() { return direction; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public int getHealth() { return health; }

    // Bullet related methods
    public List<Bullet> getBullets() { return bullets; }
    public int getReloadProgress() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastFire = currentTime - lastFireTime;
        int progress = (int) ((timeSinceLastFire / (float) FIRE_COOLDOWN) * 100);
        return Math.min(progress, 100);
    }

    // Damage related methods
    public void takeDamage(int damage) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDamageTime >= DAMAGE_COOLDOWN) {
            try {
                damageQueue.put(damage);
                lastDamageTime = currentTime;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public void updateHealth() {
        new Thread(() -> {
            while (true) {
                try {
                    int damage = damageQueue.take();
                    health -= damage;
                    Log.d("COLLISION", "Player's health is now " + health);
                    if (health <= 0) {
                        break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    // Shooting and reloading methods
    public void shoot(float direction) {
        lock.lock();
        try {
            if (!isReloading.get()) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastFireTime >= FIRE_COOLDOWN) {
                    Bullet bullet = new Bullet(x, y, direction, screenWidth, screenHeight);
                    bullets.add(bullet);
                    lastFireTime = currentTime;
                    reload();
                }
            }
        } finally {
            lock.unlock();
        }
    }
    public void reload() {
        isReloading.set(true);
        final int intervalSteps = 10; // Number of steps for the reload progress
        final long timeDelta = FIRE_COOLDOWN / intervalSteps; // Time for each step

        scheduler.scheduleAtFixedRate(new Runnable() {
            private int steps = 0;
            @Override
            public void run() {
                steps++;
                if (steps >= intervalSteps) {
                    isReloading.set(false);
                }
            }
        }, timeDelta, timeDelta, TimeUnit.MILLISECONDS);
    }

    // Drawing methods
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(playerImage, x - radius, y - radius, paint);
        for (Bullet bullet : bullets) {
            bullet.update();
            bullet.draw(canvas, paint);
        }
    }
}