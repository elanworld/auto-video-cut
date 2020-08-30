package com.alan;

import com.alan.audio.AudioContainer;
import com.alan.audio.RosaPy4j;
import com.alan.audio.SoxBox;
import com.alan.cmd.FFmpegCmd;
import com.alan.util.FilesBox;

import java.util.List;

public class MainSpeakGrabber {
    RosaPy4j rosaPy4j;
    AudioContainer audioContainer;
    FFmpegCmd fFmpegCmd;
    SoxBox soxBox;

    public MainSpeakGrabber() {
        rosaPy4j = new RosaPy4j();
        audioContainer = new AudioContainer();
         fFmpegCmd = new FFmpegCmd();
         soxBox = new SoxBox();
    }

    public void run(String file) {
        List<List<Double>> speakClips = rosaPy4j.getSpeakClips(file);
        List<List<Double>> silenceFromeSpeak = audioContainer.getSilenceFromeSpeak(speakClips);
        fFmpegCmd.setInput(file);
        FFmpegCmd.FiltersSet filtersSet = fFmpegCmd.new FiltersSet();
        String speak = FilesBox.outFile(file, "speak");
        String noise = FilesBox.outFile(file, "noise");
        String out = FilesBox.outFile(file, "out");
        filtersSet.setSelect(speakClips);
        fFmpegCmd.setOutput(speak).setFilter_complex(filtersSet).run();
        filtersSet.clear().setSelect(silenceFromeSpeak);
        fFmpegCmd.setOutput(noise).setFilter_complex(filtersSet).run();
        soxBox.noise(noise,speak,out);


    }

    public static void main(String[] args) {
        String file = "F:\\Alan\\Videos\\Mine\\aa.wav";
        MainSpeakGrabber mainSpeakGrabber = new MainSpeakGrabber();
        mainSpeakGrabber.run(file);


    }
}
