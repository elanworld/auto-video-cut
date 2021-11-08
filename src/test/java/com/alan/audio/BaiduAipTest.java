/*
 * Copyright (c) 2021.
 * author:Alan
 * All rights reserved.
 */

package com.alan.audio;

import org.junit.Test;

public class BaiduAipTest {

	@Test
	public void text2audio() {
		new BaiduAip().text2audio("我看见了什么东西", "output.mp3");
	}
}
