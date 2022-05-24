package com.example.gametest;

import static com.google.android.material.math.MathUtils.dist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.core.content.ContextCompat;

public class Player{
    public double MAX_SPEED;
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
        MAX_SPEED = 8;
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

    // LINE/CIRCLE
    /*boolean checkCollision(double x1, double y1, double x2, double y2, double cx, double cy, double r) {

        // is either end INSIDE the circle?
        // if so, return true immediately
        boolean inside1 = pointCircle(x1,y1, cx,cy,r);
        boolean inside2 = pointCircle(x2,y2, cx,cy,r);
        if (inside1 || inside2) return true;

        // get length of the line
        double distX = x1 - x2;
        double distY = y1 - y2;
        double len = Math.sqrt( (distX*distX) + (distY*distY) );

        // get dot product of the line and circle
        double dot = ( ((cx-x1)*(x2-x1)) + ((cy-y1)*(y2-y1)) ) / Math.pow(len,2);

        // find the closest point on the line
        double closestX = x1 + (dot * (x2-x1));
        double closestY = y1 + (dot * (y2-y1));

        // is this point actually on the line segment?
        // if so keep going, but if not, return false
        boolean onSegment = linePoint(x1,y1,x2,y2, closestX,closestY);
        if (!onSegment) return false;

        // optionally, draw a circle at the closest
        // point on the line
        /*fill(255,0,0);
        noStroke();
        ellipse(closestX, closestY, 20, 20);*/

        // get distance to closest point
        /*distX = closestX - cx;
        distY = closestY - cy;
        double distance = (double) Math.sqrt( (distX*distX) + (distY*distY) );

        if (distance <= r) {
            return true;
        }
        return false;
    }
    boolean pointCircle(double px, double py, double cx, double cy, double r) {

        // get distance between the point and circle's center
        // using the Pythagorean Theorem
        double distX = px - cx;
        double distY = py - cy;
        double distance = (double) Math.sqrt( (distX*distX) + (distY*distY) );

        // if the distance is less than the circle's
        // radius the point is inside!
        if (distance <= r) {
            return true;
        }
        return false;
    }
    boolean linePoint(double x1, double y1, double x2, double y2, double px, double py) {

        // get distance from the point to the two ends of the line
        double d1 = dist((float) px,(float) py,(float) x1,(float) y1);
        double d2 = dist((float) px,(float) py,(float) x2,(float) y2);

        // get the length of the line
        float lineLen = dist((float) x1,(float) y1,(float) x2,(float) y2);

        // since floats are so minutely accurate, add
        // a little buffer zone that will give collision
        float buffer = (float) 0.1;    // higher # = less accurate

        // if the two distances are equal to the line's
        // length, the point is on the line!
        // note we use the buffer here to give a range,
        // rather than one #
        if (d1+d2 >= lineLen-buffer && d1+d2 <= lineLen+buffer) {
            return true;
        }
        return false;
    }*/



