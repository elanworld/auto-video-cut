package com.alan;

import com.alan.cmd.FFmpegCmd;
import com.alan.util.Output;
import com.alan.util.StringContainer;

public class Main {

    public static void main(String[] args) throws Exception/**/ {
        // write your cod1e here
        String file = "F:\\Alan\\Videos\\Œ“µƒ ”∆µ\\NEXUS BLITZ PENTAKILL 2020.mp4";
        FFmpegCmd fFmpegCmd = new FFmpegCmd();
        fFmpegCmd.cutCrop(file, StringContainer.getOutPath(file), 400, 550, (1280 - 400) / 2, 0);
        fFmpegCmd.run();
        Output.print("finish");
    }
}
