package com.alan.video;

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
        recorder();
        Frame frame = fFmpegFrameGrabber.grabKeyFrame();

        float duration = 0;
        while (frame != null) {
            fFmpegFrameRecorder.record(frame);
            BufferedImage lastImage = java2DFrameConverter.getBufferedImage(frame);
            frame = fFmpegFrameGrabber.grabImage();
            BufferedImage currentImage = java2DFrameConverter.getBufferedImage(frame);
            float like = imagePHash.compareImg(lastImage, currentImage);
            Output.print(frame.imageWidth);
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

    public void recorder() throws Exception{
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
     *
     * @param file：视频路径
     */
    public String getWriteName(String file) throws IOException {
        String[] strings = StringContainer.pathSplit(file);
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

    public void finalize() {
        Output.print(String.format("used time:%s", new Date().getTime() - start));
    }
}