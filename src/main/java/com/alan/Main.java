package com.alan;

import com.alan.audio.Lflyasr;
import com.alan.cmd.FFmpegCmd;
import com.alan.cmd.RunCmd;
import com.alan.output.Output;
import com.alan.util.StringConv;
import com.alan.video.CvContainer;
import org.bytedeco.opencv.presets.opencv_core;

public class Main {

    public static void main(String[] args) throws Exception/**/{
        // write your cod1e here
        String file = "F:\\Alan\\Videos\\我的视频\\剪辑\\知乎视频.mp4";
        CvContainer cvContainer = new CvContainer();
        cvContainer.grabFrame(file);

    }
}
