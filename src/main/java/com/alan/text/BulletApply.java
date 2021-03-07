package com.alan.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.alan.util.FilesBox;
import com.alan.util.StringBox;
import com.alan.video.FFmpegCmd;

public class BulletApply {
	BulletBox bulletBox;
	List<String> videos;
	String keyWord;
	FFmpegCmd fFmpegCmd;
	String outDir;

	public BulletApply() {
		this.bulletBox = new BulletBox();
		this.videos = new ArrayList<>();
		this.fFmpegCmd = new FFmpegCmd();
	}

	public void setApply() {
		bulletBox.setKeyWord(keyWord);
		HashMap<Long, String> fileIndex = getFileIndex(videos);
		ArrayList<Integer> list = new ArrayList<>();
		for (Long episode : fileIndex.keySet()) {
			list.add(episode.intValue());
		}
		bulletBox.setEpisodes(list);
	}

	public List<String> getFiles() {
		List<String> allFile = new ArrayList<>();
		for (String dir : this.videos) {
			allFile.addAll(FilesBox.directoryList(dir));
		}
		return allFile;
	}

	public HashMap<Long, String> getFileIndex(List<String> files) {
		HashMap<Long, String> box = new HashMap<>();
		for (String file : files) {
			String regex = ".*庆余年 第(.*)集_1080P.*";
			List<String> group = StringBox.findGroup(Collections.singletonList(file), regex);
			if (!group.isEmpty()) {
				Long episode = Long.decode(group.get(0));
				box.put(episode, file);
			}

		}
		return box;
	}

	public void run() {
		List<List<Long>> clips = bulletBox.getClip();
		HashMap<Long, String> fileIndex = getFileIndex(videos);
		for (int i = 0; i < 5; i++) {
			if (i > clips.size() - 1) {
				break;
			}
			List<Long> bullet = clips.get(i);
			Long episode = bullet.get(0);
			Long start = bullet.get(1);
			Long end = bullet.get(2);
			String episodeFile = fileIndex.get(episode);
			fFmpegCmd.setInput(episodeFile).setOutput(FilesBox.outDirFile(episodeFile)).setTime(start, end)
					.setCodecQSV().run();
		}

	}

	public List<String> getVideos() {
		return videos;
	}

	public void setVideos(List<String> videos) {
		this.videos = videos;
	}

	public void addVideoDir(String videoDir) {
		this.videos.addAll(FilesBox.directoryList(videoDir));
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getOutDir() {
		return outDir;
	}

	public void setOutDir(String outDir) {
		this.outDir = outDir;
	}
}
