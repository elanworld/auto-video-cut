package com.alan.video;

import com.alan.util.RunCmd;
import com.alan.util.StringBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FFmpegCapturer extends FFmpegCmd {
    CmdInfo cmdInfo = new CmdInfo();
    DevicesInfo devicesInfo;
    ArrayList<String> recordCmdList = new ArrayList<>();
    ArrayList<String> inputDevices = new ArrayList<>();

    public FFmpegCapturer() {
        initCmdList();
        capturerInit();
        cmdMap.clear();
        initCmdMap();

        init();
    }

    private void init() {
        devicesInfo = new DevicesInfo();
        addFormat();

    }

    private void capturerInit() {
        recordCmdList = new ArrayList<>(Arrays.asList(cmdInfo.format, cmdInfo.log,
                cmdInfo.video_size, cmdInfo.framerate, cmdInfo.inputDevices));
        int input = cmdList.indexOf("input");
        cmdList.addAll(input, recordCmdList);
    }

    public FFmpegCapturer runWithInput() {

        ArrayList<String> cmds = new ArrayList<>();
        for (String cmd : cmdMap.values()) {
            if (cmd != null) {
                cmds.add(cmd);
            }
        }
        setCmdLine(String.join(" ", cmds));
        runCmd = new RunCmd(cmdLine, 3600, false, this.print);
        runCmd.input(StringBox.input());
        return this;
    }

    private FFmpegCapturer setCmdMap(String cmdInfo, String line) {
        cmdMap.replace(cmdInfo, line);
        return this;
    }

    private FFmpegCapturer addFormat() {
        setCmdMap(cmdInfo.format, "-f dshow");
        return this;
    }

    public FFmpegCapturer addLog() {
        setCmdMap(cmdInfo.log, "-show_video_device_dialog true -show_audio_device_dialog true");
        return this;
    }

    public FFmpegCapturer addCodecRaw() {
        setCodec("rawvideo");
        return this;
    }

    private void setInputDevices() {
        String join = String.join(":", inputDevices);
        setCmdMap(cmdInfo.inputDevices, "-i " + join);
    }

    public FFmpegCapturer addVideoDevice(String regex) {
        inputDevices.add("video=" + devicesInfo.getDevice(regex));
        setInputDevices();
        return this;
    }

    public FFmpegCapturer addAudioDevice(String regex) {
        setCmdMap(cmdInfo.format, "audio=" + devicesInfo.getDevice(regex));
        setInputDevices();
        return this;
    }

    public class DevicesInfo {
        List<String> devices;

        public DevicesInfo() {
            setCmdMap(cmdInfo.format, "-list_devices true -f dshow -i dummy");
            run();
            List<String> result = getResult();
            devices = StringBox.findGroup(result, "\\[.*\\] *(.*) *");
        }

        public String getDevice(String regex) {
            for (String dev : devices) {
                if (dev.matches(".*" + regex + ".*")) {
                    return dev;
                }
            }
            throw new RuntimeException("not matcher any device by: " + regex);
        }
    }

    private class CmdInfo {
        public String inputDevices = "inputDevice";
        public String format = "format";
        public String log = "log";
        public String video_size = "video_size";
        public String framerate = "framerate";
    }

}
