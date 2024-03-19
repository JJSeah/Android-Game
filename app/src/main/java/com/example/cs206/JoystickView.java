package com.example.cs206;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JoystickView extends View {
    private float xPosition;
    private float yPosition;
    private float center_x, center_y;
    private Paint circlePaint;
    private JoystickListener joystickListener;

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setColor(0xFF00CC00);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setStrokeJoin(Paint.Join.ROUND);
        circlePaint.setStrokeCap(Paint.Cap.ROUND);
        circlePaint.setStrokeWidth(8);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        center_x = w / 2;
        center_y = h / 2;
        xPosition = center_x;
        yPosition = center_y;
    }

    public float getNormalizedX() {
        return (xPosition - center_x) / center_x;
    }

    public float getNormalizedY() {
        return (center_y - yPosition) / center_y;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the outer circle of the joystick
        circlePaint.setColor(Color.GRAY);
        canvas.drawCircle(center_x, center_y, 100, circlePaint);

        // Draw the inner circle of the joystick
        circlePaint.setColor(Color.parseColor("#A9A9A9"));
        canvas.drawCircle(xPosition, yPosition, 50, circlePaint);

//        // Draw the buttons of the retro controller
//        circlePaint.setColor(Color.RED);
//        canvas.drawCircle(center_x - 150, center_y, 30, circlePaint);
//        canvas.drawCircle(center_x - 150, center_y - 70, 30, circlePaint);
//        canvas.drawCircle(center_x - 150, center_y + 70, 30, circlePaint);
//        canvas.drawCircle(center_x - 220, center_y, 30, circlePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float newXPosition = event.getX();
                float newYPosition = event.getY();

                // Calculate the distance from the center of the joystick to the touch point
                float distance = (float) Math.sqrt(Math.pow(newXPosition - center_x, 2) + Math.pow(newYPosition - center_y, 2));

                // If the distance is less than the radius of the joystick, update the position of the joystick
                if (distance < 100) {
                    xPosition = newXPosition;
                    yPosition = newYPosition;
                } else {
                    // If the distance is greater than the radius of the joystick, keep the joystick on the edge of the circle
                    xPosition = center_x + (newXPosition - center_x) * 100 / distance;
                    yPosition = center_y + (newYPosition - center_y) * 100 / distance;
                }

                if (joystickListener != null) {
                    joystickListener.onJoystickMoved((xPosition - center_x) / center_x, (center_y - yPosition) / center_y);
                }
                break;

            case MotionEvent.ACTION_UP:
                // When the user stops touching the screen, reset the position of the joystick to the center
                xPosition = center_x;
                yPosition = center_y;

                if (joystickListener != null) {
                    joystickListener.onJoystickReleased();
                }
                break;
        }

        invalidate();

        return true;
    }

    public void setJoystickListener(JoystickListener joystickListener) {
        this.joystickListener = joystickListener;
    }

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent);
        void onJoystickReleased();
        void onJoystickHold(float xPercent, float yPercent);
    }

    public void setColor(int color) {
        circlePaint.setColor(color);
        invalidate();
    }
}