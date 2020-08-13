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
    double splitHeight = 0.2;
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

        AiBaseTarget ai = new AiBaseTarget(splitHeight, big - smarll, (big - smarll) / 2);

        FFmpegCmd fFmpegCmd = new FFmpegCmd();
        fFmpegCmd.setInput(file);
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
                if (i == 0) {
                    continue;
                }
                like = imagePHash.distance(lastHash, currentHash);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            double clipDuration = (end - start) / fps;
            if (like > splitHeight && clipDuration > smarll || clipDuration > big) {
                fFmpegCmd.setOutput(getWriteName(file)).
                        setTime((float) ((double) start / fps), (float) ((double) end / fps)).setDcodeCopy().run();
                start = end;
                splitHeight = ai.input(clipDuration);
                Output.print("now splitHeight:" + splitHeight);
            }
        }
        Output.print(fFmpegCmd.getFinalCmds());
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
     * @param file：视频路径
     */
    private String getWriteName(String file) {
        String name = FilesBox.outDirFile(file, writeNum);
        Output.print(name);
        writeNum += 1;
        return name;
    }
}
