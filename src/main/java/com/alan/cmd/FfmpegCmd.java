package com.alan.cmd;

import com.alan.output.Output;

import java.nio.file.Path;
import java.nio.file.Paths;


public class FFmpegCmd {
    String ffmpeg = "ffmpeg";

    public String cut(String file, String outfile, float start, float end) {
        return String.format("%s -y -i \"%s\" -ss %s -to %s -codec copy \"%s\"",
                ffmpeg, file, start, end, outfile);
    }

    public String cutAudio(String file, String outfile, float start, float end) {
        return String.format("%s -y -ss %s -to %s -i \"%s\" -codec copy \"%s\"",
                ffmpeg, start, end, file, outfile);
    }


    public String getPCM(String file, String outfile, float start, float end) {
        return String.format("%s -y -ss %s -t %s -i \"%s\" -acodec pcm_s16le -f s16le -ac 1 -ar 16000 \"%s\"",
                ffmpeg, start, end, file, outfile);
    }

    public String getOutPath(String inputPath) {
        Path path = Paths.get(inputPath);
        Path parent = path.getParent();
        Path fileName = path.getFileName();
        Path outPath = Paths.get(parent.toString(), "out_" + fileName.toString());
        return outPath.toString();
    }

    public String videoLinkAudio(String video, String audio, String outfile, float start, float end) {
        return String.format("%s -y -i \"%s\" -i \"%s\" -map 0:v:0 -map 1:a:0 -codec copy \"%s\"",
                ffmpeg, video, audio, outfile);
    }
}
