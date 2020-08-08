package com.alan.util;

public class CaptionException extends Exception {
    String ex;

    public CaptionException(String error) {
        ex = error;
    }
    @Override
    public void printStackTrace() {
        System.out.println("My Exception: " + ex);
        super.printStackTrace();
    }
}