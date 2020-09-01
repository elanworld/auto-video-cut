package com.alan.audio;

import com.alan.util.FilesBox;
import com.alan.util.Output;
import com.alan.util.RunCmd;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class SoxBox {
    String sox = "cmd /c sox";

    public SoxBox noise(String noise, String file, String outFile) {
        String prof = FilesBox.outDirFile(file) + ".prof";
        String cmd = String.format("%s %s -n noiseprof %s", sox, noise, prof);
        new RunCmd(cmd);
        cmd = String.format("%s %s %s noisered %s 0.21", sox, file, outFile, prof);
        new RunCmd(cmd);
        for (String f : new String[]{prof}) {
            Paths.get(f).toFile().delete();
        }
        return this;
    }
}
