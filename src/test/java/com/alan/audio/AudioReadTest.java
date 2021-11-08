package com.alan.audio;

import com.alan.video.CvContainer;

public class AudioReadTest {
	AudioContainer audioContainer = new AudioContainer();
	String file = "F:\\Alan\\Videos\\Mine\\aa.wav";

	public void load() {
		audioContainer.loadAudio(file);
	}

	public void opencv() throws Exception {
		CvContainer cvContainer = new CvContainer();
		cvContainer.audioRead(file);
	}

	public void py_read() {
		new RosaPy4j().getSpeakClips(file);
	}
}
