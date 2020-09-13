package com.alan;

import com.alan.audio.AudioContainer;
import com.alan.audio.RosaPy4j;
import com.alan.audio.SoxBox;
import com.alan.video.FFmpegCmd;
import com.alan.util.FilesBox;

import java.util.*;

public class MainSpeakClipper {
    String bgmPath = "F:\\Alan\\Music\\AutoCutBGM\\speak";
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
        List<String> cleanFiles = new ArrayList<>(Arrays.asList(good, bad, soxOut, soxOutDnorm, bgmGenerate, speakWithBgm, temp));
        ArrayList<String> bgms = FilesBox.dictoryListFilter(bgmPath, true, "mp3", "wav", "m4a");
        String bgm = bgms.get(0);

        // generate background music to good voice
        fFmpegCmd.setInput(bgm).setOutput(bgmGenerate);
        filtersSet.setAudioLoudnorm().setAudioVolumPercent(0.2).toFFmpegCmd();
        fFmpegCmd.run().clear();

        // generate speak voice
        fFmpegCmd.clear().setInput(file).setOutput(good);
        filtersSet.setSelect(speakClips).toFFmpegCmd().run().clear();

        if (noise) {
            fFmpegCmd.setInput(file).setOutput(bad);
            filtersSet.setSelect(silenceFromeSpeak).toFFmpegCmd().run().clear();
            soxBox.noise(bad, good, soxOut);
        } else {
            filtersSet.setAudioLoudnorm().toFFmpegCmd().
                    setInput(good).setOutput(soxOut).run().clear();
        }

        filtersSet.setAudioLoudnorm().setAudioVolumPercent(1).toFFmpegCmd().
                setInput(soxOut).setOutput(soxOutDnorm).run().clear();

        fFmpegCmd.setInput(bgmGenerate).setInput(soxOutDnorm).setOutput(speakWithBgm);
        filtersSet.setAudioMix().toFFmpegCmd().run().clear();

        // generate final file
        fFmpegCmd.setCodecQSV().setInput(file).setOutput(temp);
        filtersSet.setSelect(speakClips).toFFmpegCmd().run().clear();

        fFmpegCmd.setCodecQSV();
        if (fFmpegCmd.isVideo(file)) {
            fFmpegCmd.setInput(temp).setInput(speakWithBgm).setOutput(speak).setCodec("copy").setMap("-map 0:v -map 1:a").run();
        } else
            fFmpegCmd.setInput(speakWithBgm).setOutput(speak).run();

        FilesBox.deleteFiles(cleanFiles);

    }

    public static void main(String[] args) {
        String dir = "F:\\Alan\\Videos\\Mine\\与我相关\\vlog";
        for (String file : FilesBox.dictoryListFilter(dir, false, "")) {
            MainSpeakClipper mainSpeakClipper = new MainSpeakClipper();
            mainSpeakClipper.run(file);
        }
    }
}
