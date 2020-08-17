package com.alan.audio;

import com.alan.paint.Graph;
import com.alan.util.Output;
import com.alan.util.RunCmd;
import com.alan.util.StringContainer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioContainer {
    private ArrayList<Byte> audioFrames = new ArrayList<Byte>();
    public float duration;
    public float rate;
    public long frameLength;

    public void loadAudio(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file));
            AudioFormat format = audioInputStream.getFormat();
            rate = format.getFrameRate();
            frameLength = audioInputStream.getFrameLength();
            duration = frameLength / rate;
            byte[] bytes = new byte[(int) frameLength];

            Output.print(format.getSampleRate(),format.getSampleSizeInBits(),format.getFrameRate(),format.getFrameSize());

            //todo get right read method

            Output.print(audioFrames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadByFFmpeg(String file) {
        String cmd = String.format("ffmpeg -i \"%s\"", file);
        RunCmd runCmd = new RunCmd(cmd);
        ArrayList<String> outError = runCmd.getOutError();
        String regex = ".*Duration: (\\d{2}):(\\d{2}):(\\d{2}).(\\d{2}),.*";
        ArrayList<String> found = StringContainer.findLine(outError, regex);
        int h = Integer.decode(found.get(0));
        int m = Integer.decode(found.get(1));
        int s = Integer.decode(found.get(2));
        int fs = Integer.decode(found.get(3));
        this.duration = h * 3600 + m * 60 + s + (float) fs / 60;
    }

    public ArrayList<Byte> getAudioFrames() {
        return audioFrames;
    }

    public ArrayList<Double> getAudioTime() {
        int size = audioFrames.size();
        double growth = duration / (size - 1);
        ArrayList<Double> timeList = new ArrayList<Double>();
        double time = 0;
        for (int i = 0; i < size; i += 1) {
            timeList.add(time);
            time += growth;
        }
        return timeList;
    }
}