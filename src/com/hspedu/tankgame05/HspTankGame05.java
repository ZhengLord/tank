package com.hspedu.tankgame05;


import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

public class HspTankGame05 extends JFrame {
    Mypanel mp=null;
    public static void main(String[] args) {
        HspTankGame05 hspTankGame04 = new HspTankGame05();
    }
    public HspTankGame05(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入选择 1：新游戏 2：继续上局");
        String key=scanner.next();
        mp=new Mypanel(key);
        this.add(mp);
        Thread thread = new Thread(mp);
        thread.start();
        this.setSize(1300,950);
        this.addKeyListener(mp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }
}
