package edu.uoc.startplayer;

public class Vector2D {
    private int posX;
    private int posY;
    Vector2D(int posX, int posY){
        this.posX = posX;
        this.posY = posY;
    }
    Vector2D(){
        this.posX = 0;
        this.posY = 0;
    }

    int GetPosX(){ return posX;}
    int GetPosY(){ return posY;}
    void SetPosX(int x){ posX = x;}
    void SetPosY(int y){ posY = y;}

}
