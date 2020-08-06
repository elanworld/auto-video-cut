package com.alan.cmd;

import com.alan.util.Output;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class RunCmd {
    public ArrayList<String> output = new ArrayList<String>();
    public ArrayList<String> outError = new ArrayList<String>();
    StreamOut streamOut;
    StreamOut streamError;
    Process p;
    public boolean stop = true;
    public int timeout = 60;

    public RunCmd(String command) {
        runCmd(command);
    }

    public RunCmd(String command,int timeout, boolean wait) {
        stop = wait;
        this.timeout = timeout;
        runCmd(command);
    }

    public void runCmd(String command) {
        try {
            p = Runtime.getRuntime().exec(command);
            new Output("runing cmd: " + command);
            new KillCmd(p, timeout).start();
            streamOut = new StreamOut(p.getInputStream());
            streamError = new StreamOut(p.getErrorStream());
            if (stop) {
                streamOut.getResul();
                streamError.getResul();
            }
            new Output("finished cmd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getOutput() {
        return streamOut.getResul();
    }

    public ArrayList<String> getOutError() {
        return streamError.getResul();
    }
}

class StreamOut extends Thread {
    public Thread t;
    InputStream input;
    public ArrayList<String> outList = new ArrayList<String>();
    boolean print = true;

    public StreamOut(InputStream in) {
        input = in;
        t = new Thread(this, getClass().getName());
        t.start();
    }

    public ArrayList<String> getResul() {
        try {
            while (true) {
                Thread.sleep(100);
                if (!t.isAlive()) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outList;
    }


    public void run() {
        outList = getOutput(input);
    }

    public ArrayList<String> getOutput(InputStream in) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "gb2312"));
            String line;
            while ((line = br.readLine()) != null) {
                if (print) {
                    Output.print(line);
                }
                list.add(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

class KillCmd extends Thread {
    Thread thread;
    private Process process;
    int time;

    public KillCmd(Process p, int timeout) {
        process = p;
        time = timeout;
        thread = new Thread(this);
        thread.start();

    }

    public void run() {
        try {
            long start = new Date().getTime();
            while (true) {
                Thread.sleep(1000);
                long duration = new Date().getTime() - start;
                boolean timeout = duration > 1000 * time;
                if (process.isAlive() && timeout) {
                    Output.print("destroy the process: timeout " + time);
                    process.destroy();
                    break;
                } else if (!process.isAlive()) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
