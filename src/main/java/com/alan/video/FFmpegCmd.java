package com.alan.video;

import com.alan.util.RunCmd;
import com.alan.util.RunBox;
import com.alan.util.StringContainer;

import java.text.DecimalFormat;
import java.util.*;


public class FFmpegCmd extends RunBox {
    String ffmpeg = "ffmpeg";
    List<String> cmdList;
    LinkedHashMap<String, String> cmdMap;
    List<String> inputFiles;
    String cmdLine;
    RunCmd runCmd;

    boolean wait = true;
    boolean print = true;

    public FFmpegCmd() {
        this.init();
        this.defaultInit();
    }

    public void init() {
        cmdMap = new LinkedHashMap<>();
        inputFiles = new ArrayList<>();
        cmdLine = null;
        cmdList = new ArrayList<>(Arrays.asList("ffmpeg", "overwrite", "hw", "decode", "time_off", "input", "crop",
                "filter_complex", "diyLine", "map", "codec", "bitrate", "output"));
        for (String cmd : cmdList) {
            cmdMap.put(cmd, null);
        }
        cmdMap.replace("ffmpeg", ffmpeg);
        cmdMap.replace("overwrite", "-y");
    }

    public void defaultInit() {
        this.new SpecialFormat().setCodecQSV();
    }

    @Override
    public void setCmdLine(String cmdLine) {
        this.cmdLine = cmdLine;
    }


    public FFmpegCmd runCommand() {
        ArrayList<String> cmds = new ArrayList<>();
        for (String cmd : cmdMap.values()) {
            if (cmd != null) {
                cmds.add(cmd);
            }
        }
        feasible();
        setCmdLine(String.join(" ", cmds));
        runCmd = new RunCmd(cmdLine, 1000, this.wait, this.print);
        if (this.wait) {
            this.new ErrorMatcher().run();
        }
        return this;
    }

    @Override
    public List<String> getResult() {
        ArrayList<String> output = runCmd.getOutput();
        output.addAll(runCmd.getError());
        return output;
    }

    public void setting(boolean wait, boolean print) {
        this.wait = wait;
        this.print = print;
    }

    /**
     * set input file first
     *
     * @param input
     * @return
     */
    public FFmpegCmd setInput(String input) {
        String line = String.format("-i \"%s\"", input);
        inputFiles.add(line);
        cmdMap.replace("input", String.join(" ", inputFiles));
        return this;
    }

    public FFmpegCmd setInput(String input, boolean clearInput) {
        if (clearInput)
            inputFiles.clear();
        return setInput(input);
    }

    public FFmpegCmd setOutput(String output) {
        cmdMap.replace("output", String.format("\"%s\"", output));
        return this;
    }

    public FFmpegCmd setCodec(String codec) {
        cmdMap.replace("codec", "-c:v " + codec);
        return this;
    }

    public FFmpegCmd setTime(double start, double end) {
        cmdMap.replace("time_off", String.format("-ss %s -to %s", start, end));
        return this;
    }

    public FFmpegCmd setCrop(double widthPercent, double heightPercent) {
        cmdMap.replace("crop", String.format("-vf crop=iw*%s:ih*%s:(iw-ow)/2:(ih-oh)/2", widthPercent, heightPercent));
        return this;
    }

    public FFmpegCmd setDiyLine(String diyLine) {
        cmdMap.replace("diyLine", diyLine);
        return this;
    }

    public FFmpegCmd setMap(String diyLine) {
        cmdMap.replace("map", diyLine);
        return this;
    }

    public FFmpegCmd clear() {
        this.init();
        return this;
    }

    private class ErrorMatcher {
        ArrayList<String> out;
        List<String> errors;

        public ErrorMatcher() {
            out = runCmd.getError();
            out.addAll(runCmd.getOutput());
            errors = new ArrayList<>(Arrays.asList(
                    "No such file or directory",
                    "Invalid data found when processing input",
                    "Conversion failed!"
                    ));
        }

        public void run() {
            for (String error : errors) {
                ArrayList<String> noFile = StringContainer.findLine(out, ".*(" + error + ").*");
                if (!noFile.isEmpty())
                    throw new RuntimeException("got error: " + noFile.toString());
            }
        }
    }

    /**
     * add filters to a system to manage
     * just repeat it if filter not set a input or output stream
     */
    public class FiltersSet {
        ArrayList<String> filters;
        String filterLine = "";

        public FiltersSet() {
            filters = new ArrayList<>();
        }

        public FFmpegCmd toFFmpeg() {
            String filterLine = "-filter_complex " + String.join(",", filters);
            filters.clear();
            cmdMap.replace("filter_complex", filterLine);
            return FFmpegCmd.this;
        }

        private FiltersSet clear() {
            filters.clear();
            return this;
        }

        public FiltersSet setLine(String diyLine) {
            filters.add(diyLine);
            return this;
        }

        public FiltersSet setAudioMix() {
            filters.add(String.format("amix=inputs=%s:duration=first:dropout_transition=2", inputFiles.size()));
            return this;
        }

        public FiltersSet setAudioPass(int low, int high) {
            filters.add(String.format("highpass=f=%s,lowpass=f=%s", low, high));
            return this;
        }

        public FiltersSet setAudioVolum(double db) {
            double meanVolume = getInfo().meanVolume;
            double slip = db - meanVolume;
            filters.add(String.format("volume=volume=%sdB", slip));
            return this;
        }

        public FiltersSet setAudioVolumPercent(double percent) {
            filters.add(String.format("volume=volume=%s", percent));
            return this;
        }

        public FiltersSet setAudioLoudnorm() {
            filters.add("loudnorm");
            return this;
        }

        /**
         * add new background with glasses blur from origin video
         *
         * @param width
         * @param height
         * @return
         */
        public FiltersSet setBoxblur(double width, double height) {
            String line = String.format("split=2[a][b];[a]scale=%s:%s,boxblur=20:20[1];" +
                            "[b]scale=%s:ih*%s/iw[2];[1][2]overlay=0:(H-h)/2 -aspect %d:%d",
                    width, height, width, width, (int) width, (int) height);
            filters.add(line);
            return this;
        }

        /**
         * select mult clips to one output
         *
         * @param timeClips
         * @return
         */
        public FiltersSet setSelect(List<List<Double>> timeClips) {
            DecimalFormat decimalFormat = new DecimalFormat("0.000");
            ArrayList<String> selects = new ArrayList<>();
            String selectJoin;
            for (List<Double> time : timeClips) {
                String start = decimalFormat.format(time.get(0));
                String end = decimalFormat.format(time.get(1));
                String line = String.format("between(t,%s,%s)", start, end);
                selects.add(line);
            }
            String join = String.join("+", selects);
            String output = cmdMap.get("output");
            if (output == null)
                throw new RuntimeException("please set output first");
            if (isVideo(output))
                selectJoin = String.format("select='%s',setpts=N/FRAME_RATE/TB;aselect='%s',asetpts=N/SR/TB", join, join);
            else
                selectJoin = String.format("aselect='%s',asetpts=N/SR/TB", join);
            filters.add(selectJoin);
            return this;
        }

        public FiltersSet setCrop(double widthPercent, double heightPercent) {
            filters.add(String.format("crop=iw*%s:ih*%s:(iw-ow)/2:(ih-oh)/2", widthPercent, heightPercent));
            return this;
        }
    }

    private class Metadata {
        double width, height;
        double duration;
        double rate;
        double meanVolume;


        public double getRate() {
            return rate;
        }

        public double getMeanVolume() {
            return meanVolume;
        }

        public void setMeanVolume(double meanVolume) {
            this.meanVolume = meanVolume;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public double getWidth() {
            return width;
        }

        public void setWidth(double width) {
            this.width = width;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "Metadata{" +
                    "width=" + width +
                    ", height=" + height +
                    ", duration=" + duration +
                    ", rate=" + rate +
                    ", meanVolume=" + meanVolume +
                    '}';
        }
    }

    private void feasible() {
        if (cmdMap.get("dcode") != null && cmdMap.get("crop") != null) {
            throw new RuntimeException("can`t set crop and dcode together!");
        }
        if (cmdMap.get("input") == null) {
            throw new RuntimeException("did`t set any input file");
        }
    }

    private Metadata getInfo() {
        Metadata metadata = new Metadata();
        String file = cmdMap.get("input");
        if (file == null) {
            throw new RuntimeException("please set input file first");
        }
        String cmd = String.format("ffmpeg %s -filter_complex volumedetect -f null -", file);
        RunCmd runCmd = new RunCmd(cmd);
        ArrayList<String> outError = runCmd.getError();
        //get duration
        String regex = ".*Duration: (\\d{2}):(\\d{2}):(\\d{2}).(\\d{2}),.*";
        ArrayList<String> found = StringContainer.findLine(outError, regex);
        if (found.size() > 0) {
            int h = Integer.parseInt(found.get(0));
            int m = Integer.parseInt(found.get(1));
            int s = Integer.parseInt(found.get(2));
            int fs = Integer.parseInt(found.get(3));
            metadata.duration = h * 3600 + m * 60 + s + (double) fs / 60;
        }
        //get size
        // regex = ".*Video: .*, (\\d+)x(\\d+) .*, (\\d+) fps,.*";
        regex = ".*Video: .*, (\\d+)x(\\d+) .*, (\\d+).(\\d+) fps,.*";
        found = StringContainer.findLine(outError, regex);
        if (found.size() > 0) {
            metadata.width = Integer.parseInt(found.get(0));
            metadata.height = Integer.parseInt(found.get(1));
            metadata.rate = Double.parseDouble(found.get(2) + "." + found.get(3));
        }

        regex = ".*mean_volume: (.*) dB.*";
        found = StringContainer.findLine(outError, regex);
        if (found.size() > 0) {
            metadata.meanVolume = Double.parseDouble(found.get(0));
        }
        return metadata;
    }

    public Boolean isVideo(String file) {
        ArrayList<String> types = new ArrayList<String>(Arrays.asList("mp4", "avi", "mkv"));
        if (file == null)
            throw new RuntimeException("please set input before this");
        for (String type : types) {
            if (file.matches(".*" + type + ".*"))
                return true;
        }
        return false;
    }


    public class SpecialFormat {

        public SpecialFormat baiduAipPCM() {
            String pcm = "-acodec pcm_s16le -f s16le -ac 1 -ar 16000";
            cmdMap.replace("codec", pcm);
            return this;
        }

        /**
         * set intel qsv codec in order to transform faster than cpu compute
         *
         * @return
         */
        public SpecialFormat setCodecQSV() {
            // cmdMap.replace("hw", "-hwaccel qsv");
            cmdMap.replace("decode", "-c:v h264_qsv");
            cmdMap.replace("codec", "-c:v h264_qsv");
            cmdMap.replace("bitrate", "-b:v 20M");
            return this;
        }
    }
}
