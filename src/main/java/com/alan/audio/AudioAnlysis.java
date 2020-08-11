package com.alan.audio;


import com.alan.util.Output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AudioAnlysis {
    ArrayList<List<Double>> speakLists = new ArrayList<List<Double>>();
    ArrayList<List<Double>> silentLists = new ArrayList<List<Double>>();

    /**
     * reduce data by avg
     *
     * @param data: data
     * @param num   : number of datas to one
     * @return : avged data
     */
    private ArrayList<Integer> avgData(ArrayList<Integer> data, int num) {
        ArrayList<Integer> newData = new ArrayList<Integer>();
        int sum = 0;
        int time = 0;
        for (Integer fram : data) {
            time += 1;
            if (time < num) {
                sum += fram;
            } else {
                newData.add(sum);
                sum = 0;
                time = 0;
            }
        }
        return newData;
    }

    private ArrayList<Double> avgTime(ArrayList<Double> timeList, int num) {
        ArrayList<Double> newData = new ArrayList<Double>();
        for (int i = 0; i < timeList.size(); i += num) {
            newData.add(timeList.get(i));
        }
        return newData;
    }

    private void powList(ArrayList<Integer> data) {
        for (int i = 0; i < data.size(); i += 1) {
            Integer d = data.get(i);
            data.set(i, d * d);
        }
    }

    private void balancedData(ArrayList<Integer> data) {
        Integer max = Collections.max(data);
        double banlance = 100.0 / max;
        for (int i = 0; i < data.size(); i += 1) {
            double d = (double) data.get(i);
            data.set(i, (int) (d * banlance));
        }
    }

    /**
     * split the audio to lists what are containing timestamp speaking
     * @param data : audio what has deal with avg method
     * @param timeList : timestamp synchronous audio
     * @param height : >0 and <100, judge speaking by number
     */
    private void splitSpeak(ArrayList<Integer> data, ArrayList<Double> timeList, int height) {
        boolean speaked = false;
        boolean silent = false;
        int start = 0;
        for (int i = 0; i < data.size(); i++) {
            boolean gotSpeaking = data.get(i) > height;
            if (gotSpeaking) {
                speaked = true;
            } else {
                silent = true;
            }
            if (speaked == silent) {
                if (gotSpeaking) {
                    start = i;
                    speaked = true;
                    silent = false;
                } else {
                    speakLists.add(timeList.subList(start, i));
                    start = i;
                    speaked = false;
                    silent = true;
                }
            }
        }
        // last time
        if (speaked = true) {
            speakLists.add(timeList.subList(start, data.size()));
        } else {
            silentLists.add(timeList.subList(start, data.size()));
        }
    }

    /**
     * get speaking timestamp like [start,stop,start,stoop...]
     * @return
     */
    private ArrayList<Double> splitStamp() {
        ArrayList<Double> clips = new ArrayList<Double>();
        for (List<Double> clip : speakLists) {
            if (clip.size() > 1) {
                clips.add(clip.get(0));
                clips.add(clip.get(clip.size() - 1));
            }
        }
        return clips;

    }

    public ArrayList<Double> getSpeakDuration(ArrayList<Integer> audioData, ArrayList<Double> timeList) {
        powList(audioData);
        ArrayList<Integer> audio = avgData(audioData, 48000);
        ArrayList<Double> time = avgTime(timeList, 48000);
        balancedData(audio);
        System.out.println("audio height: " + audio);
        Output.print(audio.size());
        splitSpeak(audio, time, 30);
        ArrayList<Double> stamps = splitStamp();
        return stamps;
    }

    public static void main(String[] args) {
        String file = "F:\\Alan\\Videos\\µÁ”∞\\speak.wav";
        AudioContainer audioContainer = new AudioContainer();
        audioContainer.loadAudio(file);
        ArrayList<Integer> audioFrames = audioContainer.getAudioFrames();
        ArrayList<Double> audioTime = audioContainer.getAudioTime();

        AudioAnlysis anlysis = new AudioAnlysis();
        ArrayList<Double> speakDuration = anlysis.getSpeakDuration(audioFrames, audioTime);
        Output.print(speakDuration);

    }
}
