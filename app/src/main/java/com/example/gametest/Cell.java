package com.example.gametest;

import static com.google.android.material.math.MathUtils.dist;

import android.content.Context;
import android.graphics.Canvas;

import androidx.constraintlayout.solver.widgets.Rectangle;

public class Cell extends Rectangle {
    public double topWall = 0;
    public double leftWall = 0;
    public double bottomWall = 0;
    public double rightWall = 0;
    public double col;
    public double row;

    public Cell(int col, int row){
        this.col = col;
        this.row = row;

    }
    boolean checkCollision(double x1, double y1, double x2, double y2, double cx, double cy, double r) {

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
        distX = closestX - cx;
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
        double lineLen = dist((float) x1,(float) y1,(float) x2,(float) y2);

        // since floats are so minutely accurate, add
        // a little buffer zone that will give collision
        double buffer = (float) 0.1;    // higher # = less accurate

        // if the two distances are equal to the line's
        // length, the point is on the line!
        // note we use the buffer here to give a range,
        // rather than one #
        if (d1+d2 >= lineLen-buffer && d1+d2 <= lineLen+buffer) {
            return true;
        }
        return false;
    }

}
