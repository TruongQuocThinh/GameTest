package com.example.gametest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.List;

import javax.sql.RowSet;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Hint hint;
    private Cell[][] cells;
    private static final int COLS = 5, ROWS = 10;
    private static final float WALL_THICKNESS = 5;
    private float cellSize, hMargin, vMargin;
    private Paint wallPaint;
    private final Player player;
    private final Joystick joystick;
    private GameLoop gameLoop;
    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);

        //player and joystick, cell, hint
        wallPaint = new Paint();
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStrokeWidth(WALL_THICKNESS);
        joystick = new Joystick(500, 1950, 70, 30);
        player = new Player(getContext(), 185, 850, 40);
        hint = new Hint( getContext(), 725, 1210, 40);
        setFocusable(true);

        createMaze();
    }

    private void createMaze() {
        cells = new Cell[COLS][ROWS];

        for(int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }

    }

    private boolean checkMaze(){
        for(int x = 0; x < COLS; x++ ){
            for(int y = 0; y < ROWS; y++){
                if(cells[x][y].topWall == 0 && cells[x][y].bottomWall == 0){
                    if(!cells[x][y].checkCollision(x * cellSize, y * cellSize, (x + 1)* cellSize, y * cellSize,player.positionX, player.positionY, player.radius) && !cells[x][y].checkCollision(x * cellSize, (y + 1) * cellSize, (x+1)* cellSize, (y + 1) * cellSize,player.positionX, player.positionY, player.radius)){
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                /*if(cells[x][y].bottomWall == 0){
                    if(cells[x][y].checkCollision(x * cellSize, (y + 1) * cellSize, (x+1)* cellSize, (y + 1) * cellSize,player.positionX, player.positionY, player.radius)){
                        return false;
                    }
                    else{
                        return true;
                    }
                }*/
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Handel touch event action
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(joystick.isPressed((double) event.getX(), (double) event.getY())){
                    joystick.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        player.draw(canvas);
        joystick.draw(canvas);
        hint.draw(canvas);
        int width = getWidth();
        int height = getHeight();

        if(width/height < COLS/ROWS){
            cellSize = width / (COLS + 1);
        }
        else{
            cellSize = height / (ROWS + 1);
        }
        hMargin = (width - COLS*cellSize)/2;
        vMargin = (float) ((height - ROWS*cellSize)/10);

        canvas.translate(hMargin, vMargin);

        for(int x = 0; x < COLS; x++ ){
            if(cells[x][0].topWall == 0){
                canvas.drawLine(x * cellSize, 0 * cellSize, (x + 1)* cellSize, 0 * cellSize, wallPaint );
                cells[x][0].checkCollision(x * cellSize, 0 * cellSize, (x + 1)* cellSize, 0 * cellSize,player.positionX, player.positionY, player.radius);
            }
            if(cells[x][ROWS - 1].bottomWall == 0){
                canvas.drawLine(x * cellSize, (ROWS - 1 + 1) * cellSize, (x+1)* cellSize, (ROWS - 1 + 1) * cellSize, wallPaint );
                cells[x][ROWS - 1].checkCollision(x * cellSize, (ROWS - 1 + 1) * cellSize, (x+1)* cellSize, (ROWS - 1 + 1) * cellSize,player.positionX, player.positionY, player.radius);

            }
        }

        for(int y = 0; y < ROWS; y++){
            if(cells[0][y].leftWall == 0){
                canvas.drawLine(0 * cellSize, y * cellSize, 0 * cellSize, (y + 1) * cellSize, wallPaint );
            }
            if(cells[COLS - 1][y].rightWall == 0){
                canvas.drawLine((COLS - 1 + 1) * cellSize, (y + 1) * cellSize, (COLS - 1 + 1)* cellSize, y * cellSize, wallPaint );
            }
        }
        //cell 0;0
        canvas.drawLine(0 * cellSize, (0 + 1) * cellSize, (0 + 1) * cellSize, (0 + 1) * cellSize, wallPaint);
        //cell 2;0
        canvas.drawLine(2 * cellSize, (0 + 1) * cellSize, (2 + 1) * cellSize, (0 + 1) * cellSize, wallPaint);
        //cell 3;0
        canvas.drawLine((3 + 1) * cellSize, (0 + 1) * cellSize, (3 + 1)* cellSize, 0 * cellSize, wallPaint );
        //cell 1;1
        canvas.drawLine(1 * cellSize, (1 + 1) * cellSize, (1 + 1) * cellSize, (1 + 1) * cellSize, wallPaint);
        canvas.drawLine((1 + 1) * cellSize, (1 + 1) * cellSize, (1 + 1)* cellSize, 1 * cellSize, wallPaint );
        //cell 2;1
        canvas.drawLine((2 + 1) * cellSize, (1 + 1) * cellSize, (2 + 1)* cellSize, 1 * cellSize, wallPaint );
        //cell 3;1
        canvas.drawLine(3 * cellSize, (1 + 1) * cellSize, (3 + 1) * cellSize, (1 + 1) * cellSize, wallPaint);
        canvas.drawLine((3 + 1) * cellSize, (1 + 1) * cellSize, (3 + 1)* cellSize, 1 * cellSize, wallPaint );
        //cell 4;1
        canvas.drawLine((4 + 1) * cellSize, (1 + 1) * cellSize, (4 + 1)* cellSize, 1 * cellSize, wallPaint );
        //cell 0;2
        canvas.drawLine(0 * cellSize, (2 + 1) * cellSize, (0 + 1) * cellSize, (2 + 1) * cellSize, wallPaint);
        //cell 1;2
        canvas.drawLine((1 + 1) * cellSize, (2 + 1) * cellSize, (1 + 1)* cellSize, 2 * cellSize, wallPaint );
        //cell 2;2
        canvas.drawLine(2 * cellSize, (2 + 1) * cellSize, (2 + 1) * cellSize, (2 + 1) * cellSize, wallPaint);
        //cell 1;3
        canvas.drawLine(1 * cellSize, (3 + 1) * cellSize, (1 + 1) * cellSize, (3 + 1) * cellSize, wallPaint);
        canvas.drawLine((1 + 1) * cellSize, (3 + 1) * cellSize, (1 + 1)* cellSize, 3 * cellSize, wallPaint );
        //cell 3;3
        canvas.drawLine(3 * cellSize, (3 + 1) * cellSize, (3 + 1) * cellSize, (3 + 1) * cellSize, wallPaint);
        //cell 4;3
        canvas.drawLine(4 * cellSize, (3 + 1) * cellSize, (4 + 1) * cellSize, (3 + 1) * cellSize, wallPaint);
        canvas.drawLine((4 + 1) * cellSize, (3 + 1) * cellSize, (4 + 1)* cellSize, 3 * cellSize, wallPaint );
        //cell 2;4
        canvas.drawLine(2 * cellSize, (4 + 1) * cellSize, (2 + 1) * cellSize, (4 + 1) * cellSize, wallPaint);
        canvas.drawLine((2 + 1) * cellSize, (4 + 1) * cellSize, (2 + 1)* cellSize, 4 * cellSize, wallPaint );
        //cell 0;5
        canvas.drawLine((0 + 1) * cellSize, (5 + 1) * cellSize, (0 + 1)* cellSize, 5 * cellSize, wallPaint );
        //cell 1;5
        canvas.drawLine((1 + 1) * cellSize, (5 + 1) * cellSize, (1 + 1)* cellSize, 5 * cellSize, wallPaint );
        //cell 3;5
        canvas.drawLine(3 * cellSize, (5 + 1) * cellSize, (3 + 1) * cellSize, (5 + 1) * cellSize, wallPaint);
        //cell 4;5
        canvas.drawLine((4 + 1) * cellSize, (5 + 1) * cellSize, (4 + 1)* cellSize, 5 * cellSize, wallPaint );
        //cell 0;6
        canvas.drawLine((0 + 1) * cellSize, (6 + 1) * cellSize, (0 + 1)* cellSize, 6 * cellSize, wallPaint );
        canvas.drawLine(0 * cellSize, (6 + 1) * cellSize, (0 + 1) * cellSize, (6 + 1) * cellSize, wallPaint);
        //cell 2;6
        canvas.drawLine((2 + 1) * cellSize, (6 + 1) * cellSize, (2 + 1)* cellSize, 6 * cellSize, wallPaint );
        canvas.drawLine(2 * cellSize, (6 + 1) * cellSize, (2 + 1) * cellSize, (6 + 1) * cellSize, wallPaint);
        //cell 3;6
        canvas.drawLine((3 + 1) * cellSize, (6 + 1) * cellSize, (3 + 1)* cellSize, 6 * cellSize, wallPaint );
        //cell 3;8
        canvas.drawLine((3 + 1) * cellSize, (8 + 1) * cellSize, (3 + 1)* cellSize, 8 * cellSize, wallPaint );
        //cell 4;6
        canvas.drawLine(4 * cellSize, (6 + 1) * cellSize, (4 + 1) * cellSize, (6 + 1) * cellSize, wallPaint);
        //cell 0;7
        canvas.drawLine((0 + 1) * cellSize, (7 + 1) * cellSize, (0 + 1)* cellSize, 7 * cellSize, wallPaint );
        //cell 1;7
        canvas.drawLine((1 + 1) * cellSize, (7 + 1) * cellSize, (1 + 1)* cellSize, 7 * cellSize, wallPaint );
        //cell 2;7
        canvas.drawLine(2 * cellSize, 7 * cellSize, (2 + 1)* cellSize, 7 * cellSize, wallPaint );
        canvas.drawLine((2 + 1) * cellSize, (7 + 1) * cellSize, (2 + 1)* cellSize, 7 * cellSize, wallPaint );
        //cell 4;7
        canvas.drawLine((4 + 1) * cellSize, (7 + 1) * cellSize, (4 + 1)* cellSize, 7 * cellSize, wallPaint );
        canvas.drawLine(4 * cellSize, (7 + 1) * cellSize, (4 + 1) * cellSize, (7 + 1) * cellSize, wallPaint);
        //cell 2;8
        canvas.drawLine((2 + 1) * cellSize, (8 + 1) * cellSize, (2 + 1)* cellSize, 8 * cellSize, wallPaint );
    }


    public void update() {
        if(!checkMaze()){
            player.update(joystick);
            Log.i("pos", player.positionX + "," + player.positionY);
        }
        else{
            player.setPosition(player.positionX, player.positionY, joystick );
        }
        joystick.update();
    }
}
