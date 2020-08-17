package com.alan.video;

import com.alan.cmd.FFmpegCmd;
import org.bytedeco.opencv.presets.opencv_core;
import org.bytedeco.tesseract.TFile;

public class VideoInstance {
    public void crop () {
        String file = "F:\\Alan\\Videos\\Mine\\New Best Zach King Magic.mp4";
        String output = "F:\\Alan\\Videos\\Mine\\New.mp4";
        FFmpegCmd fFmpegCmd = new FFmpegCmd();
        fFmpegCmd.setInput(file).setOutput(output).setCrop(0.5625,1);
        fFmpegCmd.run();
    }

    public static void main(String[] args) {
        VideoInstance videoInstance = new VideoInstance();
        videoInstance.crop();
    }
}
