package com.alan.cmd;

import com.alan.util.RunCmd;
import com.alan.util.StringContainer;

import java.util.*;


public class FFmpegCmd {
    String ffmpeg = "ffmpeg";
    ArrayList<String> cmdList;
    LinkedHashMap<String, String> cmdMap = new LinkedHashMap<>();
    String finalCmdLine;
    ArrayList<String> finalCmds = new ArrayList<>();
    public Size size = new Size();
    public double duration;
    public double rate;

    public FFmpegCmd() {
        cmdList = new ArrayList<String>(Arrays.asList("ffmpeg", "overwrite", "time_off", "input", "crop", "dcode", "output"));
        for (String cmd : cmdList) {
            cmdMap.put(cmd, null);
        }
        cmdMap.replace("ffmpeg", ffmpeg);
        cmdMap.replace("overwrite", "-y");
    }

    public void run() {
        ArrayList<String> cmds = new ArrayList<>();
        for (String cmd : cmdMap.values()) {
            if (cmd != null) {
                cmds.add(cmd);
            }
        }
        feasible();
        finalCmdLine = String.join(" ", cmds);
        finalCmds.add(finalCmdLine);
        new RunCmd(finalCmdLine, 1000, true, false);
    }

    public ArrayList<String> getFinalCmds() {
        return finalCmds;
    }

    public FFmpegCmd setInput(String input) {
        cmdMap.replace("input", String.format("-i \"%s\"", input));
        return this;
    }

    public FFmpegCmd setOutput(String output) {
        cmdMap.replace("output", String.format("\"%s\"", output));
        return this;
    }

    public FFmpegCmd setDcodeCopy() {
        cmdMap.replace("dcode", "-c copy");
        return this;
    }

    public FFmpegCmd setTime(double start, double end) {
        cmdMap.replace("time_off", String.format("-ss %s -to %s", start, end));
        return this;
    }

    public FFmpegCmd setCrop(double widthPercent, double heightPercent) {
        getInfo();
        int width = (int) (size.width * widthPercent);
        int height = (int) (size.height * heightPercent);
        int startWidth = (int) ((size.width - width) / 2);
        int startHeight = (int) ((size.height - height) / 2);
        cmdMap.replace("crop", String.format("-vf crop=%s:%s:%s:%s", width, height, startWidth, startHeight));
        return this;
    }

    private void feasible() {
        if (cmdMap.get("dcode") != null && cmdMap.get("crop") != null) {
            throw new RuntimeException("can`t set crop and dcode together!");
        }
        if (cmdMap.get("input") == null) {
            throw new RuntimeException("did`t set any input file");
        }
    }

    private void getInfo() {
        String file = cmdMap.get("input");
        String cmd = String.format("ffmpeg %s", file);
        RunCmd runCmd = new RunCmd(cmd, 5, true, false);
        ArrayList<String> outError = runCmd.getOutError();
        //get duration
        String regex = ".*Duration: (\\d{2}):(\\d{2}):(\\d{2}).(\\d{2}),.*";
        ArrayList<String> found = StringContainer.findLine(outError, regex);
        if (found.size() > 0) {
            int h = Integer.parseInt(found.get(0));
            int m = Integer.parseInt(found.get(1));
            int s = Integer.parseInt(found.get(2));
            int fs = Integer.parseInt(found.get(3));
            duration = h * 3600 + m * 60 + s + (double) fs / 60;
        }
        //get size
        // regex = ".*Video: .*, (\\d+)x(\\d+) .*, (\\d+) fps,.*";
        regex = ".*Video: .*, (\\d+)x(\\d+) .*, (\\d+).(\\d+) fps,.*";
        found = StringContainer.findLine(outError, regex);
        if (found.size() > 0) {
            size.width = Integer.parseInt(found.get(0));
            size.height = Integer.parseInt(found.get(1));
            rate = Double.parseDouble(found.get(2) + "." + found.get(3));
        }
    }

    private class Size {
        public double width, height;

        public Size() {
            this(0, 0);
        }

        public Size(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "Size{" +
                    "width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    public void cut(String file, String outfile, double start, double end) {
        setInput(file).setOutput(outfile).setTime(start, end);
    }

    public void cutCrop(String file, String outfile, double widthPercent, double heightPercent) {
        finalCmdLine = setInput(file).setOutput(outfile).setCrop(widthPercent, heightPercent).finalCmdLine;
    }

    public void getPCM(String file, String outfile, double start, double end) {
        finalCmdLine = String.format("%s -y -ss %s -t %s -i \"%s\" -acodec pcm_s16le -f s16le -ac 1 -ar 16000 \"%s\"",
                ffmpeg, start, end, file, outfile);
    }

    public void videoLinkAudio(String video, String audio, String outfile, double start, double end) {
        finalCmdLine = String.format("%s -y -i \"%s\" -i \"%s\" -map 0:v:0 -map 1:a:0 -codec copy \"%s\"",
                ffmpeg, video, audio, outfile);
    }
}
