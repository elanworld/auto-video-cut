package com.alan.paint;

import javax.swing.JFrame;
import java.awt.Panel;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

public class Graph {
    public Graph(ArrayList<Integer> graphNum) {
        new Frame(graphNum);
    }

}

class Frame extends JFrame {
    public Frame(ArrayList<Integer> graphNum) {
        super();
        setVisible(true);
        setSize(500, 500);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Comp panmel = new Comp(graphNum);
        add(panmel);
    }
}

class Comp extends Panel {
    ArrayList<Integer> list;

    public Comp(ArrayList<Integer> graphNum) {
        list = graphNum;
    }

    public void paint(Graphics g) {
        super.paint(g);
        int xDuration = getSize().width / (list.size() - 1);
        double yPercent = getSize().height / Collections.max(list);
        int x = 0;
        for (int i = 0; i < list.size() - 1; i += 1) {
            double y1 = getSize().height - list.get(i) * yPercent;
            double y2 = getSize().height - list.get(i + 1) * yPercent;
            g.drawLine(x, (int) y1, x + xDuration, (int) y2);
            x += xDuration;
        }
    }
}