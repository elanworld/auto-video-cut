package com.alan;

import com.alan.system.SystemPath;
import com.alan.util.FilesBox;
import com.alan.util.Output;
import com.alan.video.OpenCvBox;

public class Main {
    public static void main(String[] args) {
        for (String file : FilesBox.dictoryListFilter(SystemPath.ROW_VIDEO_DIRECTORY.getPath(), false, "mp4")) {
            Output.print(file);
            OpenCvBox openCvBox = new OpenCvBox();
            openCvBox.recognition(file);
            FilesBox.move(file, SystemPath.GENERAL_VIDEO_DIRECTORY.getPath());
        }
    }
}
