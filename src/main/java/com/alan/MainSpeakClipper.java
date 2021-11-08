package com.alan;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alan.audio.AudioContainer;
import com.alan.audio.RosaPy4j;
import com.alan.audio.SoxBox;
import com.alan.common.system.SystemPath;
import com.alan.common.util.FilesBox;
import com.alan.video.FFmpegCmd;

/**
 * 说话识别监视视频剪切器
 */
public class MainSpeakClipper {
	boolean noise = false;

	RosaPy4j rosaPy4j;
	AudioContainer audioContainer;
	FFmpegCmd fFmpegCmd;
	SoxBox soxBox;

	public MainSpeakClipper() {
		rosaPy4j = new RosaPy4j();
		audioContainer = new AudioContainer();
		fFmpegCmd = new FFmpegCmd();
		fFmpegCmd.setTimeout(Duration.ofMinutes(10L));
		soxBox = new SoxBox();
	}

	public void run(String file) {
		List<List<Double>> speakClips = rosaPy4j.getSpeakClips(file);
		List<List<Double>> silenceFromeSpeak = audioContainer.getSilenceFromeSpeak(speakClips);
		FFmpegCmd.FiltersSet filtersSet = fFmpegCmd.new FiltersSet();

		String wav = FilesBox.changeExt(file, "wav");
		String good = FilesBox.outFile(wav, "good");
		String bad = FilesBox.outFile(wav, "bad");
		String soxOut = FilesBox.outFile(wav, "soxOut");
		String soxOutDnorm = FilesBox.outFile(wav, "soxOutDnorm");
		String bgmGenerate = FilesBox.outFile(wav, "bgmBack");
		String speakWithBgm = FilesBox.outFile(wav, "speakWithBgm");
		String temp = FilesBox.outFile(file, "temp");
		String speak = FilesBox.outFile(file, "speak");
		List<String> cleanFiles = new ArrayList<>(
				Arrays.asList(good, bad, soxOut, soxOutDnorm, bgmGenerate, speakWithBgm, temp));
		List<String> bgms = FilesBox.directoryListFilter(SystemPath.BGM.getPath(), true, "mp3", "wav", "m4a");
		String bgm = bgms.get(0);

		// generate background music to good voice
		fFmpegCmd.setInput(bgm).setOutput(bgmGenerate);
		filtersSet.setAudioLoudnorm().setAudioVolumePercent(0.2).toFFmpegCmd();
		fFmpegCmd.run();
		fFmpegCmd.clear();

		// generate speak voice
		fFmpegCmd.clear().setInput(file).setOutput(good);
		filtersSet.setSelect(speakClips).toFFmpegCmd().run();
		fFmpegCmd.clear();

		if (noise) {
			fFmpegCmd.setInput(file).setOutput(bad);
			filtersSet.setSelect(silenceFromeSpeak).toFFmpegCmd().run();
			fFmpegCmd.clear();
			List<String> cmd = soxBox.noiseProf(bad, good, soxOut);
			soxBox.run(cmd.get(0));
			soxBox.run(cmd.get(1));
		} else {
			filtersSet.setAudioLoudnorm().toFFmpegCmd().setInput(good).setOutput(soxOut).run();
		}

		filtersSet.setAudioLoudnorm().setAudioVolumePercent(1).toFFmpegCmd().setInput(soxOut).setOutput(soxOutDnorm)
				.run();
		fFmpegCmd.clear();

		fFmpegCmd.setInput(bgmGenerate).setInput(soxOutDnorm).setOutput(speakWithBgm);
		filtersSet.setAudioMix().toFFmpegCmd().run();
		fFmpegCmd.clear();

		// generate final file
		fFmpegCmd.setInput(file).setOutput(temp).setCodecQSV();
		filtersSet.setSelect(speakClips).toFFmpegCmd().run();
		fFmpegCmd.clear();

		// fFmpegCmd.setCodecQSV(); memory leaks
		if (fFmpegCmd.isVideo(file)) {
			fFmpegCmd.setInput(temp).setInput(speakWithBgm).setOutput(speak).setCodec("copy")
					.setMap("-map 0:v -map 1:a").run();
		} else {
			fFmpegCmd.setInput(speakWithBgm).setOutput(speak).run();
		}
		cleanFiles.forEach(FilesBox::deleteFiles);
	}

	public static void main(String[] args) {
		for (String file : FilesBox.directoryListFilter(SystemPath.VLOG.getPath(), false, "")) {
			MainSpeakClipper mainSpeakClipper = new MainSpeakClipper();
			mainSpeakClipper.run(file);
		}
	}
}
