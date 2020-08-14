package com.alan;

import com.alan.video.CvContainer;
import com.alan.video.OpenCvBox;

public class Main {
    public static void main(String[] args) {
        String file = "F:\\Alan\\Videos\\Mine\\PERFECT FLASH.mp4";
        // CvContainer cvContainer = new CvContainer();
        // cvContainer.run(file);


        OpenCvBox openCvBox = new OpenCvBox(file);
    }
}
