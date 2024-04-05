package com.example.cs206;

public class EnemyThread extends Thread {
    private Enemy enemy;
    private static final int MOVE_COOLDOWN = 1000; // Cooldown time in milliseconds

    public EnemyThread(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            enemy.update(); // Update the enemy's position
            try {
                Thread.sleep(1); // Sleep for a short time to avoid using 100% CPU
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Preserve the interrupt
            }
        }
    }
}
