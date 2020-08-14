package com.alan.util;


public class Output {
    private static boolean show = true;

    public <E> Output(E out) {
        if (show) {
            System.out.println(out);
        }
    }

    public static <E> void print(E... objects) {
        if (show) {
            for (E object : objects) {
                System.out.print(object + " ");
            }
            System.out.println();
        }
    }

    public static void setShow(boolean show) {
        Output.show = show;
    }
}
