package com.alan.util;

import com.alan.output.Output;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StringConv {
    public static String[] pathSplit(String path) {
        File file = new File(path);
        String name = file.getName();
        String parent = file.getParent();
        String[] split = name.split("\\.");
        String basename = split[0];
        String ext = "." + split[1];
        String[] pathSplit = {parent, basename, ext , name};
        return pathSplit;
    }

    public String getOutPath(String inputPath) {
        Path path = Paths.get(inputPath);
        Path parent = path.getParent();
        Path fileName = path.getFileName();
        Path outPath = Paths.get(parent.toString(), "out_" + fileName.toString());
        return outPath.toString();
    }

    public static String findLine(ArrayList<String> lines, String regex) {
        String catLine = "";
        regex = ".*" + regex + ".*";
        for (String line : lines) {
            new Output(line);
            boolean matches = line.matches(regex);
            if (matches) {
                catLine = line;
            }
        }
        return catLine;
    }

    public static ArrayList<String> dictoryList(String dir) {
        ArrayList<String> list = new ArrayList<String>();
        Path path = Path.of(dir);
        try {
            Stream<Path> pathStream = Files.list(path);
            Object[] objects = pathStream.toArray();
            for (Object object : objects) {
                String fileObject = object.toString();
                if (new File(fileObject).isFile())
                    list.add(object.toString());
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}

class MyException extends Exception {

}