package com.alan.video;

import com.alan.util.FilesBox;
import com.alan.util.Output;
import com.alan.photo.ImagePHash;
import org.bytedeco.javacv.*;

import java.awt.image.BufferedImage;
import java.util.Date;

public class CvContainer {
    FFmpegFrameGrabber fFmpegFrameGrabber;
    FFmpegFrameRecorder fFmpegFrameRecorder;
    Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
    ImagePHash imagePHash = new ImagePHash();
    String file;
    float rate;
    long start = new Date().getTime();

    public void run(String file) {
        try {
            grabFrame(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void audioRead(String file) throws Exception {
        fFmpegFrameGrabber = FFmpegFrameGrabber.createDefault(file);
        fFmpegFrameGrabber.start();
        for (int i = 0; i<10;i++) {
            Frame frame = fFmpegFrameGrabber.grabFrame(true, false, false, true);
            Output.print(frame);
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
                FilesBox.outDirFile(file),
                fFmpegFrameGrabber.getImageWidth(),
                fFmpegFrameGrabber.getImageHeight()
        );
        fFmpegFrameRecorder.setFrameRate(fFmpegFrameGrabber.getFrameRate());
        fFmpegFrameRecorder.setAudioChannels(2);
        fFmpegFrameRecorder.start();

    }

    @Deprecated
    protected void finalize() {
        Output.print(String.format("used time:%s", new Date().getTime() - start));
    }
}