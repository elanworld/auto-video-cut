package com.alan.utils;

import com.alan.output.Output;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Spliterator;
import java.util.regex.Pattern;

public class FilePath {
    public String[] pathSplit(String path) {
        File file = new File(path);
        String name = file.getName();
        String parent = file.getParent();
        String[] split = name.split("\\.");
        String basename = split[0];
        String ext = "." + split[1];
        String[] pathSplit = {parent, basename, ext};
        return pathSplit;
    }

    public String getOutPath(String inputPath) {
        Path path = Paths.get(inputPath);
        Path parent = path.getParent();
        Path fileName = path.getFileName();
        Path outPath = Paths.get(parent.toString(), "out_" + fileName.toString());
        return outPath.toString();
    }
}
