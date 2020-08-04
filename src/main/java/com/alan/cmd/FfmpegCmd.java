package com.alan.cmd;

import com.alan.output.Output;
import com.alan.utils.FilePath;

import java.io.File;
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

    public String changeAudio(String video, String audio, String outfile, float start, float end) {
        return String.format("%s -y -i \"%s\" -i \"%s\" -map 0:v:0 -map 1:a:0 -codec copy \"%s\"",
                ffmpeg, video, audio, outfile);
    }
    public static void main(String args[]) {
        FFmpegCmd fFmpegCmd = new FFmpegCmd();
        String file = "F:\\Alan\\Videos\\电影\\举起手来 HD1280高清国语中字.mp4";
        String outPath = new FFmpegCmd().getOutPath(file);
        new Output(outPath);
//        String outfile = fFmpegCmd.getOutPath(file);
//        double start = 57*60+38;
//        double end = start + 80;
//        new RunCmd(fFmpegCmd.cut(file, outfile, start, end));
    }
}
