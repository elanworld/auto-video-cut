package com.alan.ai;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.alan.common.util.Output;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;

//本地语音识别
@Deprecated
public class Sphinx {
	public static void main(String[] args) throws Exception {

		Configuration configuration = new Configuration();

		configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
		configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
		configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

		StreamSpeechRecognizer streamSpeechRecognizer = new StreamSpeechRecognizer(configuration);
		InputStream stream = new FileInputStream(new File("F:\\Alan\\Videos\\电影\\speak.wav"));

		streamSpeechRecognizer.startRecognition(stream);
		SpeechResult result;
		ArrayList<String> resultList = new ArrayList<String>();
		Output.print(stream.available());

		while ((result = streamSpeechRecognizer.getResult()) != null) {
			System.out.format("Hypothesis: %s\n", result.getHypothesis());
			for (WordResult word : result.getWords()) {
				String w = word.getWord().toString();
				long start = word.getTimeFrame().getStart();
				Output.print(w, start);
				resultList.add(w);
			}

		}
		streamSpeechRecognizer.stopRecognition();
		Output.print(resultList);
	}

}
