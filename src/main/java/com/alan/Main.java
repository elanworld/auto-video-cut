package com.alan;

import com.alan.audio.Lflyasr;
import com.alan.cmd.FFmpegCmd;
import com.alan.cmd.RunCmd;
import com.alan.output.Output;

public class Main {

    public static void main(String[] args) {
        // write your code here
        new Output("Main class");
        FFmpegCmd ffmpegCmd = new FFmpegCmd();
        String file = "F:\\Alan\\Videos\\��Ӱ\\out_�������� HD1280�����������.mp4.wav";
        Lflyasr lflyasr = new Lflyasr();
        lflyasr.upData(file);
        lflyasr.loopGetResult();
        lflyasr.exit();


    }
}
