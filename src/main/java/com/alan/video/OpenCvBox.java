package com.alan.video;

import com.alan.ai.AiBaseTarget;
import com.alan.photo.ImagePHash;
import com.alan.text.SubtitleBox;
import com.alan.util.FilesBox;
import com.alan.util.Output;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class OpenCvBox {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    double fps;
    double fourcc;
    double duration;
    double count;
    Size frameSize = new Size();
    SubtitleBox subtitleBox;

    //need to defile
    double splitHeight = 0.3;
    double small = 15;
    double big = 35;

    public OpenCvBox() {
        Output.setLog(false);
        subtitleBox = new SubtitleBox();
    }

    public void recognition(String file) {
        ImagePHash imagePHash = new ImagePHash();
        VideoCapture videoCapture = new VideoCapture();
        videoCapture.open(file);
        duration = videoCapture.get(Videoio.CAP_PROP_OPENNI_MAX_TIME_DURATION);
        count = videoCapture.get(Videoio.CAP_PROP_FRAME_COUNT);
        fps = videoCapture.get(Videoio.CAP_PROP_FPS);
        fourcc = videoCapture.get(Videoio.CAP_PROP_FOURCC);
        frameSize.width = videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        frameSize.height = videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);

        AiBaseTarget ai = new AiBaseTarget(splitHeight, big - small, (big + small) / 2, false);

        FFmpegCmd fFmpegCmd = new FFmpegCmd();
        fFmpegCmd.setting(true, true);
        Mat mat = new Mat();
        String lastHash;
        String currentHash = null;
        int start = 0;
        int end = 0;
        for (int i = 0; i < (int) count; i++) {
            end += 1;
            videoCapture.read(mat);
            lastHash = currentHash;
            float like = -1;
            try {
                currentHash = imagePHash.getHash(mat2BufferedImage(mat));
                if (lastHash == null) {
                    continue;
                }
                like = imagePHash.distance(lastHash, currentHash);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            // Output.print(like, currentHash, lastHash);

            double clipDuration = (end - start) / fps;
            if (like > splitHeight && clipDuration > small || clipDuration > big) {
                clipper(file, fFmpegCmd, start, end);
                start = end;
                splitHeight = ai.input(clipDuration);
                Output.print("now splitHeight:" + splitHeight);
            }
        }
        Output.print(ai.toString());
    }

    public void write(String file) {
        VideoWriter videoWriter = new VideoWriter();
        videoWriter.open(getWriteName(file), (int) fourcc, fps, frameSize);
    }

    private boolean clipper(String file, FFmpegCmd fFmpegCmd, int start, int end) {
        double tStart =  start / fps;
        double tEnd = end / fps;
        FFmpegCmd.FiltersSet filtersSet = fFmpegCmd.new FiltersSet();
        String temp = getWriteName(file);
        String out = getWriteName(file);

        if (Files.exists(Paths.get(out)))
            return false;
        // out subtitle
        String srtOut = out.replace(".mp4", ".srt");
        boolean exist = subtitleClip(file, srtOut, tStart, tEnd);

        filtersSet.setCrop(0.8, 1).toFFmpegCmd().setInput(file).setOutput(temp).setCodecQSV()
                .setTime(tStart, tEnd).run().clear();
        if (exist)
            filtersSet.setSubtitle(srtOut);
        filtersSet.setBoxblur(1080,1920).toFFmpegCmd()
                .setInput(temp).setOutput(out).setCodecQSV().run().clear();
        try {
            Files.deleteIfExists(Paths.get(temp));
            if (exist) {
                Files.deleteIfExists(Paths.get(srtOut));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean subtitleClip(String file, String srtOut, double start ,double end) {
        String srt = file.replace(".mp4", ".srt");
        if (!Paths.get(srt).toFile().exists())
            return false;
        subtitleBox.init(srt);
        List<String> byFilter = subtitleBox.getByFilter(true, false, true, start, end, -start);
        return subtitleBox.write(byFilter,srtOut);
    }

    private static BufferedImage mat2BufferedImage(Mat matrix) throws Exception {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(mob.toArray()));
        return image;
    }

    private String getWriteName(String file) {
        return FilesBox.outDirFile(file);
    }
}
