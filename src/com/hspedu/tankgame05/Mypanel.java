package com.hspedu.tankgame05;


import com.hspedu.tankgame05.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

public class Mypanel extends JPanel implements KeyListener,Runnable{
    Hero hero=null;
    Vector<EnemyTank> enemyTanks=new Vector<>();
    Vector<Bomb> bombs=new Vector<>();
    Vector<Node> nodes=new Vector<>();
    int enemyTankSize=4;
    //定义三张炸弹图片
    Image image1=null;
    Image image2=null;
    Image image3=null;
    public Mypanel(String key){
        File file = new File(Recorder.getRecordFile());
        if (file.exists()) {
            nodes = Recorder.getNodesAndEnemy();
        }else {
            System.out.println("文件不存在，只能开启新游戏");
            key="1";
        }
        //将MyPanel对象的 enemyTanks 设置给Recorder
        Recorder.setEnemyTanks(enemyTanks);
        hero= new Hero(500,100);
        switch (key){
            case "1":
                for (int i = 0; i < enemyTankSize; i++) {
                    EnemyTank enemyTank = new EnemyTank(100 * (i + 1), 0);
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirection(2);
                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirection());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                Recorder.setAllEnemyTankNum(0);
                break;
            case "2":
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
                    enemyTank.setEnemyTanks(enemyTanks);
                    enemyTank.setDirection(node.getDirect());
                    new Thread(enemyTank).start();
                    Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirection());
                    enemyTank.shots.add(shot);
                    new Thread(shot).start();
                    enemyTanks.add(enemyTank);
                }
                break;
            default:
                System.out.println("你的输入有误");
        }
        image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
        image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
        image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));
        //播放音乐
        new AePlayWave("src\\111.wav").start();

    }
    //显示击毁敌方坦克数
    public void showInfo(Graphics g){
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);
        g.drawString("您累积击毁敌方坦克",1020,30);
        drawTank(1020,60,g,0,0);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getAllEnemyTankNum()+"",1080,100);

    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0,0,1000,750);
        showInfo(g);
        if (hero!=null&&hero.isLive) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirection(), 1);
        }
        //画出hero射击的子弹
//        if(hero.shot!=null&&hero.shot.isLive!=false){
//            System.out.println("子弹被绘制。。。");
//            g.draw3DRect(hero.shot.x,hero.shot.y,2,2,false);
//        }
        //将hero的子弹集合 shots，遍历取出绘制
        for (int i = 0; i < hero.shots.size(); i++) {
            Shot shot = hero.shots.get(i);
            if(shot!=null&&shot.isLive!=false){
                g.draw3DRect(shot.x,shot.y,2,2,false);
            }else {//如果该shot对象已经无效，就从shots集合中拿掉
                hero.shots.remove(shot);
            }
        }

        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            if(bomb.life>6){
                g.drawImage(image1,bomb.x,bomb.y,60,60,this);
            }else if (bomb.life>3){
                g.drawImage(image2,bomb.x,bomb.y,60,60,this);
            }else {
                g.drawImage(image3,bomb.x,bomb.y,60,60,this);
            }
            bomb.lifeDown();
            if(bomb.life==0){
                bombs.remove(bomb);
            }
        }
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            if(enemyTank.isLive) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirection(), 0);
                for (int j = 0; j < enemyTank.shots.size(); j++) {
                    Shot shot = enemyTank.shots.get(j);
                    if (shot.isLive) {
                        g.draw3DRect(shot.x, shot.y, 1, 1, false);
                    } else {
                        enemyTank.shots.remove(shot);
                    }
                }
            }
        }


    }
    public void drawTank(int x,int y,Graphics g,int direct,int type){
        switch (type){
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.yellow);
                break;
        }
        switch (direct){
            case 0://向上
                g.fill3DRect(x,y,10,60,false);
                g.fill3DRect(x+30,y,10,60,false);
                g.fill3DRect(x+10,y+10,20,40,false);
                g.fillOval(x+10,y+20,20,20);
                g.drawLine(x+20,y+30,x+20,y);
                break;
            case 1://向右
                g.fill3DRect(x,y,60,10,false);
                g.fill3DRect(x,y+30,60,10,false);
                g.fill3DRect(x+10,y+10,40,20,false);
                g.fillOval(x+20,y+10,20,20);
                g.drawLine(x+30,y+20,x+60,y+20);
                break;
            case 2://向下
                g.fill3DRect(x,y,10,60,false);
                g.fill3DRect(x+30,y,10,60,false);
                g.fill3DRect(x+10,y+10,20,40,false);
                g.fillOval(x+10,y+20,20,20);
                g.drawLine(x+20,y+30,x+20,y+60);
                break;
            case 3://向左
                g.fill3DRect(x,y,60,10,false);
                g.fill3DRect(x,y+30,60,10,false);
                g.fill3DRect(x+10,y+10,40,20,false);
                g.fillOval(x+20,y+10,20,20);
                g.drawLine(x+30,y+20,x,y+20);
                break;
            default:
                System.out.println("暂时没有处理");
        }
    }
    //所有的子弹都取出与敌人的坦克进行判断
    public void hitEnemyTank(){
        //遍历我们的子弹
        for (int j = 0; j < hero.shots.size(); j++) {
            Shot shot = hero.shots.get(j);
            if (shot!=null&&shot.isLive){
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hitTank(hero.shot,enemyTank);
                }
            }
        }
    }
    public void hitHero(){
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            for (int j = 0; j < enemyTank.shots.size(); j++) {
                Shot shot = enemyTank.shots.get(j);
                if (hero.isLive&&shot.isLive){
                    hitTank(shot,hero);
                }
            }
        }
    }
    //产生爆炸效果
    public  void hitTank(Shot s, Tank enemyTank){
        switch (enemyTank.getDirection()){
            case 0:
            case 2:
                if (s.x>enemyTank.getX()&&s.x<enemyTank.getX()+40
                        &&s.y> enemyTank.getY()&&s.y<enemyTank.getY()+60){
                    s.isLive=false;
                    enemyTank.isLive=false;
                    enemyTanks.remove(enemyTank);
                    if (enemyTank instanceof EnemyTank){
                        Recorder.addAllEnemyTankNum();
                    }
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1:
            case 3:
                if (s.x>enemyTank.getX()&&s.x<enemyTank.getX()+60
                        &&s.y> enemyTank.getY()&&s.y<enemyTank.getY()+40){
                    s.isLive=false;
                    enemyTank.isLive=false;
                    enemyTank.isLive=false;
                    enemyTanks.remove(enemyTank);
                    if (enemyTank instanceof EnemyTank){
                        Recorder.addAllEnemyTankNum();
                    }
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_W){
            hero.setDirection(0);
            if (hero.getY()>0) {
                hero.moveUp();
            }
        }else if(e.getKeyCode()==KeyEvent.VK_D){
            hero.setDirection(1);
            if (hero.getX()+60<1000) {
                hero.moveRight();
            }
        }else if(e.getKeyCode()==KeyEvent.VK_A){
            hero.setDirection(3);
            if (hero.getX()>0) {
                hero.moveLeft();
            }
        }else if (e.getKeyCode()==KeyEvent.VK_S){
            hero.setDirection(2);
            if (hero.getY()+60<750) {
                hero.moveDown();
            }
        }
        if(e.getKeyCode()==KeyEvent.VK_J){
//            //判断hero的子弹是否销毁，发射一颗子弹
//            if(hero.shot==null||!hero.shot.isLive) {
//                hero.shotEnemyTank();
//            }
            //发射多颗子弹
            hero.shotEnemyTank();
        }
        repaint();

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            if (hero.shot!=null&&hero.shot.isLive){
//                for (int i = 0; i < enemyTanks.size(); i++) {
//                    EnemyTank enemyTank = enemyTanks.get(i);
//                    hitTank(hero.shot,enemyTank);
//                }
//            }
            hitEnemyTank();
            hitHero();
            this.repaint();
        }
    }
}
