/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan.text;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alan.common.text.SubtitleBox;
import org.junit.Assume;
import org.junit.Test;

import com.alan.common.util.Output;

public class SubtitleBoxTest {

	String subtitle = "F:\\Alan\\Videos\\Mine\\selenium_download\\New Best Zach King Magic Tricks 2020.srt";
	String subtitleOut = "F:\\Alan\\Videos\\Mine\\selenium_download\\New Best Zach King Magic Tricks 20201.srt";

	@Test
	public void init() {
		new SubtitleBox().init(
				"F:\\Alan\\Videos\\Mine\\selenium_download\\youtube\\Lobsters vs Trigger Fish _ Trials Of Life _ BBC Earth.srt");
	}

	@Test
	public void subtitleTest() {
		SubtitleBox subtitleBox = new SubtitleBox();
		Pattern num = Pattern.compile("^(\\d{1,4})$");
		Matcher numMt = num.matcher("1");
		boolean b = numMt.find();
		Assume.assumeTrue(b);
		subtitleBox.init(subtitle);
		List<String> chinese = subtitleBox.getByFilter(false, true, true, 5, 20, 0);
		Output.print(chinese);
		subtitleBox.write(chinese, subtitleOut);

	}
}
