package com.alan.text;

import com.alan.util.FilesBox;
import com.alan.util.StringContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubtitleBox implements SubtitleBoxInterface {
    List<SubtitleBody> subtitleBodies;

    @Override
    public List<String> getClip(double start, double end) {
        ArrayList<String> subtitles = new ArrayList<>();
        List<SubtitleBody> filters = filters(subtitleBodies, true, true, true, start, end);
        for (SubtitleBody body : filters) {
            subtitles.add(body.toBody());
        }
        return subtitles;
    }

    @Override
    public List<String> getEnglish() {
        ArrayList<String> subtitles = new ArrayList<>();
        List<SubtitleBody> filters = filters(subtitleBodies, false, true, false, 0, 0);
        for (SubtitleBody body : filters) {
            subtitles.add(body.toBody());
        }
        return subtitles;
    }

    @Override
    public List<String> getChinese() {
        ArrayList<String> subtitles = new ArrayList<>();
        List<SubtitleBody> filters = filters(subtitleBodies, true, false, false, 0, 0);
        for (SubtitleBody body : filters) {
            subtitles.add(body.toBody());
        }
        return subtitles;
    }

    public SubtitleBox init(String file) {
        subtitleBodies = read(file);
        return this;
    }

    private List<SubtitleBody> read(String file) {
        ArrayList<String> reader = FilesBox.reader(file);
        List<SubtitleBody> subtitleBodies = new ArrayList<>();
        Pattern num = Pattern.compile("^(\\d{1,4})$");
        Pattern time = Pattern.compile("(\\d+):(\\d+):(\\d+),(\\d+) --> (\\d+):(\\d+):(\\d+),(\\d+)");
        SubtitleBody subtitleBody = new SubtitleBody();
        for (String line : reader) {
            Matcher numMt = num.matcher(line);
            Matcher timeMt = time.matcher(line);
            boolean nFind = numMt.find();
            boolean tFind = timeMt.find();
            if (nFind) {
                subtitleBodies.add(subtitleBody.clone());
                subtitleBody.init();
                subtitleBody.setNum(Integer.parseInt(numMt.group(1)));
            }
            if (tFind) {
                int h = Integer.parseInt(timeMt.group(1));
                int m = Integer.parseInt(timeMt.group(2));
                int s = Integer.parseInt(timeMt.group(3));
                int ms = Integer.parseInt(timeMt.group(4));
                int h1 = Integer.parseInt(timeMt.group(5));
                int m1 = Integer.parseInt(timeMt.group(6));
                int s1 = Integer.parseInt(timeMt.group(7));
                int ms1 = Integer.parseInt(timeMt.group(8));
                double start = h * 60 * 60 + m * 60 + s + ms / 60.0;
                double end = h1 * 60 * 60 + m1 * 60 + s1 + ms1 / 60.0;
                subtitleBody.setStart(start);
                subtitleBody.setEnd(end);
            }
            if ((!nFind & !tFind)) {
                subtitleBody.addText(line);
            }
        }
        return subtitleBodies;
    }

    private boolean checkChinese(String word) {
        return StringContainer.checkChinese(word);
    }

    public SubtitleBody filter(SubtitleBody subtitleBody, boolean chinese, boolean english, boolean clip, double start, double end) {
        if (clip) {
            if (subtitleBody.start >= start & subtitleBody.end <= end) {
            } else {
                return null;
            }
        }
        List<String> text = subtitleBody.text;
        for (int i = text.size() - 1; i > -1; i--) {
            if (!chinese)
                if (checkChinese(text.get(i)))
                    text.remove(i);
            if (!english)
                if (!checkChinese(text.get(i)))
                    text.remove(i);
        }
        if (text.isEmpty())
            return null;
        return subtitleBody;
    }

    public List<SubtitleBody> filters(List<SubtitleBody> bodies,
                                      boolean chinese, boolean english, boolean clip, double start, double end) {
        ArrayList<SubtitleBody> newBodies = new ArrayList<>();
        for (SubtitleBody body : bodies) {
            SubtitleBody filter = filter(body, chinese, english, clip, start, end);
            if (filter != null)
                newBodies.add(filter);
        }
        return newBodies;
    }

    public List<String> getByFilter(boolean chinese, boolean english, boolean clip, double start, double end) {
        ArrayList<String> subtitles = new ArrayList<>();
        List<SubtitleBody> filters = filters(subtitleBodies, chinese, english, clip, start, end);
        for (SubtitleBody body : filters) {
            subtitles.add(body.toBody());
        }
        return subtitles;
    }

    public boolean write(List<String> subtitle, String file) {
        if (subtitle.isEmpty())
            return false;
        FilesBox.writer(subtitle,file);
        return true;
    }

}
