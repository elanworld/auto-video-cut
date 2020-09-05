package com.alan.video;

import com.alan.ai.AiBaseTarget;
import com.alan.photo.ImagePHash;
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

public class OpenCvBox {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    double fps;
    double fourcc;
    double duration;
    double count;
    Size frameSize = new Size();

    //need to defile
    double splitHeight = 0.3;
    double small = 15;
    double big = 35;

    public OpenCvBox() {
        Output.setLog(false);
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
        FFmpegCmd.FiltersSet filtersSet = fFmpegCmd.new FiltersSet();
        String temp = getWriteName(file);
        String out = getWriteName(file);
        if (Files.exists(Paths.get(out)))
            return false;
        filtersSet.setCrop(0.8, 1).toFFmpeg();
        fFmpegCmd.setInput(file).setOutput(temp).
                setTime((float) ((double) start / fps), (float) ((double) end / fps)).runCommand().clear();
        filtersSet.setBoxblur(1080, 1920).toFFmpeg();
        fFmpegCmd.clear().setInput(temp).setOutput(out).runCommand().clear();
        try {
            Files.deleteIfExists(Paths.get(temp));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    private static BufferedImage mat2BufferedImage(Mat matrix) throws Exception {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(mob.toArray()));
        return image;
    }

    private String getWriteName(String file) {
        String name = FilesBox.outDirFile(file);
        Output.print(name);
        return name;
    }
}
