package com.alan;

import com.alan.audio.AudioContainer;
import com.alan.video.FFmpegCmd;
import com.alan.util.FilesBox;
import com.alan.util.Output;

import java.io.File;
import java.util.ArrayList;

public class MainAudioRing {
    public static void main(String[] args) {


        String dir = "F:\\Alan\\Music";
        File outDir = new File(dir, "out");
        if (!outDir.exists()) {
            outDir.mkdir();
        }
        ArrayList<String> strings = FilesBox.dictoryList(dir);
        for (String file : strings) {
            Output.print(file);
            AudioContainer audioContainer = new AudioContainer();
            audioContainer.loadByFFmpeg(file);
            float duration = audioContainer.duration;
            float start = 0;
            if (duration > 30) {
                start = (duration - 30) / 2;
            }
            FFmpegCmd fFmpegCmd = new FFmpegCmd();
            String outFile = new File(outDir.toString(), FilesBox.pathSplit(file)[3]).toString();
            if (new File(outFile).exists()) {
                continue;
            }
            fFmpegCmd.setInput(file).setOutput(outFile).setTime(start, start + 30).runCommand();
        }
    }
}
