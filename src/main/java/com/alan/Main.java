package com.alan;

import com.alan.util.FilesBox;
import com.alan.video.OpenCvBox;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        String dictory = "F:\\Alan\\Videos\\Mine\\selenium_download";
        String dirTouched = new File("F:\\Alan\\Videos\\Mine\\selenium_download", "touched").toString();
        for (String file : FilesBox.dictoryListFilter(dictory, "mp4", false)) {
            OpenCvBox openCvBox = new OpenCvBox(file);
            FilesBox.move(file, dirTouched);
        }
    }
}
