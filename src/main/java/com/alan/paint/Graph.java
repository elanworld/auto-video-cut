package com.alan.paint;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Panel;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Graph {
    public static void main(String args[]) {
//        new NumToLine();
        new Frame();
    }
}

class Frame extends JFrame {
    public Frame() {
        super();
        setVisible(true);
        setSize(500, 500);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Comp());
    }
}

class Comp extends Panel {
    public void paint(Graphics g) {
        super.paint(g);
        ArrayList<Integer> list = new NumToLine().getNumbers();
        for (int i=0;i<=list.size()-4;i=i+2) {
            int x1 = list.get(i);
            int y1 = list.get(i+1);
            int x2 = list.get(i+2);
            int y2 = list.get(i+3);
            try {
                Thread.sleep(100);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            g.drawLine(x1,y1,x2,y2);
        }

    }
}

class NumToLine {
    public int x =0;
    ArrayList<Integer> list = new ArrayList<>();

    public NumToLine() {
        Integer[] numbers = {80, 20,52,66,68,33,43,22,23,89};
//        int max = (int) Collections.max(Arrays.asList(numbers));
//        System.out.println(max);
        for (Integer n : numbers) {
            list.add(x);
            list.add(n);
            x = x + 100;
        }
        System.out.println(list);
    }
    public ArrayList<Integer> getNumbers() {
        return list;
    }
}