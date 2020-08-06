package com.alan.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.alan.util.Output;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

//本地语音识别
@Deprecated
public class Sphinx {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
        InputStream stream = new FileInputStream(new File("F:\\Alan\\Videos\\电影\\out_举起手来 HD1280高清国语中字.mp4.wav"));


        recognizer.startRecognition(stream);
        SpeechResult result;
        ArrayList<String> resultList = new ArrayList<String>();

        while ((result = recognizer.getResult()) != null) {
            System.out.format("Hypothesis: %s\n", result.getHypothesis());
            new Output(result.getResult());

        }
        recognizer.stopRecognition();
        Output.print(resultList);
    }


}
