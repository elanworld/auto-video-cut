package com.alan.audio;

import com.alan.util.Output;
import com.alan.video.CvContainer;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

public class AudioReadTest {
    AudioContainer audioContainer = new AudioContainer();
    String file = "F:\\Alan\\Videos\\Mine\\aa.wav";

    @Test
    public void load() {
        audioContainer.loadAudio(file);
    }


    @Test
    public void opencv() throws Exception {
        CvContainer cvContainer = new CvContainer();
        cvContainer.audioRead(file);
    }

    @Test
    public void py_read() {
        new RosaPy4j().getSpeakClips(file);
    }
}
