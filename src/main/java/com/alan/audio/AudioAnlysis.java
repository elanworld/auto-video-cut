package com.alan.audio;

import com.alan.output.Output;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.ArrayList;

public class AudioAnlysis {
    private ArrayList<Integer> audioFrames = new ArrayList<Integer>();
    float duration;
    float rate;
    long frameLength;

    public void loadAudio(String file) {
        try {
            new Output(file);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file));
            AudioFormat format = audioInputStream.getFormat();
            rate = format.getFrameRate();
            frameLength = audioInputStream.getFrameLength();
            duration = frameLength / rate;
            byte[] byteFrames = audioInputStream.readNBytes((int) frameLength);
            for (byte b : byteFrames) {
                int frame = b;
                audioFrames.add(frame);
            }
            new Output(audioFrames);
            new Output(audioFrames.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getAudioFrames() {
        return audioFrames;
    }

    public ArrayList<Float> getFramesStamp() {
        //todo reduce Frames memory using
        ArrayList<Float> timeList = new ArrayList<>();
        ArrayList<Float> floats = (ArrayList<Float>) timeList.subList(44,99);
        return floats;
    }
}
