package com.alan.audio;

import com.alan.util.Output;
import com.alan.util.RunCmd;
import com.alan.util.StringContainer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AudioContainer {
    private ArrayList<Integer> audioFrames = new ArrayList<Integer>();
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
            byte[] bytes = new byte[(int)frameLength];
            audioInputStream.read(bytes);
            //todo get right read method
            Output.print(duration);
            for (byte frame : bytes) {
                audioFrames.add((int)frame);
            }
            Output.print(audioFrames.size()/rate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadByFFmpeg(String file) {
        String cmd = String.format("ffmpeg -i \"%s\"", file);
        RunCmd runCmd = new RunCmd(cmd);
        ArrayList<String> outError = runCmd.outError;
        String line = StringContainer.findLine(outError, "Duration");
        Output.print(line);
        String pattern = ".*Duration: (\\d{2}):(\\d{2}):(\\d{2}).(\\d{2}),.*";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        try {
            if (matcher.find()) {
                int h = Integer.decode(matcher.group(1));
                int m = Integer.decode(matcher.group(2));
                int s = Integer.decode(matcher.group(3));
                int fs = Integer.decode(matcher.group(4));
                duration = h * 3600 + m * 60 + s + fs / 60;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getAudioFrames() {
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