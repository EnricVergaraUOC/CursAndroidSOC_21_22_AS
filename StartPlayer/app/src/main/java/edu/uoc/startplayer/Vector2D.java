package edu.uoc.startplayer;

import android.graphics.Color;

public class Vector2D {
    int posX;
    int posY;
    int ID;
    int color;
    Vector2D(int posX, int posY, int ID, int color){
        this.posX = posX;
        this.posY = posY;
        this.ID = ID;
        this.color = color;
    }
    Vector2D(){
        this.posX = 0;
        this.posY = 0;
    }
}
