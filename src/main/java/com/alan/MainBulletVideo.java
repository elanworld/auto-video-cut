package com.alan;

import com.alan.text.BulletApply;

public class MainBulletVideo {
    public static void main(String[] args) {
        BulletApply apply = new BulletApply();
        apply.addVideoDir("G:\\Alan\\Documents\\Git\\webvideo-downloader\\videos");
        apply.setOutDir("G:\\Alan\\Documents\\Git\\webvideo-downloader\\videos");
        apply.setKeyWord("奶奶");
        apply.setApply();
        apply.run();
    }
}
