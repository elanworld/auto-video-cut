package com.alan.cmd;

import com.alan.output.Output;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunCmd {
    public RunCmd(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            new Output("cmd is going...");
            InputStream in = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"gb2312"));
            while (br.read() != -1) {
                System.out.println(br.readLine());
            }
            in.close();
            new Output("cmd is closed");
        } catch (IOException e) {
            new Output("error");
            e.printStackTrace();
        }
    }
}
