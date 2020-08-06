package com.alan.cmd;

import java.nio.file.Path;
import java.nio.file.Paths;


public class FFmpegCmd {
    String ffmpeg = "ffmpeg";

    public String cut(String file, String outfile, float start, float end) {
        return String.format("%s -y -i \"%s\" -ss %s -to %s -codec copy \"%s\"",
                ffmpeg, file, start, end, outfile);
    }

    public String cutSize(String file, String outfile, float width, float height, float startWidth, float startHeight) {
        return String.format("%s -y -i \"%s\" -vf crop=%s:%s:%s:%s \"%s\"",
                ffmpeg, file, width, height, startWidth, startHeight, outfile);
    }

    public String cutAudio(String file, String outfile, float start, float end) {
        return String.format("%s -y -ss %s -to %s -i \"%s\" -codec copy \"%s\"",
                ffmpeg, start, end, file, outfile);
    }

    public String getPCM(String file, String outfile, float start, float end) {
        return String.format("%s -y -ss %s -t %s -i \"%s\" -acodec pcm_s16le -f s16le -ac 1 -ar 16000 \"%s\"",
                ffmpeg, start, end, file, outfile);
    }

    public String videoLinkAudio(String video, String audio, String outfile, float start, float end) {
        return String.format("%s -y -i \"%s\" -i \"%s\" -map 0:v:0 -map 1:a:0 -codec copy \"%s\"",
                ffmpeg, video, audio, outfile);
    }
}
