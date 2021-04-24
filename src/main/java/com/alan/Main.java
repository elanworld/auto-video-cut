package com.alan;

import com.alan.common.system.SystemPath;
import com.alan.common.util.FilesBox;
import com.alan.common.util.Output;
import com.alan.video.OpenCvBox;

/**
 * 短视频识别场景切割
 */
public class Main {
	public static void main(String[] args) {
		for (String file : FilesBox.directoryListFilter(SystemPath.ROW_VIDEO.getPath(), false, "mp4")) {
			Output.print(file);
			OpenCvBox openCvBox = new OpenCvBox();
			openCvBox.recognition(file);
			FilesBox.move(file, SystemPath.GENERAL_VIDEO.getPath());
		}
	}
}
