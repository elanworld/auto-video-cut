package com.alan.video;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alan.util.StringBox;

public class FFmpegCapture extends FFmpegCmd {
	DevicesInfo devicesInfo;
	ArrayList<FFmpegEnum> recordCmdList = new ArrayList<>();
	ArrayList<String> inputDevices = new ArrayList<>();

	public FFmpegCapture() {
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
		recordCmdList = new ArrayList<>(Arrays.asList(FFmpegEnum.format, FFmpegEnum.log, FFmpegEnum.video_size,
				FFmpegEnum.framerate, FFmpegEnum.inputDevice));
		int input = cmdList.indexOf(FFmpegEnum.input);
		cmdList.addAll(input, recordCmdList);
	}

	public FFmpegCapture runWithInput() {

		ArrayList<String> cmds = new ArrayList<>();
		for (String cmd : cmdMap.values()) {
			if (cmd != null) {
				cmds.add(cmd);
			}
		}
		this.command = String.join(" ", cmds);
		setTimeout(Duration.ofMinutes(10L));
		setWait(false);
		input(StringBox.input());
		return this;
	}

	private FFmpegCapture setCmdMap(FFmpegEnum cmdInfo, String line) {
		cmdMap.replace(cmdInfo, line);
		return this;
	}

	private FFmpegCapture addFormat() {
		setCmdMap(FFmpegEnum.format, "-f dshow");
		return this;
	}

	public FFmpegCapture addLog() {
		setCmdMap(FFmpegEnum.log, "-show_video_device_dialog true -show_audio_device_dialog true");
		return this;
	}

	public FFmpegCapture addCodecRaw() {
		setCodec("rawvideo");
		return this;
	}

	private void setInputDevices() {
		String join = String.join(":", inputDevices);
		setCmdMap(FFmpegEnum.inputDevice, "-i " + join);
	}

	public FFmpegCapture addVideoDevice(String regex) {
		inputDevices.add("video=" + devicesInfo.getDevice(regex));
		setInputDevices();
		return this;
	}

	public FFmpegCapture addAudioDevice(String regex) {
		setCmdMap(FFmpegEnum.format, "audio=" + devicesInfo.getDevice(regex));
		setInputDevices();
		return this;
	}

	public class DevicesInfo {
		List<String> devices;

		public DevicesInfo() {
			setCmdMap(FFmpegEnum.format, "-list_devices true -f dshow -i dummy");
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
}
