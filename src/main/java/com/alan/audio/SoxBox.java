package com.alan.audio;

import com.alan.util.FilesBox;
import com.alan.util.RunCommand;
import com.alan.util.RunReceiver;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SoxBox implements RunCommand {
    String sox = "sox";
    String cmdLine;
    private RunReceiver runReceiver;
    Set<String> clearBox = new LinkedHashSet<>();

    public List<String> noiseProf(String inputFile, String noise, String outFile) {
        String prof = FilesBox.outExt(noise, "prof");
        clearBox.add(prof);
        String cmd = String.format("%s %s -n noiseprof %s", sox, noise, prof);
        String cmd1 = String.format("%s %s %s noisered %s 0.21", sox, inputFile, outFile, prof);
        return Arrays.asList(cmd,cmd1);
    }

    public void clearFiles() {
        for (String f : clearBox) {
            Paths.get(f).toFile().delete();
        }
    }

    public void execute() {
        runReceiver = new RunReceiver();
        runReceiver.setCommand(cmdLine);
        runReceiver.run();
    }

    @Override
    public void setCmdLine(String cmdLine) {
        this.cmdLine = cmdLine;
    }
}
