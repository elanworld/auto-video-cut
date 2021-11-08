/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan.video;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.alan.common.data.main.dao.DownHistoryDao;
import com.alan.common.data.main.entity.SubtitleVo;
import com.alan.common.data.DataBox;
import com.alan.common.util.FilesBox;
import com.alan.common.util.StringBox;
import com.alan.text.SubtitleService;

/**
 * @Description: 实现功能
 * @Author: Alan
 * @Date: 2021/6/30
 */
public class MovieGen {
	FFmpegFuture fFmpegFuture = new FFmpegFuture();
	DownHistoryDao dao = DataBox.getMapper(DownHistoryDao.class);

	public void genFromText(String text, String outFile) {
		SubtitleService subtitleService = new SubtitleService();
		List<String> splitText = StringBox.splitText(text);
		List<SubtitleVo> subtitleVos = subtitleService.queryLikeSubtle(splitText);
		List<String> tss = new ArrayList<>();
		subtitleVos.forEach(sub -> {
			String temp = FilesBox.temp(FilesBox.outFile("temp.ts"));
			fFmpegFuture.setInput(dao.queryById(sub.getDownHisId()).getFilePath(), true).setOutput(temp);
			fFmpegFuture.setCodecCopy().setTime(sub.getStart(), sub.getEnd()).run();
			tss.add(temp);
		});
		fFmpegFuture.clear();
		fFmpegFuture.setTimeout(Duration.ofMinutes(10));
		fFmpegFuture.setInputConcat(tss).setCodecQSV().setOutput(outFile).run();
		FilesBox.tempDelete();
	}

}
