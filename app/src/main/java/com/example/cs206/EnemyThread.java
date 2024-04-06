package com.example.cs206;

public class EnemyThread extends Thread {
    private Enemy enemy;
    private boolean isRunning = false;

    public EnemyThread(Enemy enemy) {
        this.enemy = enemy;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning && !Thread.currentThread().isInterrupted()) {
            if(!isRunning) {System.out.println(isRunning);}
            enemy.update(); // Update the enemy's position
            try {
                Thread.sleep(1); // Sleep for a short time to avoid using 100% CPU
            } catch (InterruptedException e) {
                isRunning = false;
            }
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}