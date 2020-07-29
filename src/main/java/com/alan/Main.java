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
        String file = "F:\\Alan\\Videos\\电影\\out_举起手来 HD1280高清国语中字.mp4.wav";
        Lflyasr lflyasr = new Lflyasr();
        lflyasr.upData(file);
        lflyasr.loopGetResult();
        lflyasr.exit();


    }
}
