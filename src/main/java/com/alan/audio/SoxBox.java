package com.alan.audio;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.alan.util.FilesBox;
import com.alan.util.RunCmd;

public class SoxBox extends RunCmd {
	String sox = "sox";
	Set<String> clearBox = new LinkedHashSet<>();

	public List<String> noiseProf(String inputFile, String noise, String outFile) {
		String prof = FilesBox.changeExt(noise, "prof");
		clearBox.add(prof);
		String cmd = String.format("%s %s -n noiseprof %s", sox, noise, prof);
		String cmd1 = String.format("%s %s %s noisered %s 0.21", sox, inputFile, outFile, prof);
		return Arrays.asList(cmd, cmd1);
	}

	public void clearFiles() {
		for (String f : clearBox) {
			Paths.get(f).toFile().delete();
		}
	}

	@Override
	public List<String> getResult() {
		return null;
	}

	@Override
	public void input(String input) {

	}
}
