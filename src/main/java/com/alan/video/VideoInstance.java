package com.alan.video;

public class VideoInstance {
    public void crop () {
        String file = "F:\\Alan\\Videos\\Mine\\New Best Zach King Magic.mp4";
        String output = "F:\\Alan\\Videos\\Mine\\New.mp4";
        FFmpegCmd fFmpegCmd = new FFmpegCmd();
        fFmpegCmd.setInput(file).setOutput(output).setCrop(0.5625,1);
        fFmpegCmd.runCommand();
    }

    public static void main(String[] args) {
        VideoInstance videoInstance = new VideoInstance();
        videoInstance.crop();
    }
}
