package com.alan;

import com.alan.audio.AudioAip;
import com.alan.audio.AudioAnlysis;
import com.alan.cmd.RunCmd;
import com.alan.video.FfmpegCmd;
import com.baidu.aip.speech.AipSpeech;

public class Main {

    public static void main(String[] args) {
        // write your code here
        String file = "F:\\Alan\\Videos\\我的视频\\知乎\\cut_video\\知乎视频.mp4";
        String outfile = "F:\\Alan\\Videos\\我的视频\\知乎\\cut_video\\知乎视频.pcm";
//        String cmd = new FfmpegCmd().getPCM(file,outfile,20,40);
//        new RunCmd(cmd);
//        new AudioAip().speechRec(outfile);
    }
}
