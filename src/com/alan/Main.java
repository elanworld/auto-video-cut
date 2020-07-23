package com.alan;

import com.alan.audio.AudioAnlysis;
import com.alan.cmd.RunCmd;

public class Main {

    public static void main(String[] args) {
	// write your code here
        String file = "F:\\Alan\\Music\\AutoCutBGM\\aa.wav";
        AudioAnlysis aua = new AudioAnlysis();
        aua.getAudio(file);
    }
}
