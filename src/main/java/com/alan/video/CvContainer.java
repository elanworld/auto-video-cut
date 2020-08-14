package com.alan.video;

import com.alan.util.FilesBox;
import com.alan.util.Output;
import com.alan.photo.ImagePHash;
import org.bytedeco.javacv.*;

import java.awt.image.BufferedImage;
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

    public void run(String file) {
        try {
            grabFrame(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void grabFrame(String file) throws Exception {
        this.file = file;
        fFmpegFrameGrabber = FFmpegFrameGrabber.createDefault(file);
        fFmpegFrameGrabber.start();
        rate = (float) fFmpegFrameGrabber.getVideoFrameRate();
        java2DFrameConverter = new Java2DFrameConverter();
        int lengthInFrames = fFmpegFrameGrabber.getLengthInFrames();

        recorder();
        String lastHash;
        String currentHash = null;
        float duration = 0;
        for (int i = 0; i < lengthInFrames; i+=1) {
            lastHash = currentHash;
            Frame frame = fFmpegFrameGrabber.grabImage();
            fFmpegFrameRecorder.record(frame);
            BufferedImage image = java2DFrameConverter.getBufferedImage(frame);
            currentHash = imagePHash.getHash(image);
            if (lastHash == null) continue;
            float like = imagePHash.distance(lastHash, currentHash);
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
    private String getWriteName(String file) {
        String name = FilesBox.outDirFile(file, writeNum);
        Output.print(name);
        writeNum += 1;
        return name;
    }

    @Deprecated
    protected void finalize() {
        Output.print(String.format("used time:%s", new Date().getTime() - start));
    }
}