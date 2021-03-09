package com.alan;

import java.util.List;

import com.alan.audio.SoxBox;

public class MainSilence {
	public static void main(String[] args) {
		String audio = "F:\\Alan\\Videos\\Mine\\与我相关\\vlog\\aa.wav";
		String noise = "F:\\Alan\\Videos\\Mine\\与我相关\\vlog\\bb.wav";
		String out = "F:\\Alan\\Videos\\Mine\\与我相关\\vlog\\cc.wav";
		SoxBox soxBox = new SoxBox();
		List<String> noiseCmd = soxBox.noiseProf(audio, noise, out);

		soxBox.run(noiseCmd.get(0));
		soxBox.run(noiseCmd.get(1));
		soxBox.clearFiles();

	}
}
