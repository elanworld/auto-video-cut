package com.alan.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static String input() {
        String s = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            s = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static int inputInterger() {
        while (true) {
            String input = input();
            Output.print(input);
            if (input.matches("(\\d+)")) {
                return Integer.valueOf(input);
            }

        }
    }
}

