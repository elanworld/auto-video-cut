package com.alan.video;

import com.alan.cmd.FFmpegCmd;
import com.alan.cmd.FFmpegCmd.FiltersSet;
import com.alan.util.Output;
import org.junit.Test;

public class FFmpegTest {
    FFmpegCmd fFmpegCmd;

    public FFmpegTest() {
        String input = "F:\\Alan\\Videos\\Mine\\cc.mp4";
        String output = "F:\\Alan\\Videos\\Mine\\cc1.mp4";
        fFmpegCmd = new FFmpegCmd();
        fFmpegCmd.setInput(input);
        fFmpegCmd.setOutput(output);
        fFmpegCmd.setting(true, true);
    }

    @Test
    public void crop() {
        fFmpegCmd.setCrop(0.2, 0.2).run();
    }

    @Test
    public void filter() {
        FiltersSet filtersSet = fFmpegCmd.new FiltersSet();
        filtersSet.setCrop(0.8,1);
        filtersSet.setBoxblur(1500,1500);
        fFmpegCmd.setFilter_complex(filtersSet).run();
    }

}
