package com.alan.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * @apiNote : just using package of jdk
 */
public class StringContainer {
    public static String[] pathSplit(String path) {
        File file = new File(path);
        String name = file.getName();
        String parent = file.getParent();
        String[] split = name.split("\\.");
        String basename = split[0];
        String ext = "." + split[1];
        String[] paths = {parent, basename, ext , name};
        return paths;
    }

    public static String outFile(String inputPath) {
        String[] paths = pathSplit(inputPath);
        Path outPath = Paths.get(paths[0], paths[1] + "_out" + paths[2]);
        return outPath.toString();
    }

    public static String outDirFile(String inputPath, int fileNum) {
        String[] paths = pathSplit(inputPath);
        Path outPath = Paths.get(paths[0], "out", paths[1], paths[1] + "_" + String.valueOf(fileNum) + paths[2]);
        Path parent = outPath.getParent();
        mkdirOutPath(parent);
        return outPath.toString();
    }

    private static void mkdirOutPath(Path path) {
        if (!Files.exists(path)) {
            File file = new File(path.toString());
            file.mkdirs();
        }
    }

    public static String findLine(ArrayList<String> lines, String regex) {
        String catLine = null;
        regex = ".*" + regex + ".*";
        for (String line : lines) {
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

    public static boolean regexFiles(String dir, String regex) {
        boolean got = false;
        ArrayList<String> strings = dictoryList(dir);
        String line = findLine(strings, regex);
        if (line != null)  got = true;
        return got;
    }
}

