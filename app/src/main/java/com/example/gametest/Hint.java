package com.example.gametest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Hint {
    private double positionX;
    private double positionY;
    private double radius;
    private Paint paint;
    public Hint(Context context, double positionX, double positionY, double radius) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;

        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.hint);
        paint.setColor(color);
    }
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float) positionY, (float) radius, paint);
    }

    public double getPositionX(){
        return this.positionX = positionX;
    }
    public double getPositionY(){
        return this.positionY = positionY;
    }

    public double getRadius(){
        return this.radius = radius;
    }
}
