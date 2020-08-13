package com.alan.util;

import java.util.ArrayList;

/**
 * @apiNote : just using package of jdk
 */
public class StringContainer {
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

    public static boolean regexFiles(String dir, String regex) {
        boolean got = false;
        ArrayList<String> strings = FilesBox.dictoryList(dir);
        String line = findLine(strings, regex);
        if (line != null) got = true;
        return got;
    }
}

