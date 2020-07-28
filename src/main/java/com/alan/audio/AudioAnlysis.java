package com.alan.audio;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.spi.AudioFileReader;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.text.Format;
import java.util.Enumeration;

public class AudioAnlysis {
    public void getAudio(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file));
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            float rate = format.getFrameRate();
            int duration = Math.round(frames / rate);
            System.out.println(rate);
            AudioFormat.Encoding enc = format.getEncoding();
            byte [] aub = audioInputStream.readNBytes(20);
            for (byte b : aub) {
                System.out.println(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
