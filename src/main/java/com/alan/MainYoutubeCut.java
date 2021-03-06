/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan;

import java.io.File;
import java.util.List;

import com.alan.system.SystemPath;
import com.alan.text.SubtitleBox;
import com.alan.text.Translator;
import com.alan.util.FilesBox;
import com.alan.video.FFmpegCmd;

/**
 * @Description: youtube视频字幕翻译嵌入
 * @Author: Alan
 * @Date: 2021/3/6
 */
public class MainYoutubeCut {
	public static void main(String[] args) {
		FFmpegCmd fFmpegCmd = new FFmpegCmd();
		SubtitleBox sub = new SubtitleBox();
		Translator translator = new Translator();
		List<String> mp4 = FilesBox.dictoryListFilter(SystemPath.YOUTUBE.getPath(), false, "mp4");
		List<String> srt = FilesBox.dictoryListFilter(SystemPath.YOUTUBE.getPath(), false, "srt");
		for (String m : mp4) {
			File file = new File(m);
			String name = file.getName();
			String parent = file.getParent();
			String out = new File(SystemPath.PRODUCE.getPath(), name).toString();
			String s = new File(parent, name.replace("-", "_").replace("mp4", "srt")).getPath();
			String ns = s + ".srt";
			if (srt.stream().anyMatch(n -> n.equals(s))) {
				sub.init(s);
				sub.forEach(n -> {
					String str = translator.run(String.join("\n", n.getText()));
					n.getText().add(str);
				});
				sub.write(sub.getSubtitleBodies(), ns);
				fFmpegCmd.setInput(m).setOutput(out).getFiltersSet().setSubtitle(ns).toFFmpegCmd().run();
			}

		}

	}
}
