package com.alan.video;

import com.alan.util.FilesBox;
import com.alan.util.Output;
import com.alan.photo.ImagePHash;
import com.alan.util.StringContainer;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

public class CvContainer {
    Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
    ImagePHash imagePHash = new ImagePHash();
    String file;
    int writeNum;
    FFmpegFrameGrabber fFmpegFrameGrabber;
    FFmpegFrameRecorder fFmpegFrameRecorder;
    float rate;
    long start = new Date().getTime();

    public void grabFrame(String file) throws Exception {
        this.file = file;
        fFmpegFrameGrabber = FFmpegFrameGrabber.createDefault(file);
        fFmpegFrameGrabber.start();
        rate = (float) fFmpegFrameGrabber.getVideoFrameRate();
        java2DFrameConverter = new Java2DFrameConverter();
        int lengthInFrames = fFmpegFrameGrabber.getLengthInFrames();


        float duration = 0;
        for (int i = 0; i < lengthInFrames; i+=2) {
            Frame lastFrame = fFmpegFrameGrabber.grabImage();
            Frame currentFrame = fFmpegFrameGrabber.grabImage();
            if (lastFrame.equals(currentFrame)) {
                Output.print("same " + lastFrame.imageWidth);
            }
            BufferedImage lastImage = java2DFrameConverter.getBufferedImage(lastFrame);
            BufferedImage currentImage = java2DFrameConverter.getBufferedImage(currentFrame);
            float like = imagePHash.compareImg(lastImage, currentImage);
            Output.print(like);
            duration += 1 / rate;
            if (like > 0.38 && duration > 5) {
                fFmpegFrameRecorder.close();
                recorder();
                duration = 0;
            }
        }
        fFmpegFrameRecorder.close();
    }

    private void recorder() throws Exception{
        fFmpegFrameRecorder = new FFmpegFrameRecorder(
                getWriteName(file),
                fFmpegFrameGrabber.getImageWidth(),
                fFmpegFrameGrabber.getImageHeight()
        );
        fFmpegFrameRecorder.setFrameRate(fFmpegFrameGrabber.getFrameRate());
        fFmpegFrameRecorder.setAudioChannels(2);
        fFmpegFrameRecorder.start();

    }

    /**
     * 自动生成新文件名
     * @param file：视频路径
     */
    private String getWriteName(String file) throws IOException {
        String[] strings = FilesBox.pathSplit(file);
        Path name = Path.of(strings[0], strings[1], strings[1] + String.valueOf(writeNum) + strings[2]);
        if (!Files.exists(name.getParent())) {
            Files.createDirectory(name.getParent());
        }
        writeNum += 1;
        Output.print(name);
        return name.toString();
    }

    public void capture(String file) {
        //todo get the right Mat
        VideoCapture videoCapture = new VideoCapture(file);
        Mat mat = new Mat();
        videoCapture.read(mat);
        Output.print(mat.rows());
    }

    @Deprecated
    protected void finalize() {
        Output.print(String.format("used time:%s", new Date().getTime() - start));
    }
}