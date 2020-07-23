package com.alan.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunCmd {
    public RunCmd(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            InputStream in = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"gb2312"));
            while (br.read() != -1) {
                System.out.println(br.readLine());
            }
            in.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
