/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan;

import java.io.File;
import java.time.Duration;
import java.util.List;

import com.alan.system.SystemPath;
import com.alan.text.SubtitleBox;
import com.alan.text.baudu.BaiduTranslator;
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
		fFmpegCmd.setTimeout(Duration.ofMinutes(5));
		SubtitleBox sub = new SubtitleBox();
		BaiduTranslator translator = new BaiduTranslator();
		List<String> mp4 = FilesBox.directoryListFilter(SystemPath.YOUTUBE.getPath(), false, "mp4");
		List<String> srt = FilesBox.directoryListFilter(SystemPath.YOUTUBE.getPath(), false, "srt");
		for (String m : mp4) {
			File file = new File(m);
			String name = file.getName();
			String parent = file.getParent();
			String out = new File(SystemPath.PRODUCE.getPath(), name).toString();
			if (new File(out).exists()) {
				continue;
			}
			String s = new File(parent, name.replace("-", "_").replace("mp4", "srt")).getPath();
			String ns = s + ".srt";
			if (srt.stream().anyMatch(n -> n.equals(s))) {
				sub.init(s);
				sub.forEach(n -> {
					String str = translator.translate(String.join(",", n.getText()), true);
					n.getText().add(str);
				});
				sub.write(sub.getAll(), ns);
				fFmpegCmd.setInput(m).setOutput(out).getFiltersSet().setSubtitle(ns).toFFmpegCmd();
				fFmpegCmd.run();
				new File(ns).delete();
			}
		}

	}
}
