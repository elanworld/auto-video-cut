package com.alan;

import com.alan.audio.SoxBox;
import com.alan.util.RunInvoker;

import java.util.List;

public class MainSilence {
    public static void main(String[] args) {
        String audio = "F:\\Alan\\Videos\\Mine\\与我相关\\vlog\\aa.wav";
        String noise = "F:\\Alan\\Videos\\Mine\\与我相关\\vlog\\bb.wav";
        String out = "F:\\Alan\\Videos\\Mine\\与我相关\\vlog\\cc.wav";
        SoxBox soxBox = new SoxBox();
        List<String> noiseCmd = soxBox.noiseProf(audio, noise, out);

        RunInvoker runInvoker = new RunInvoker(soxBox);
        soxBox.setCmdLine(noiseCmd.get(0));
        runInvoker.call();
        soxBox.setCmdLine(noiseCmd.get(1));
        runInvoker.call();
        soxBox.clearFiles();

    }
}
