package com.alan;

import com.alan.util.FilesBox;
import com.alan.util.Output;
import com.alan.video.OpenCvBox;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String dictory = "F:\\Alan\\Videos\\Mine\\selenium_download";
        String dirTouched = new File("F:\\Alan\\Videos\\Mine\\selenium_download", "touched").toString();
        for (String file : FilesBox.dictoryListFilter(dictory, false, "mp4")) {
            Output.print(file);
            OpenCvBox openCvBox = new OpenCvBox();
            openCvBox.recognition(file);
            FilesBox.move(file, dirTouched);
        }
    }
}
