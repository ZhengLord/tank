package com.hspedu.tankgame05;

public class Bomb {
    int x;
    int y;
    boolean isLive=true;
    int life=9;

    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void lifeDown(){
        if(life>0){
            life--;
        }else {
            isLive=false;
        }
    }
}
