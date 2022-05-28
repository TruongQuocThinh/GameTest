package com.example.gametest.graphics;

import android.graphics.Canvas;

import com.example.gametest.Player;
import com.example.gametest.PlayerState;

public class Animator {
    private Sprite[] playerSpriteArray;
    private int idxNotMovingFrame = 0;
    private int idxMovingFrame = 1;
    private int updatesBeforeNextMoveFrame;
    private static final int MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME = 5;

    public Animator(Sprite[] playerSpriteArray) {
        this.playerSpriteArray = playerSpriteArray;
    }


    public void draw(Canvas canvas, Player player) {
        switch (player.getPlayerState().getState()) {
            case NOT_MOVING:
                drawFrame(canvas, player, playerSpriteArray[idxNotMovingFrame]);
                break;
            case STARED_MOVING:
                updatesBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME;
                drawFrame(canvas, player, playerSpriteArray[idxMovingFrame]);
                break;
            case IS_MOVING:
                updatesBeforeNextMoveFrame--;
                if(updatesBeforeNextMoveFrame == 0) {
                    updatesBeforeNextMoveFrame = MAX_UPDATES_BEFORE_NEXT_MOVE_FRAME;
                    toggleIdxMovingFrame();
                }
                drawFrame(canvas, player, playerSpriteArray[idxMovingFrame]);
                break;
            default:
                break;
        }
    }

    private void toggleIdxMovingFrame() {
        if(idxMovingFrame == 1)
            idxMovingFrame = 2;
        else
            idxMovingFrame = 1;
    }

    public void drawFrame(Canvas canvas, Player player, Sprite sprite) {
        sprite.draw(
                canvas,
                (int) player.positionX,
                (int) player.positionY
        );
    }
}

