package com.alan.video;

import com.alan.output.Output;
import com.alan.photo.ImagePHash;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;

public class JOpencv {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public JOpencv(String file) {
        VideoCapture videoCapture = new VideoCapture(file);
        videoCapture.open(file);
        boolean opened = videoCapture.isOpened();
        new Output(opened);
        ImagePHash pHash = new ImagePHash();
        for (int i=0;i<50;i++) {
            new Output("frame:"+i);
            Mat mat =new Mat();
            Mat mat2 = new Mat();
            videoCapture.retrieve(mat);
            videoCapture.read(mat2);
            new Output(mat.height());
//            BufferedImage image = (BufferedImage) HighGui.toBufferedImage(mat);
//            BufferedImage image2 = (BufferedImage) HighGui.toBufferedImage(mat2);
//            int num = pHash.compareImg(image, image2);
//            new Output(num);
        }
    }

    public static void main(String[] args) {
        String file = "D:\\Alan\\MOVIE\\Í¶¸å\\ºÚÆ»¹ûMojave¶¯Ì¬±ÚÖ½.mp4";
        new Output(file);
        JOpencv jOpencv = new JOpencv(file);
    }
}
