package com.alan.audio;

import com.alan.cmd.FFmpegCmd;
import com.alan.cmd.RunCmd;
import com.alan.output.Output;

public class AudioShortCut {
    public static void main(String[] args) {
        String audio = "F:\\Alan\\Music\\¿á¹·\\ÅËçâ°Ø - Â·Ì«Íä.mp3";
        AudioAnlysis audioAnlysis = new AudioAnlysis();
        audioAnlysis.loadAudio(audio);
        float duration = audioAnlysis.duration;
        float start = 0;
        new Output(duration);
        if (duration > 30) {
            start = (duration - 30) / 2;
        }
        FFmpegCmd fFmpegCmd = new FFmpegCmd();
        String cmd = fFmpegCmd.cutAudio(audio, fFmpegCmd.getOutPath(audio), start, start + 30);
        new RunCmd(cmd);
    }
}
