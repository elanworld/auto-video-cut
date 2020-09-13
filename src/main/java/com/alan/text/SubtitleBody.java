package com.alan.text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubtitleBody implements Serializable, Cloneable {
    int num;
    double start;
    double end;
    List<String> text;

    public SubtitleBody() {
        init();
    }

    public void init() {
        num = 0;
        start = 0;
        end = 0;
        text = new ArrayList<>();
    }

    @Override
    public SubtitleBody clone() {
        SubtitleBody sub = null;
        try {
            sub = (SubtitleBody) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return sub;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public List<String> getText() {
        return text;
    }

    public void addText(String text) {
        if (!text.equals("\n"))
            this.text.add(text);
    }

    private String computeTime(double time) {
        int h = (int) (time / 3600);
        double mod = time - h * 3600;
        int m = (int) (mod / 60);
        double mod1 = mod - m * 60;
        int s = (int) mod1;
        double ms = (int) ((mod1 - s) * 100);
        return String.format("%02d:%02d:%02d,%03d", h, m, s, (int) ms);
    }

    public String toBody() {
        String txt = String.join("\n",text);
        String start = computeTime(this.start);
        String end = computeTime(this.end);
        return String.format("%d\n" +
                "%s --> %s\n" +
                "%s\n\n", num, start, end, txt);
    }

    @Override
    public String toString() {
        return "SubtitleBody{" +
                "num=" + num +
                ", start=" + start +
                ", end=" + end +
                ", english='" + text + '\'' +
                '}';
    }
}
