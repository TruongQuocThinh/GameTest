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
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.gametest.graphics.Animator;
import com.example.gametest.graphics.SpriteSheet;

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
    private int width ,height;
    private int c, r;
    private int[][] map;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);

        //player and joystick, cell, hint
        wallPaint = new Paint();
        wallPaint.setColor(Color.WHITE);
        wallPaint.setStrokeWidth(WALL_THICKNESS);
        joystick = new Joystick(525, 1950, 70, 30);
        SpriteSheet spriteSheet = new SpriteSheet(context);
        Animator animator = new Animator(spriteSheet.getPlayerSpriteArray());
        player = new Player(getContext(), 185, 825, 35, animator);
        hint = new Hint( getContext(), 725, 1210, 35);
        setFocusable(true);

        this.c = 0;
        this.r = 0;

        createMaze();
    }

    private void setMap() {
        map = new int[COLS][ROWS];

        map[0][0] = 842; map[1][0] = 8; map[2][0] = 82; map[3][0] = 86; map[4][0] = 486; //0
        map[0][1] = 84; map[1][1] = 62; map[2][1] = 486; map[3][1] = 426; map[4][1] = 46; //1
        map[0][2] = 42; map[1][2] = 86; map[2][2] = 42; map[3][2] = 8; map[4][2] = 6; //2
        map[0][3] = 48; map[1][3] = 26; map[2][3] = 48; map[3][3] = 2; map[4][3] = 26; //3
        map[0][4] = 4; map[1][4] = 8; map[2][4] = 26; map[3][4] = 48; map[4][4] = 86; //4
        map[0][5] = 46; map[1][5] = 46; map[2][5] = 48; map[3][5] = 2; map[4][5] = 6; //5
        map[0][6] = 46; map[1][6] = 4; map[2][6] = 26; map[3][6] = 486; map[4][6] = 426; //6
        map[0][7] = 486; map[1][7] = 46; map[2][7] = 486; map[3][7] = 4; map[4][7] = 286; //7
        map[0][8] = 4; map[1][8] = 0; map[2][8] = 6; map[3][8] = 46; map[4][8] = 486; //8
        map[0][9] = 42; map[1][9] = 26; map[2][9] = 42; map[3][9] = 2; map[4][9] = 26; //9
    }

    private void createMaze() {
        cells = new Cell[COLS][ROWS];

        for(int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
        setMap();
    }

    private boolean checkTop(int x, int y){
        if(cells[x][y].topWall){
            if(!cells[x][y].checkCollision(x * cellSize + hMargin, y * cellSize + vMargin, (x + 1)* cellSize + hMargin, y * cellSize + vMargin,player.positionX, player.positionY, player.radius + 12)){
                return false;
            }
            else{
                return true;
            }
        }
        return false;
    }
    private boolean checkLeft(int x, int y){
        if(cells[x][y].leftWall){
            if(!cells[x][y].checkCollision(x * cellSize + hMargin, y * cellSize + vMargin, x * cellSize + hMargin, (y + 1) * cellSize + vMargin,player.positionX, player.positionY, player.radius + 12)){
                return false;
            }
            else{
                return true;
            }
        }
        return false;
    }
    private boolean checkBottom(int x, int y){
        if(cells[x][y].bottomWall){
            if(!cells[x][y].checkCollision(x * cellSize + hMargin, (y + 1) * cellSize + vMargin, (x+1)* cellSize + hMargin, (y + 1) * cellSize + vMargin,player.positionX, player.positionY, player.radius + 12)){
                return false;
            }
            else{
                return true;
            }
        }
        return false;
    }
    private boolean checkRight(int x, int y){
        if(cells[x][y].rightWall){
            if(!cells[x][y].checkCollision((x + 1) * cellSize + hMargin, (y + 1) * cellSize + vMargin, (x + 1)* cellSize + hMargin, y * cellSize + vMargin,player.positionX, player.positionY, player.radius + 12)){
                return false;
            }
            else{
                return true;
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

        width = getWidth();
        height = getHeight();

        if(width/height < COLS/ROWS){
            cellSize = width / (COLS + 1);
        }
        else{
            cellSize = height / (ROWS + 1);
        }
        hMargin = (width - COLS*cellSize)/2;
        vMargin = (float) ((height - ROWS*cellSize)/10);

        canvas.translate(hMargin, vMargin);

        if (c < COLS && r < ROWS) {
            for (int x = 0; x < COLS; x++) {
                for (int y = 0; y < ROWS; y++) {
                    if ((x == c) && (y == r)) {
                        for (int i = x - 1; i <= x + 1; i++) {
                            for (int j = y - 1; j <= y +1; j++) {
                                if (i < 0) {
                                    continue;
                                }
                                if (j < 0) {
                                    continue;
                                }
                                if (i >= COLS) {
                                    continue;
                                }
                                if (j >= ROWS) {
                                    continue;
                                }
                                if (map[i][j] == 0) {
                                    continue;
                                }
                                if (String.valueOf(map[i][j]).contains("2")) {
                                    canvas.drawLine(i * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, (j + 1) * cellSize, wallPaint);
                                    cells[i][j].setBottomWall(true);
                                }
                                if (String.valueOf(map[i][j]).contains("4")) {
                                    canvas.drawLine(i * cellSize, j * cellSize, i * cellSize, (j + 1) * cellSize, wallPaint);
                                    cells[i][j].setLeftWall(true);
                                }
                                if (String.valueOf(map[i][j]).contains("6")) {
                                    canvas.drawLine((i + 1) * cellSize, (j + 1) * cellSize, (i + 1) * cellSize, j * cellSize, wallPaint);
                                    cells[i][j].setRightWall(true);
                                }
                                if (String.valueOf(map[i][j]).contains("8")) {
                                    canvas.drawLine(i * cellSize, j * cellSize, (i + 1) * cellSize, j * cellSize, wallPaint);
                                    cells[i][j].setTopWall(true);
                                }
                            }
                        }
                    }
                }
            }
        }
        /*for(int x = 0; x < COLS; x++ ){
            //vien tren
            canvas.drawLine(x * cellSize, 0 * cellSize, (x + 1)* cellSize, 0 * cellSize, wallPaint );
            cells[x][0].setTopWall(true);
            //vien duoi
            canvas.drawLine(x * cellSize, (ROWS - 1 + 1) * cellSize, (x+1)* cellSize, (ROWS - 1 + 1) * cellSize, wallPaint );
            cells[x][ROWS - 1].setBottomWall(true);
        }

        for(int y = 0; y < ROWS; y++) {
            //vien trai
            canvas.drawLine(0 * cellSize, y * cellSize, 0 * cellSize, (y + 1) * cellSize, wallPaint);
            cells[0][y].setLeftWall(true);
            //vien phai
            canvas.drawLine((COLS - 1 + 1) * cellSize, (y + 1) * cellSize, (COLS - 1 + 1) * cellSize, y * cellSize, wallPaint);
            cells[COLS - 1][y].setRightWall(true);
        }
        //cell 0;0
        canvas.drawLine(0 * cellSize, (0 + 1) * cellSize, (0 + 1) * cellSize, (0 + 1) * cellSize, wallPaint);
        cells[0][0].setBottomWall(true);
        //cell 0;1
        cells[0][1].setTopWall(true);
        //cell 0;3
        cells[0][3].setTopWall(true);
        //cell 2;0
        canvas.drawLine(2 * cellSize, (0 + 1) * cellSize, (2 + 1) * cellSize, (0 + 1) * cellSize, wallPaint);
        cells[2][0].setBottomWall(true);
        //cell 2;1
        cells[2][1].setLeftWall(true);
        //cell 2;3
        cells[2][3].setLeftWall(true);
        cells[2][3].setTopWall(true);
        //cell 3;0
        canvas.drawLine((3 + 1) * cellSize, (0 + 1) * cellSize, (3 + 1)* cellSize, 0 * cellSize, wallPaint );
        cells[3][0].setRightWall(true);
        //cell 4:0
        cells[4][0].setLeftWall(true);
        //cell 1:0
        cells[1][0].setTopWall(true);
        //cell 1;1
        canvas.drawLine(1 * cellSize, (1 + 1) * cellSize, (1 + 1) * cellSize, (1 + 1) * cellSize, wallPaint);
        cells[1][1].setBottomWall(true);
        canvas.drawLine((1 + 1) * cellSize, (1 + 1) * cellSize, (1 + 1)* cellSize, 1 * cellSize, wallPaint );
        cells[1][1].setRightWall(true);
        //cell 2;1
        canvas.drawLine((2 + 1) * cellSize, (1 + 1) * cellSize, (2 + 1)* cellSize, 1 * cellSize, wallPaint );
        cells[2][1].setRightWall(true);
        cells[2][1].setTopWall(true);
        //cell 3;1
        canvas.drawLine(3 * cellSize, (1 + 1) * cellSize, (3 + 1) * cellSize, (1 + 1) * cellSize, wallPaint);
        cells[3][1].setBottomWall(true);
        canvas.drawLine((3 + 1) * cellSize, (1 + 1) * cellSize, (3 + 1)* cellSize, 1 * cellSize, wallPaint );
        cells[3][1].setRightWall(true);
        cells[3][1].setLeftWall(true);
        //cell 4:1
        cells[4][1].setLeftWall(true);
        //cell 0;2
        canvas.drawLine(0 * cellSize, (2 + 1) * cellSize, (0 + 1) * cellSize, (2 + 1) * cellSize, wallPaint);
        cells[0][2].setBottomWall(true);
        //cell 1;2
        canvas.drawLine((1 + 1) * cellSize, (2 + 1) * cellSize, (1 + 1)* cellSize, 2 * cellSize, wallPaint );
        cells[1][2].setRightWall(true);
        cells[1][2].setTopWall(true);
        //cell 2;2
        canvas.drawLine(2 * cellSize, (2 + 1) * cellSize, (2 + 1) * cellSize, (2 + 1) * cellSize, wallPaint);
        cells[2][2].setBottomWall(true);
        cells[2][2].setLeftWall(true);
        //cell 3:2
        cells[3][2].setTopWall(true);
        //cell 1;4
        cells[1][4].setTopWall(true);
        //cell 2;4
        canvas.drawLine(2 * cellSize, (4 + 1) * cellSize, (2 + 1) * cellSize, (4 + 1) * cellSize, wallPaint);
        cells[2][4].setRightWall(true);
        canvas.drawLine((2 + 1) * cellSize, (4 + 1) * cellSize, (2 + 1)* cellSize, 4 * cellSize, wallPaint );
        cells[2][4].setBottomWall(true);
        //cell 3;4
        cells[3][4].setTopWall(true);
        //cell 4;4
        cells[4][4].setTopWall(true);
        //cell 1;3
        canvas.drawLine(1 * cellSize, (3 + 1) * cellSize, (1 + 1) * cellSize, (3 + 1) * cellSize, wallPaint);
        cells[1][3].setRightWall(true);
        canvas.drawLine((1 + 1) * cellSize, (3 + 1) * cellSize, (1 + 1)* cellSize, 3 * cellSize, wallPaint );
        cells[1][3].setBottomWall(true);
        //cell 3;3
        canvas.drawLine(3 * cellSize, (3 + 1) * cellSize, (3 + 1) * cellSize, (3 + 1) * cellSize, wallPaint);
        cells[3][3].setBottomWall(true);
        //cell 4;3
        canvas.drawLine(4 * cellSize, (3 + 1) * cellSize, (4 + 1) * cellSize, (3 + 1) * cellSize, wallPaint);
        cells[4][3].setBottomWall(true);
        //cell 3;4
        cells[3][4].setLeftWall(true);
        //cell 0;5
        canvas.drawLine((0 + 1) * cellSize, (5 + 1) * cellSize, (0 + 1)* cellSize, 5 * cellSize, wallPaint );
        cells[0][5].setRightWall(true);
        //cell 1;5
        canvas.drawLine((1 + 1) * cellSize, (5 + 1) * cellSize, (1 + 1)* cellSize, 5 * cellSize, wallPaint );
        cells[1][5].setRightWall(true);
        cells[1][5].setLeftWall(true);
        //cell 2;5
        cells[2][5].setLeftWall(true);
        cells[2][5].setTopWall(true);
        //cell 3;5
        canvas.drawLine(3 * cellSize, (5 + 1) * cellSize, (3 + 1) * cellSize, (5 + 1) * cellSize, wallPaint);
        cells[3][5].setBottomWall(true);
        //cell 0;6
        canvas.drawLine((0 + 1) * cellSize, (6 + 1) * cellSize, (0 + 1)* cellSize, 6 * cellSize, wallPaint );
        cells[0][6].setRightWall(true);
        canvas.drawLine(0 * cellSize, (6 + 1) * cellSize, (0 + 1) * cellSize, (6 + 1) * cellSize, wallPaint);
        cells[0][6].setBottomWall(true);
        //cell 1;6
        cells[1][6].setLeftWall(true);
        //cell 2;6
        canvas.drawLine((2 + 1) * cellSize, (6 + 1) * cellSize, (2 + 1)* cellSize, 6 * cellSize, wallPaint );
        cells[2][6].setRightWall(true);
        canvas.drawLine(2 * cellSize, (6 + 1) * cellSize, (2 + 1) * cellSize, (6 + 1) * cellSize, wallPaint);
        cells[2][6].setBottomWall(true);
        //cell 3;6
        canvas.drawLine((3 + 1) * cellSize, (6 + 1) * cellSize, (3 + 1)* cellSize, 6 * cellSize, wallPaint );
        cells[3][6].setRightWall(true);
        cells[3][6].setTopWall(true);
        cells[3][6].setLeftWall(true);
        //cell 4;6
        canvas.drawLine(4 * cellSize, (6 + 1) * cellSize, (4 + 1) * cellSize, (6 + 1) * cellSize, wallPaint);
        cells[4][6].setBottomWall(true);
        cells[4][6].setLeftWall(true);
        //cell 0;7
        canvas.drawLine((0 + 1) * cellSize, (7 + 1) * cellSize, (0 + 1)* cellSize, 7 * cellSize, wallPaint );
        cells[0][7].setRightWall(true);
        //cell 0;7
        cells[0][7].setTopWall(true);
        //cell 1;7
        canvas.drawLine((1 + 1) * cellSize, (7 + 1) * cellSize, (1 + 1)* cellSize, 7 * cellSize, wallPaint );
        cells[1][7].setRightWall(true);
        cells[1][7].setLeftWall(true);
        //cell 2;7
        canvas.drawLine((2 + 1) * cellSize, (7 + 1) * cellSize, (2 + 1)* cellSize, 7 * cellSize, wallPaint );
        cells[2][7].setRightWall(true);
        cells[2][7].setTopWall(true);
        cells[2][7].setLeftWall(true);
        //cell 3;7
        cells[3][7].setLeftWall(true);
        //cell 4;7
        canvas.drawLine(4 * cellSize, (7 + 1) * cellSize, (4 + 1) * cellSize, (7 + 1) * cellSize, wallPaint);
        cells[4][7].setBottomWall(true);
        cells[4][7].setTopWall(true);
        //cell 2;8
        canvas.drawLine((2 + 1) * cellSize, (8 + 1) * cellSize, (2 + 1)* cellSize, 8 * cellSize, wallPaint );
        cells[2][8].setRightWall(true);
        //cell 3;8
        canvas.drawLine((3 + 1) * cellSize, (8 + 1) * cellSize, (3 + 1)* cellSize, 8 * cellSize, wallPaint );
        cells[3][8].setRightWall(true);
        cells[3][8].setLeftWall(true);
        //cell 4;8
        cells[4][8].setTopWall(true);
        cells[4][8].setLeftWall(true);*/
    }

    private void bounds(int c, int r){
        if (c < 0) {
            c = 0;
        }
        if (r < 0) {
            r = 0;
        }
        if (c > COLS - 1) {
            c = COLS - 1;
        }
        if (r > ROWS - 1) {
            r = ROWS - 1;
        }
    }

    public void update() {
//        Log.e("player", player.positionX + ", " + player.positionY);
        c = (int)Math.floor((player.positionX - hMargin)/cellSize);
        r = (int)Math.floor((player.positionY - vMargin)/cellSize);
//        Log.i("cell", "[" + c + "][" + r + "]");

        bounds(c, r);

        if (c < COLS || r < ROWS) {
            if(!checkTop(c, r) && !checkBottom(c, r) && !checkLeft(c, r) && !checkRight(c, r)){
                player.update(joystick);
                Log.i("pos:", String.valueOf(player.positionX + ", " + player.positionY));
            }
            else{
                player.setPosition(player.positionX, player.positionY, joystick);
            }
        }
        if(player.isColliding(player, hint)){
        }
        joystick.update();
    }
}
