package com.alan.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class Options {
    public static void sleep(int second) {
        try {
            com.alan.util.Output.print("wait for(seconds): " + String.valueOf(second));
            TimeUnit.SECONDS.sleep(second);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String waitForInput() {
        String line = null;
        try {
            System.out.print("please input:");
            InputStream in = System.in;
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "gb2312"));
            line = br.readLine();
            com.alan.util.Output.print(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
}
