package com.example.gametest;

import android.graphics.Canvas;

public class Cell {
    public boolean topWall = true;
    public boolean leftWall = true;
    public boolean bottomWall = true;
    public boolean rightWall = true;
    public int col;
    public int row;

    public Cell(int col, int row){
        this.col = col;
        this.row = row;
    }
}
