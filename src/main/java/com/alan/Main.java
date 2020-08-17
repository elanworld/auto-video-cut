package com.alan;

import com.alan.audio.AudioContainer;
import com.alan.video.OpenCvBox;

public class Main {
    public static void main(String[] args) {
        String wav = "F:\\Alan\\Videos\\µÁ”∞\\audio_read.wav";
        AudioContainer audioContainer = new AudioContainer();
        audioContainer.loadAudio(wav);
        System.exit(0);
        String file = "F:\\Alan\\Videos\\Mine\\New.mp4";
        OpenCvBox openCvBox = new OpenCvBox(file);
    }
}
