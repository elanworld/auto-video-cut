package com.alan.video;

import com.alan.video.FFmpegCmd.FiltersSet;

public class FFmpegCmdTest {
    String input = "F:\\Alan\\Videos\\Mine\\aa.mp4";
    String output = "F:\\Alan\\Videos\\Mine\\bb.mp4";
    String sub = "F:\\Alan\\Videos\\Mine\\my.srt";
    FFmpegCmd fFmpegCmd = new FFmpegCmd();

    public void filter() {
        FiltersSet filtersSet = fFmpegCmd.new FiltersSet();
        filtersSet.setCrop(0.8, 1);
        filtersSet.toFFmpegCmd().getFiltersSet().setBoxblur(1920, 1280).toFFmpegCmd().run();
    }


    public void qsv() {
        fFmpegCmd.setCodecQSV();
        fFmpegCmd.run();
    }

    public void crop() {
        String file = "F:\\Alan\\Videos\\Mine\\New Best Zach King Magic.mp4";
        String output = "F:\\Alan\\Videos\\Mine\\New.mp4";
        FFmpegCmd fFmpegCmd = new FFmpegCmd();
        fFmpegCmd.setInput(file).setOutput(output).setCrop(0.5625, 1);
        fFmpegCmd.run();
    }


    public void subtitle() {
        fFmpegCmd.setInput(input).setOutput(output).getFiltersSet().setSubtitle(sub).toFFmpegCmd().run();
    }


}
