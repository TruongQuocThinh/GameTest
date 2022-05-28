package com.example.gametest;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.gametest.graphics.Animator;
import com.example.gametest.graphics.Sprite;

public class Player{
    public final static double MAX_SPEED = 6.5 ;
    public double positionX;
    public double positionY;
    public double radius;
    private Paint paint;
    private double velocityX;
    private double velocityY;
    private Animator animator;
    private PlayerState playerState;

    public Player(Context context, double positionX, double positionY, double radius, Animator animator) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.radius = radius;
        this.animator = animator;
        this.playerState = new PlayerState(this);
    }

    public void draw(Canvas canvas) {
        animator.draw(canvas, this);
    }

    public void update(Joystick joystick) {
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;
        
        playerState.update();

    }

    /**
     * isColliding check if two circle objects are colliding, based on their position and radii
     * @param player
     * @param hint
     * @return
     */
    public boolean isColliding(Player player, Hint hint){
        double xDif = positionX - hint.getPositionX();
        double yDif = positionY - hint.getPositionY();
        double distanceSquared = xDif * xDif + yDif * yDif;
        if (distanceSquared < (radius + hint.getRadius()) * (radius + hint.getRadius() - 35)){
            return true;
        }
        else{
            return false;
        }
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


    public PlayerState getPlayerState() {
        return playerState;
    }

    public double getVelocityX(){
        return velocityX;
    }

    public double getVelocityY(){
        return velocityY;
    }
}




