package com.hspedu.tankgame05;

import java.util.Vector;

public class Hero extends Tank {
    //定义一个shot对象，表示一个射击（线程）
    Shot shot=null;
    //可以发射多颗子弹
    Vector<Shot> shots=new Vector<>();
    public Hero(int x, int y) {
        super(x, y);
    }
    public void shotEnemyTank(){
        //控制发射子弹的数量，最多五颗一次
        if(shots.size()==5)return;
        switch (getDirection()){
            case 0:
                shot=new Shot(getX()+20,getY(),0);
                break;
            case 1:
                shot=new Shot(getX()+60,getY()+20,1);
                break;
            case 2:
                shot=new Shot(getX()+20,getY()+60,2);
                break;
            case 3:
                shot=new Shot(getX(),getY()+20,3);
                break;
        }
        shots.add(shot);
        Thread thread = new Thread(shot);
        thread.start();
    }
}
