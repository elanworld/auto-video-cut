package com.alan.util;


public class Output {
    private static boolean show = true;

    public <E> Output(E out) {
        if (true) {
            System.out.println(out);
        }
    }

    public static <E> void print(E out) {
        if (show) {
            System.out.println(out);
        }
    }

    public static void setShow(boolean show) {
        Output.show = show;
    }
}
