package com.alan.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @apiNote : just using package of jdk
 */
public class StringContainer {
    public static ArrayList<String> findLine(ArrayList<String> lines, String regex) {
        ArrayList<String> found = new ArrayList<>();
        Pattern compile = Pattern.compile(regex);
        for (String line : lines) {
            Matcher matcher = compile.matcher(line);
            if (matcher.find()) {
                for (int i = 1;i <= matcher.groupCount(); i++) {
                    found.add(matcher.group(i));
                }
            }
        }
        return found;
    }

    public static boolean regexFiles(String dir, String regex) {
        boolean got = false;
        ArrayList<String> strings = FilesBox.dictoryList(dir);
        ArrayList<String> found = findLine(strings, regex);
        if (found.size() > 0) got = true;
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

