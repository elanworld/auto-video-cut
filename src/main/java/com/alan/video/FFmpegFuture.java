/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan.video;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alan.common.util.FilesBox;

/**
 * @Description: 实现功能
 * @Author: Alan
 * @Date: 2021/6/8
 */
public class FFmpegFuture extends FFmpegCmd {

	public void concat(List<String> inputs, String output) {
		String temp = "temp.txt";
		setInput(temp);
		setOutput(output);
		super.cmdMap.replace(FFmpegEnum.format, "-f concat -safe 0");
		List<String> collect = inputs.stream().map(file -> "file '" + file + "'").collect(Collectors.toList());
		FilesBox.writer(collect, temp);
		setCodecCopy();
		run();
		new File(temp).delete();
	}

	/**
	 * 设置ts格式
	 *
	 * @return
	 */
	public FFmpegFuture setOutTs() {
		setCodecQSV();
		super.cmdMap.replace(FFmpegEnum.out_format, "-bsf:v h264_mp4toannexb -bsf:a aac_adtstoasc -r 30 -ar 48000");
		return this;
	}

	public FFmpegFuture setInputConcat(List<String> inputs) {
		super.inputFiles = inputs;
		Stream<String> stream = inputs.stream().map(file -> "\"" + file + "\"");
		String join = String.join("|", stream.collect(Collectors.toList()));
		String line = String.format("-i \"concat:%s\"", join);
		super.cmdMap.replace(FFmpegEnum.input, line);
		return this;
	}
}
