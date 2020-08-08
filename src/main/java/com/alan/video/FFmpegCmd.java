package com.alan.cmd;

import com.alan.util.CaptionException;
import com.alan.util.Output;
import com.alan.util.RunCmd;

import java.util.*;


public class FFmpegCmd {
    String ffmpeg = "ffmpeg";
    ArrayList<String> cmdList;
    LinkedHashMap<String, String> cmdMap = new LinkedHashMap<>();
    String finalCmdLine;

    public FFmpegCmd() {
        cmdList = new ArrayList<String>(Arrays.asList("ffmpeg", "overwrite", "time_off", "input", "crop", "dcode", "output"));
        for (String cmd : cmdList) {
            cmdMap.put(cmd, null);
        }
        cmdMap.replace("ffmpeg", ffmpeg);
        cmdMap.replace("overwrite", "-y");
    }

    public void run(){
        ArrayList<String> cmds = new ArrayList<>();
        for (String cmd : cmdMap.values()) {
            if (cmd != null) {
                cmds.add(cmd);
            }
        }
        finalCmdLine = String.join(" ", cmds);
        try {
            feasible();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new RunCmd(finalCmdLine, 1000, true);
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

    public FFmpegCmd setTime(float start, float end) {
        cmdMap.replace("time_off", String.format("-ss %s -to %s", start, end));
        return this;
    }

    public FFmpegCmd setCrop(float width, float height, float startWidth, float startHeight) {
        cmdMap.replace("crop", String.format("-vf crop=%s:%s:%s:%s", width, height, startWidth, startHeight));
        return this;
    }

    private void feasible() throws Exception{
        if (cmdMap.get("dcode") != null && cmdMap.get("crop") != null) {
            throw new CaptionException("can`t set crop and dcode together!");
        }
    }

    public void cut(String file, String outfile, float start, float end) {
        setInput(file).setOutput(outfile).setTime(start, end);
    }

    public void cutCrop(String file, String outfile, float width, float height, float startWidth, float startHeight) {
        finalCmdLine = setInput(file).setOutput(outfile).setCrop(width, height, startWidth, startHeight).finalCmdLine;
    }

    public void getPCM(String file, String outfile, float start, float end) {
        finalCmdLine = String.format("%s -y -ss %s -t %s -i \"%s\" -acodec pcm_s16le -f s16le -ac 1 -ar 16000 \"%s\"",
                ffmpeg, start, end, file, outfile);
    }

    public void videoLinkAudio(String video, String audio, String outfile, float start, float end) {
        finalCmdLine = String.format("%s -y -i \"%s\" -i \"%s\" -map 0:v:0 -map 1:a:0 -codec copy \"%s\"",
                ffmpeg, video, audio, outfile);
    }
}
