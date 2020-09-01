package com.alan;

import com.alan.audio.AudioContainer;
import com.alan.audio.RosaPy4j;
import com.alan.audio.SoxBox;
import com.alan.video.FFmpegCmd;
import com.alan.util.FilesBox;

import java.util.*;

public class MainSpeakClipper {
    boolean noise = false;

    RosaPy4j rosaPy4j;
    AudioContainer audioContainer;
    FFmpegCmd fFmpegCmd;
    SoxBox soxBox;

    public MainSpeakClipper() {
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
        String good = FilesBox.outFile(wav, "good");
        String bad = FilesBox.outFile(wav, "bad");
        String soxOut = FilesBox.outFile(wav, "soxOut");
        String soxOutDnorm = FilesBox.outFile(wav, "soxOutDnorm");
        String bgmGenerate = FilesBox.outFile(wav, "bgmBack");
        String speakWithBgm = FilesBox.outFile(wav, "speakWithBgm");
        String temp = FilesBox.outFile(file, "temp");
        String speak = FilesBox.outFile(file, "speak");
        String bgmPath = "F:\\Alan\\Music\\AutoCutBGM\\speak";
        List<String> cleanFiles = new ArrayList<>(Arrays.asList(good, bad, soxOut,soxOutDnorm ,bgmGenerate, speakWithBgm, temp));
        ArrayList<String> bgms = FilesBox.dictoryListFilter(bgmPath, true, "mp3", "wav", "m4a");
        String bgm = bgms.get(0);

        // generate background music to good voice
        fFmpegCmd.clear().setInput(bgm).setOutput(bgmGenerate);
        filtersSet.clear().setAudioLoudnorm().setAudioVolumPercent(0.2);
        fFmpegCmd.setFilter_complex(filtersSet).run();

        // generate good voice without noise
        fFmpegCmd.clear().setInput(file).setOutput(good);
        filtersSet.setSelect(speakClips);
        fFmpegCmd.setFilter_complex(filtersSet).run();

        if (noise) {
            filtersSet.clear().setSelect(silenceFromeSpeak);
            fFmpegCmd.setOutput(bad).setFilter_complex(filtersSet).run();
            soxBox.noise(bad, good, soxOut);
        } else {
            filtersSet.clear().setAudioLoudnorm();
            fFmpegCmd.clear().setInput(good).setOutput(soxOut).setFilter_complex(filtersSet).run();
        }

        filtersSet.clear().setAudioLoudnorm().setAudioVolumPercent(1);
        fFmpegCmd.clear().setInput(soxOut).setOutput(soxOutDnorm).setFilter_complex(filtersSet).run();

        fFmpegCmd.clear().setInput(soxOutDnorm).setInput(bgmGenerate).setOutput(speakWithBgm);
        filtersSet.clear().setAudioMix();
        fFmpegCmd.setFilter_complex(filtersSet).run();

        // generate final file
        fFmpegCmd.clear().setInput(file).setOutput(temp);
        filtersSet.clear().setSelect(speakClips);
        fFmpegCmd.setFilter_complex(filtersSet).run();
        if (fFmpegCmd.isVideo(file))
            fFmpegCmd.clear().setInput(temp).setInput(speakWithBgm).setOutput(speak).setMap("-map 0:v -map 1:a").run();
        else
            fFmpegCmd.clear().setInput(speakWithBgm).setOutput(speak).run();

        FilesBox.deleteFiles(cleanFiles);

    }

    public static void main(String[] args) {
        String file = "F:\\Alan\\Videos\\Mine\\与我相关\\视频相册\\vlog\\VID_20200826_085711.mp4";
        file = FilesBox.inputIfNotExists(file);
        MainSpeakClipper mainSpeakClipper = new MainSpeakClipper();
        mainSpeakClipper.run(file);


    }
}
