package com.example.gametest;

import static com.google.android.material.math.MathUtils.dist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.core.content.ContextCompat;

public class Player{
    public final static double MAX_SPEED = 6.5 ;
    public double positionX;
    public double positionY;
    public double radius;
    private Paint paint;
    private double velocityX;
    private double velocityY;

    public Player(Context context, double positionX, double positionY, double radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.player);
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
    }

    public void update(Joystick joystick) {
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;

    }

    public void setPosition(double positionX, double positionY, Joystick joystick) {
        //player đi lên
        if(positionY < 0) {
            this.positionX = positionX;
            this.positionY = positionY + velocityY;

            //player đi lên + trái
            if (positionX < 0) {
                this.positionY = positionY + velocityY;
                this.positionX = positionX + velocityX;
            }
            //player đi lên + phải
            else {
                this.positionY = positionY + velocityY;
                this.positionX = positionX - velocityX;
            }
        }
        //player đi xuống
        else {
            this.positionX = positionX;
            this.positionY = positionY - velocityY;

            //player đi xuống + trái
            if (joystick.getActuatorX() < 0) {
                this.positionY = positionY - velocityY;
                this.positionX = positionX + velocityX;
                joystick.resetActuator();
            }
            //player đi xuống + phải
            else {
                this.positionY = positionY - velocityY;
                this.positionX = positionX - velocityX;
            }
        }
        //player đi phải
        if (positionX > 0) {
            this.positionX = positionX - velocityX;
            this.positionY = positionY;
            //player đi phải + lên
            if ((positionY < 0)) {
                this.positionX = positionX - velocityX;
                this.positionY = positionY + velocityY;
            }
            //player đi phải + xuống
            else {
                this.positionX = positionX - velocityX;
                this.positionY = positionY - velocityY;
            }
        }
        //player đi trái
        else {
            this.positionX = positionX + velocityX;
            this.positionY = positionY;
            //player đi trái + lên
            if ((joystick.getActuatorY() < 0)) {
                this.positionX = positionX + velocityX;
                this.positionY = positionY + velocityY;
            }
            //player đi trái + xuống
            else {
                this.positionX = positionX + velocityX;
                this.positionY = positionY - velocityY;
            }
        }
    }


}




