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
import com.alan.util.Output;
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
		Output.print("find video:", mp4);
		for (String m : mp4) {
			String rs;
			for (String s : srt) {
				rs = FilesBox.renameIfLike(m, s);
				String ns = FilesBox.outFile(rs, "new");
				String out = new File(SystemPath.PRODUCE.getPath(), new File(m).getName()).toString();
				if (rs != null) {
					sub.init(rs);
					sub.forEach(n -> {
						String str = translator.translate(String.join(",", n.getText()), true);
						n.getText().add(str);
					});
					sub.write(sub.getAll(), ns);
					fFmpegCmd.setInput(m).setOutput(out).setCodecQSV().getFiltersSet().setSubtitle(ns).toFFmpegCmd()
							.run();
					if (new File(out).exists()) {
						FilesBox.move(m, FilesBox.outDir(m, "used"));
						FilesBox.move(rs, FilesBox.outDir(m, "used"));
						new File(ns).delete();
					}
				}
			}
		}
	}
}
