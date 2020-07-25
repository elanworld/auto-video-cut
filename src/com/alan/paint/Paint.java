package com.alan.paint;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class Paint extends JFrame{

    public Paint() {
        super();
        add(new mycanvas());
    }
    public static void main(String[] args) {
        Paint paint = new Paint();
        paint.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        paint.setVisible(true);


    }
    private class mycanvas extends Panel {

        public  void paint(Graphics g) {
            // TODO Auto-generated method stub
            Graphics2D g2 = (Graphics2D) g;
            g2.drawOval(5, 5, 100, 100);
            g2.fillRect(15, 15, 80, 80);

            Shape shape1 = new Rectangle2D.Double(100, 5, 100, 100);//矩形对象
            g2.fill(shape1);

            int x[]={250,300,250,300};
            int y[]={130,130,200,200};
            g2.drawPolygon(x,y,4);//多边形  第三个参数是边数 第一二个参数是 边数横，纵坐标



        }

    }
}