/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan.video;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import com.alan.common.util.FilesBox;

/**
 * @Description: 实现功能
 * @Author: Alan
 * @Date: 2021/6/8
 */
public class FFmpegFuture extends FFmpegCmd {

	public void concat(List<String> inputs, String output) {
		String temp = "temp";
		setInput(temp);
		setOutput(output);
		this.cmdMap.replace(FFmpegEnum.concat, "-f concat -safe 0");
		List<String> collect = inputs.stream().map(file -> "file '" + file + "'").collect(Collectors.toList());
		FilesBox.writer(collect, temp);
		setCodecQSV();
		run();
		new File(temp).delete();
	}
}
