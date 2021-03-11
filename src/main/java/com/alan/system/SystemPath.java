/*
 * Copyright (c) 2020.
 * author:Alan
 * All rights reserved.
 */

package com.alan.system;

public enum SystemPath {
	ROW_VIDEO("F:\\Alan\\Videos\\Mine\\selenium_download", "原视频目录"), GENERAL_VIDEO(
			"F:\\Alan\\Videos\\Mine\\selenium_download\\touched",
			"生成视频目录"), YOUTUBE("F:\\Alan\\Videos\\Mine\\selenium_download\\youtube", "youtube下载目录"), PRODUCE(
					"F:\\Alan\\Videos\\Mine\\selenium_download\\youtube\\produce",
					"youtube生成目录"), BGM("F:\\Alan\\Music\\AutoCutBGM\\speak",
							"bgm目录"), VLOG("F:\\Alan\\Videos\\Mine\\与我相关\\vlog", "vlog目录");

	String path;
	String describe;

	SystemPath(String path, String describe) {
		this.path = path;
		this.describe = describe;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

}
