package com.alan.output;

public class Output {
    public <E> Output(E out) {
        if (true) {
            System.out.println(out);
        }
    }

    public static <E> void print(E out) {
        System.out.println(out);
    }

    public static void main(String args[]) {
        System.out.println("test output in static main");
    }
}
