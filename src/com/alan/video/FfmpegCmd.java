package com.alan.video;

import com.alan.Output;

public class FfmpegCmd {
    String ffmpeg = "ffmpeg";

    public String cut(String file, String outfile, double start, double end) {
        String cmd;
        cmd = String.format("%s -i %s -ss %s -to %s -codec copy %s", ffmpeg, file, start, end, outfile);
        new Output(cmd);
        return cmd;
    }

    public String getPCM(String file, String outfile, double start, double end) {
        String cmd = String.format("%s -y -ss %s -t %s -i %s -acodec pcm_s16le -f s16le -ac 1 -ar 16000 %s", ffmpeg, start, end, file, outfile);
        new Output(cmd);
        return cmd;
    }
}
