package com.alan.paint;

import javax.swing.JFrame;
import java.awt.Panel;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Graph extends JFrame {
    public Graph(ArrayList<Integer> graphNum) {
        super();
        setVisible(true);
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Paint panmel = new Paint(graphNum);
        add(panmel);
    }
}

class Paint extends Panel {
    ArrayList<Integer> list;

    /**
     * paint an array numbers data in a blank panel
     * auto adjust loacation when
     * just pait positiver interger
     * @param graphNum : numbers to paint
     */
    public Paint(ArrayList<Integer> graphNum) {
        list = graphNum;
    }

    public void paint(Graphics g) {
        super.paint(g);
        int xDuration = getSize().width / (list.size() - 1);
        double yPercent = getSize().height / (double) Collections.max(list);
        int x = 0;
        for (int i = 0; i < list.size() - 1; i += 1) {
            double y1 = getSize().height - list.get(i) * yPercent;
            double y2 = getSize().height - list.get(i + 1) * yPercent;
            g.drawLine(x, (int) y1, x + xDuration, (int) y2);
            x += xDuration;
        }
    }
}