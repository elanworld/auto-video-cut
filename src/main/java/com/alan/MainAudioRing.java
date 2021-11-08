package com.alan;

import java.io.File;
import java.util.List;

import com.alan.audio.AudioContainer;
import com.alan.common.util.FilesBox;
import com.alan.common.util.Output;
import com.alan.common.util.StringBox;
import com.alan.video.FFmpegCmd;

/**
 * 铃声剪辑
 */
public class MainAudioRing {
	public static void main(String[] args) {
		String dir;
		if (args.length == 0) {
			dir = StringBox.input();
		} else {
			dir = args[0];
		}
		List<String> strings = FilesBox.directoryList(dir);
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
			String outFile = FilesBox.outDirFile(file);
			if (new File(outFile).exists()) {
				continue;
			}
			fFmpegCmd.setInput(file).setOutput(outFile).setTime(start, start + 30).run();
		}
	}
}
