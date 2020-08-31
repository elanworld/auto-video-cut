package com.alan.audio;

import com.alan.util.Output;
import com.alan.util.RunCmd;
import com.alan.util.StringContainer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AudioContainer {
    private ArrayList<Byte> audioFrames = new ArrayList<Byte>();
    public float duration;
    public float rate;
    public int frameLength;

    public void loadAudio(String file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file));
            AudioFormat format = audioInputStream.getFormat();
            rate = format.getFrameRate();
            frameLength = (int) audioInputStream.getFrameLength();
            duration = frameLength / rate;
            Output.print(duration, rate);
            byte[] bytes = new byte[frameLength];
            Output.print(bytes.length, bytes.length);


            //todo get right read method

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadByFFmpeg(String file) {
        String cmd = String.format("ffmpeg -i \"%s\"", file);
        RunCmd runCmd = new RunCmd(cmd);
        ArrayList<String> outError = runCmd.getError();
        String regex = ".*Duration: (\\d{2}):(\\d{2}):(\\d{2}).(\\d{2}),.*";
        ArrayList<String> found = StringContainer.findLine(outError, regex);
        int h = Integer.decode(found.get(0));
        int m = Integer.decode(found.get(1));
        int s = Integer.decode(found.get(2));
        int fs = Integer.decode(found.get(3));
        this.duration = h * 3600 + m * 60 + s + (float) fs / 60;
    }

    public ArrayList<Byte> getAudioFrames() {
        return audioFrames;
    }

    /**
     * get silence clips form speak clips like ([[start,end][start....])
     *
     * @return
     */
    public List<List<Double>> getSilenceFromeSpeak(List<List<Double>> clips) {
        List<List<Double>> silences = new ArrayList<List<Double>>();
        for (int i = 0; i < clips.size() - 1; i++) {
            ArrayList<Double> time = new ArrayList<>();
            time.add(clips.get(i).get(1));
            time.add(clips.get(i + 1).get(0));
            silences.add(time);
        }
        if (silences.isEmpty())
            throw new RuntimeException("fail to get audio time clips from python connection");
        return silences;
    }

    public ArrayList<Double> getAudioTime() {
        int size = audioFrames.size();
        double growth = duration / (size - 1);
        ArrayList<Double> timeList = new ArrayList<Double>();
        double time = 0;
        for (int i = 0; i < size; i += 1) {
            timeList.add(time);
            time += growth;
        }
        return timeList;
    }


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
     *
     * @param data      : audio what has deal with avg method
     * @param timeArray : timestamp synchronous audio
     * @param height    : >0 and <100, judge speaking by number
     */
    private void splitSpeak(ArrayList<Integer> data, ArrayList<Double> timeArray, int height) {
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
                    speakLists.add(timeArray.subList(start, i));
                    start = i;
                    speaked = false;
                    silent = true;
                }
            }
        }
        // last time
        if (speaked = true) {
            speakLists.add(timeArray.subList(start, data.size()));
        } else {
            silentLists.add(timeArray.subList(start, data.size()));
        }
    }

    /**
     * get speaking timestamp like [start,stop,start,stoop...]
     *
     * @return
     */
    private ArrayList<Double> splitTimeClips() {
        ArrayList<Double> clips = new ArrayList<Double>();
        for (List<Double> clip : speakLists) {
            if (clip.size() > 1) {
                clips.add(clip.get(0));
                clips.add(clip.get(clip.size() - 1));
            }
        }
        return clips;

    }

    /**
     * get speak time clips frome audio frames
     *
     * @param audioData
     * @param timeList
     * @return
     */
    public ArrayList<Double> getSpeakClips(ArrayList<Integer> audioData, ArrayList<Double> timeList) {
        powList(audioData);
        ArrayList<Integer> audio = avgData(audioData, 48000);
        ArrayList<Double> time = avgTime(timeList, 48000);
        balancedData(audio);
        System.out.println("audio height: " + audio);
        Output.print(audio.size());
        splitSpeak(audio, time, 30);
        ArrayList<Double> stamps = splitTimeClips();
        return stamps;
    }
}