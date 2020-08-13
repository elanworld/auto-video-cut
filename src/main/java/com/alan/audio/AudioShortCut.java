package com.alan.audio;

import com.alan.cmd.FFmpegCmd;
import com.alan.util.FilesBox;
import com.alan.util.Output;
import com.alan.util.StringContainer;

import java.io.File;
import java.util.ArrayList;

public class AudioShortCut {
    public static void main(String[] args) {


        String dir = "F:\\Alan\\Music\\¿á¹·";
        File outDir = new File(dir, "out");
        if (!outDir.exists()) {
            outDir.mkdir();
        }
        ArrayList<String> strings = FilesBox.dictoryList(dir);
        for (String file : strings) {
            Output.print(file);
            String audio = file;
            AudioContainer audioContainer = new AudioContainer();
            audioContainer.loadByFFmpeg(audio);
            float duration = audioContainer.duration;
            float start = 0;
            if (duration > 30) {
                start = (duration - 30) / 2;
            }
            FFmpegCmd fFmpegCmd = new FFmpegCmd();
            String outFile = new File(outDir.toString(), FilesBox.pathSplit(audio)[3]).toString();
            if (new File(outFile).exists()) {
                continue;
            };
            fFmpegCmd.setInput(audio).setOutput(outFile).setTime(start, start + 30).run();
        }
    }
}
