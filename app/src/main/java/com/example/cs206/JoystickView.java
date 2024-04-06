package com.example.cs206;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * JoystickView class that represents a joystick control.
 */
public class JoystickView extends View {
    private float joystickXPosition;
    private float joystickYPosition;
    private float center_x, center_y;
    private Paint circlePaint;
    private JoystickListener joystickListener;
    private boolean isJoystickHeld = false;
    private Handler handler = new Handler();

    /**
     * Constructor for the JoystickView class.
     *
     * @param context The context of the application.
     * @param attrs The attribute set.
     */
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

    /**
     * Runnable that continuously updates the joystick position while it is being held.
     */
    private Runnable joystickRunnable = new Runnable() {
        @Override
        public void run() {
            if (isJoystickHeld && joystickListener != null) {
                joystickListener.onJoystickHold(getNormalizedX(), getNormalizedY());
            }
            handler.postDelayed(this, 100); // Adjust the delay as needed
        }
    };

    /**
     * Method that is called when the view is detached from the window.
     * It removes the joystickRunnable from the handler.
     */
    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        handler.removeCallbacks(joystickRunnable);
    }

    /**
     * Method that is called when the size of the view is changed.
     * It updates the center and joystick positions.
     *
     * @param w The new width of the view.
     * @param h The new height of the view.
     * @param oldw The old width of the view.
     * @param oldh The old height of the view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        center_x = w / 2;
        center_y = h / 2;
        joystickXPosition = center_x;
        joystickYPosition = center_y;
    }

    /**
     * Method that returns the normalized x position of the joystick.
     *
     * @return The normalized x position of the joystick.
     */
    public float getNormalizedX() {
        return (joystickXPosition - center_x) / center_x;
    }

    /**
     * Method that returns the normalized y position of the joystick.
     *
     * @return The normalized y position of the joystick.
     */
    public float getNormalizedY() {
        return (center_y - joystickYPosition) / center_y;
    }

    /**
     * Method that draws the joystick on the canvas.
     *
     * @param canvas The canvas on which the joystick is drawn.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        circlePaint.setColor(Color.GRAY);
        canvas.drawCircle(center_x, center_y, 100, circlePaint);

        circlePaint.setColor(Color.parseColor("#A9A9A9"));
        canvas.drawCircle(joystickXPosition, joystickYPosition, 50, circlePaint);
    }

    /**
     * Method that handles touch events on the joystick.
     *
     * @param event The MotionEvent object that contains the details of the touch event.
     * @return A boolean indicating whether the touch event was handled.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float newXPosition = event.getX();
                float newYPosition = event.getY();

                float distance = (float) Math.sqrt(Math.pow(newXPosition - center_x, 2) + Math.pow(newYPosition - center_y, 2));

                if (distance < 100) {
                    joystickXPosition = newXPosition;
                    joystickYPosition = newYPosition;
                } else {
                    joystickXPosition = center_x + (newXPosition - center_x) * 100 / distance;
                    joystickYPosition = center_y + (newYPosition - center_y) * 100 / distance;
                }

                if (joystickListener != null) {
                    joystickListener.onJoystickMoved((joystickXPosition - center_x) / center_x, (center_y - joystickYPosition) / center_y);
                }
                isJoystickHeld = true;
                handler.post(joystickRunnable);
                break;

            case MotionEvent.ACTION_UP:
                joystickXPosition = center_x;
                joystickYPosition = center_y;

                if (joystickListener != null) {
                    joystickListener.onJoystickReleased();
                }
                isJoystickHeld = false;
                handler.removeCallbacks(joystickRunnable);
                break;
        }

        invalidate();

        return true;
    }

    /**
     * Method that sets the joystick listener.
     *
     * @param joystickListener The joystick listener.
     */
    public void setJoystickListener(JoystickListener joystickListener) {
        this.joystickListener = joystickListener;
    }

    /**
     * Interface that defines the methods for a joystick listener.
     */
    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent);
        void onJoystickReleased();
        void onJoystickHold(float xPercent, float yPercent);
    }

    /**
     * Method that sets the color of the joystick.
     *
     * @param color The color of the joystick.
     */
    public void setColor(int color) {
        circlePaint.setColor(color);
        invalidate();
    }
}