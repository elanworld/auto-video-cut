package com.alan.audio;

import com.alan.cmd.RunCmd;
import com.alan.util.Output;
import com.alan.util.StringContainer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        ArrayList<Float> floats = (ArrayList<Float>) timeList.subList(44, 99);
        return floats;
    }

    public float loadByFFmpeg(String file) {
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
        return duration;
    }
}
