package com.alan.cmd;

import com.alan.output.Output;

import java.io.*;
import java.util.ArrayList;

public class RunCmd {
    public RunCmd(String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            new Output("cmd: "+command);
            p.waitFor();
            getOutput(p.getInputStream());
            new Output("erroline");
            getOutput(p.getErrorStream());
            new Output("cmd is closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getOutput(InputStream in) throws UnsupportedEncodingException, IOException {
        ArrayList<String> list = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "gb2312"));
        while (br.read() != -1) {
            System.out.println(br.readLine());
            list.add(br.readLine());
        }
        in.close();
        return list;
    }
}
