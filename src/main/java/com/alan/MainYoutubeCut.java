/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alan.auto.dao.DownHistoryDao;
import com.alan.auto.entity.DownHistory;
import com.alan.auto.system.DownTypeEnum;
import com.alan.common.data.DataBox;
import com.alan.common.system.SystemPath;
import com.alan.common.text.SubtitleBox;
import com.alan.common.util.FilesBox;
import com.alan.common.util.Output;
import com.alan.common.util.StringBox;
import com.alan.common.web.tans.BaiduTranslator;
import com.alan.video.FFmpegFuture;

/**
 * @Description: youtube视频字幕翻译嵌入
 * @Author: Alan
 * @Date: 2021/3/6
 */
public class MainYoutubeCut {
	public static void main(String[] args) {
		DownHistoryDao historyDao = DataBox.getMapper(DownHistoryDao.class);
		DownHistory downHistory = new DownHistory();
		downHistory.setUsed(false);
		List<DownHistory> downHistories = historyDao.queryAll(downHistory);
		Output.print("find video:", downHistories);
		downHistories.forEach(movie -> downHistories.forEach(subtitle -> {
			if (StringUtils.equals(movie.getTitle(), subtitle.getTitle())
					&& StringUtils.equals(movie.getType(), DownTypeEnum.movie.name())
					&& DownTypeEnum.isSubtitle(subtitle.getType())) {
				List<String> outs = cut(movie.getFilePath(), subtitle.getFilePath());
				movie.setUsed(true);
				subtitle.setUsed(true);
				historyDao.update(movie);
				historyDao.update(subtitle);
				outs.forEach(file -> {
					DownHistory product = new DownHistory();
					product.setType(DownTypeEnum.product.name());
					product.setTitle(movie.getTitle());
					product.setUrl(movie.getUrl());
					product.setFilePath(file);
					product.setFileExists(true);
					product.setUsed(false);
					product.setLastUpdateDate(new Date());
					historyDao.insert(product);
				});
			}
		}));

	}

	private static List<String> cut(String movie, String srt) {
		FFmpegFuture fFmpegCmd = new FFmpegFuture();
		fFmpegCmd.setTimeout(Duration.ofMinutes(30));
		SubtitleBox sub = new SubtitleBox();
		String ns = FilesBox.outFile(srt, "new");
		String tmp = new File(movie).getName() + ".ts";
		String out = new File(SystemPath.PRODUCE.getPath(), new File(movie).getName()).toString();
		sub.init(srt);
		sub.forEach(n -> {
			if (!StringBox.checkChinese(String.join(",", n.getText())) && !String.join(",", n.getText()).equals("")) {
				String str = BaiduTranslator.translate(String.join(",", n.getText()), true);
				n.getText().add(str);
			}
		});
		sub.write(sub.getAll(), ns);
		fFmpegCmd.setCodecQSV().setInput(movie).setOutput(tmp).getFiltersSet().setSubtitle(ns).toFFmpegCmd().run();
		fFmpegCmd.clear();
		List<String> outs = new ArrayList<>();
		double duration = fFmpegCmd.setInput(tmp).getMetadata().duration;
		for (double i = 0; i < duration; i += 180) {
			double end = i + 180;
			if (end > duration) {
				end = duration;
			}
			String tempTs = "temp.ts";
			fFmpegCmd.setInput(tmp).setOutput(tempTs).setCodecCopy().setTime(i, end).run();
			fFmpegCmd.clear();
			String output = FilesBox.outFile(out);
			fFmpegCmd.concat(Arrays.asList(SystemPath.LIKE.getPath(), tempTs, SystemPath.LIKE.getPath()), output);
			outs.add(output);
			new File(tempTs).delete();
		}
		new File(tmp).delete();
		new File(ns).delete();
		return outs;
	}
}
