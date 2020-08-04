package com.alan.audio;

import com.alan.cmd.FFmpegCmd;
import com.alan.cmd.RunCmd;
import com.alan.output.Output;
import com.alan.util.StringConv;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AudioShortCut {
    public static void main(String[] args) {


        String dir = "F:\\Alan\\Music\\¿á¹·";
        File outDir = new File(dir, "out");
        if (!outDir.exists()) {
            outDir.mkdir();
        }
        ArrayList<String> strings = StringConv.dictoryList(dir);
        for (String file : strings) {
            Output.print(file);
            String audio = file;
            AudioAnlysis audioAnlysis = new AudioAnlysis();
            audioAnlysis.loadByFFmpeg(audio);
            float duration = audioAnlysis.duration;
            float start = 0;
            if (duration > 30) {
                start = (duration - 30) / 2;
            }
            FFmpegCmd fFmpegCmd = new FFmpegCmd();
            String outFile = new File(outDir.toString(), StringConv.pathSplit(audio)[3]).toString();
            if (new File(outFile).exists()) {
                continue;
            };
            String cmd = fFmpegCmd.cutAudio(audio, outFile, start, start + 30);
            new RunCmd(cmd);
        }
    }
}
