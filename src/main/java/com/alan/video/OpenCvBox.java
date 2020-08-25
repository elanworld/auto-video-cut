package com.alan.video;

import com.alan.ai.AiBaseTarget;
import com.alan.cmd.FFmpegCmd;
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
import java.nio.file.Path;

public class OpenCvBox {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    int writeNum = 1;
    double fps;
    double fourcc;
    double duration;
    double count;
    Size frameSize = new Size();

    //need to defile
    double splitHeight = 0.3;
    double smarll = 15;
    double big = 35;

    public OpenCvBox(String file) {
        ImagePHash imagePHash = new ImagePHash();
        VideoCapture videoCapture = new VideoCapture();
        videoCapture.open(file);
        duration = videoCapture.get(Videoio.CAP_PROP_OPENNI_MAX_TIME_DURATION);
        count = videoCapture.get(Videoio.CAP_PROP_FRAME_COUNT);
        fps = videoCapture.get(Videoio.CAP_PROP_FPS);
        fourcc = videoCapture.get(Videoio.CAP_PROP_FOURCC);
        frameSize.width = videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH);
        frameSize.height = videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT);

        AiBaseTarget ai = new AiBaseTarget(splitHeight, big - smarll, (big + smarll) / 2, false);

        FFmpegCmd fFmpegCmd = new FFmpegCmd().setInput(file);
        fFmpegCmd.setting(true,true);
        FFmpegCmd.FiltersSet filtersSet = new FFmpegCmd.FiltersSet();
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
            if (like > splitHeight && clipDuration > smarll || clipDuration > big) {
                String temp = getWriteName(file);
                filtersSet.clear().setCrop(0.8,1);
                fFmpegCmd.setInput(file).setOutput(temp).setFilter_complex(filtersSet).
                        setTime((float) ((double) start / fps), (float) ((double) end / fps)).run();
                filtersSet.clear().setBoxblur(1080,1920);
                fFmpegCmd.clear().setInput(temp).setOutput(getWriteName(file)).setFilter_complex(filtersSet).run();
                try {
                    Files.deleteIfExists(Path.of(temp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                start = end;
                splitHeight = ai.input(clipDuration);
                Output.print("now splitHeight:" + splitHeight);
            }
        }
        Output.print(ai.toString());
    }

    public void writeContainer(String file) {
        VideoWriter videoWriter = new VideoWriter();
        videoWriter.open(getWriteName(file), (int) fourcc, fps, frameSize);
    }

    private static BufferedImage mat2BufferedImage(Mat matrix) throws Exception {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(mob.toArray()));
        return image;
    }

    /**
     * 自动生成新文件名
     *
     * @param file：文件路径
     */
    private String getWriteName(String file) {
        String name = FilesBox.outDirFile(file, writeNum);
        Output.print(name);
        writeNum += 1;
        return name;
    }
}
