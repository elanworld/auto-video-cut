package com.alan.video;

import com.alan.video.FFmpegCmd.FiltersSet;
import org.junit.Test;

public class FFmpegTest {
    FFmpegCmd fFmpegCmd;

    public FFmpegTest() {
        String input = "F:\\Alan\\Videos\\Mine\\与我相关\\vlog\\aa.mp4";
        String output = "F:\\Alan\\Videos\\Mine\\与我相关\\vlog\\bb.mp4";
        fFmpegCmd = new FFmpegCmd();
        fFmpegCmd.setInput(input);
        fFmpegCmd.setOutput(output);
        fFmpegCmd.setting(true, true);
    }


    public void crop() {
        fFmpegCmd.setCrop(0.2, 0.2).runCommand();
    }

    public void filter() {
        FiltersSet filtersSet = fFmpegCmd.new FiltersSet();
        filtersSet.setCrop(0.8,1);
        filtersSet.setBoxblur(1500,1500).toFFmpeg().runCommand();
    }

    @Test
    public void qsv() {
        fFmpegCmd.new SpecialFormat().setCodecQSV();
        fFmpegCmd.runCommand();
    }

}
