package com.alan;

import com.alan.audio.AudioContainer;
import com.alan.audio.RosaPy4j;
import com.alan.audio.SoxBox;
import com.alan.video.FFmpegCmd;
import com.alan.util.FilesBox;
import org.python.antlr.ast.Str;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
        FFmpegCmd.FiltersSet filtersSet = fFmpegCmd.new FiltersSet();

        String wav = FilesBox.outExt(file, "wav");
        String speak = FilesBox.outFile(wav, "speak");
        String noise = FilesBox.outFile(wav, "bad");
        String good = FilesBox.outFile(wav, "good");
        String wavBgm = FilesBox.outFile(wav, "good_bgm");
        String bgm1 = FilesBox.outFile(wav, "bgm");
        String outFile1 = FilesBox.outFile(file, "speak-1");
        String outFile = FilesBox.outFile(file, "speak");
        String bgmPath = "F:\\Alan\\Music\\AutoCutBGM\\speak";
        ArrayList<String> bgms = FilesBox.dictoryListFilter(bgmPath, true, "mp3", "wav", "m4a");
        Collections.shuffle(bgms);
        String bgm = bgms.get(0);
        List<String> cleanFiles = new ArrayList<>();
        cleanFiles.addAll(Arrays.asList(wav, speak, noise, good, wavBgm, bgm1, outFile1));

        // generate good voise without noise
        fFmpegCmd.setInput(file).setOutput(speak);
        filtersSet.setSelect(speakClips);
        fFmpegCmd.setFilter_complex(filtersSet).run();
        filtersSet.clear().setSelect(silenceFromeSpeak);
        fFmpegCmd.setOutput(noise).setFilter_complex(filtersSet).run();
        soxBox.noise(noise, speak, good);

        // generate back audio with bgm from good voise
        fFmpegCmd.clear().setInput(bgm);
        filtersSet.clear().setAudioVolum(0.1);
        fFmpegCmd.setFilter_complex(filtersSet).setOutput(bgm1).run();
        fFmpegCmd.clear().setInput(good).setInput(bgm1);
        filtersSet.clear().setAudioMix();
        fFmpegCmd.setFilter_complex(filtersSet).setOutput(wavBgm).run();

        // generate final file
        fFmpegCmd.clear().setInput(file).setOutput(outFile1);
        filtersSet.clear().setSelect(speakClips);
        fFmpegCmd.setFilter_complex(filtersSet).run();
        fFmpegCmd.clear().setInput(outFile1).setInput(wavBgm).setOutput(outFile);
        if (fFmpegCmd.isVideo(file))
            fFmpegCmd.setMap("-map 0:v -map 1:a").run();
        else
            fFmpegCmd.setMap("-map 0:0 -map 1:0").run();
            // todo

        FilesBox.deleteFiles(cleanFiles);

    }

    public static void main(String[] args) {
        String file = "F:\\Alan\\Videos\\Mine\\与我相关\\视频相册\\vlog\\VID_20200826_085711.mp4";
        MainSpeakGrabber mainSpeakGrabber = new MainSpeakGrabber();
        mainSpeakGrabber.run(file);


    }
}
