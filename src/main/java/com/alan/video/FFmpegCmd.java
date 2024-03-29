package com.alan.video;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import com.alan.common.text.FileSuffixEnum;
import com.alan.common.util.Output;
import com.alan.common.util.RunCmd;
import com.alan.common.util.StringBox;

public class FFmpegCmd extends RunCmd {
	private String ffmpeg = "ffmpeg";
	List<FFmpegEnum> cmdList = new ArrayList<>();
	LinkedHashMap<FFmpegEnum, String> cmdMap = new LinkedHashMap<>();
	List<String> inputFiles = new ArrayList<>();
	// 后处理任务列表
	List<Runnable> runTasks = new ArrayList<>();

	FiltersSet filtersSet = null;
	SpecialFormat specialFormat = null;

	public FFmpegCmd() {
		this.initCmdList();
		this.initCmdMap();
	}

	@Override
	public void run() {
		feasible(); // check all setting if possible
		runTasks.forEach(Runnable::run);
		runTasks.clear();
		ArrayList<String> cmds = new ArrayList<>();
		for (String cmd : cmdMap.values()) {
			if (cmd != null) {
				cmds.add(cmd);
			}
		}
		this.command = String.join(" ", cmds);
		Output.setFilePrint(false);
		super.run();
	}

	protected void initCmdList() {
		cmdList.addAll(Arrays.asList(FFmpegEnum.ffmpeg, FFmpegEnum.format, FFmpegEnum.overwrite, FFmpegEnum.hw,
				FFmpegEnum.decode, FFmpegEnum.time_off, FFmpegEnum.input, FFmpegEnum.crop, FFmpegEnum.filter_complex,
				FFmpegEnum.diyLine, FFmpegEnum.map, FFmpegEnum.codec, FFmpegEnum.out_format, FFmpegEnum.bitrate,
				FFmpegEnum.output));
	}

	protected void initCmdMap() {
		for (FFmpegEnum cmd : cmdList) {
			cmdMap.put(cmd, null);
		}
		cmdMap.replace(FFmpegEnum.ffmpeg, ffmpeg);
		cmdMap.replace(FFmpegEnum.overwrite, "-y");
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
		cmdMap.replace(FFmpegEnum.input, String.join(" ", inputFiles));
		return this;
	}

	public FFmpegCmd setInput(String input, boolean clearInput) {
		if (clearInput) {
			inputFiles.clear();
		}
		return setInput(input);
	}

	public FFmpegCmd setOutput(String output) {
		cmdMap.replace(FFmpegEnum.output, String.format("\"%s\"", output));
		return this;
	}

	public FFmpegCmd setCodec(String codec) {
		cmdMap.replace(FFmpegEnum.codec, "-c:v " + codec);
		return this;
	}

	public FFmpegCmd setCodecCopy() {
		return setCodec("copy");
	}

	/**
	 * set intel qsv codec in order to transform faster than cpu compute
	 *
	 * @return
	 */
	public FFmpegCmd setCodecQSV() {
		this.runTasks.add(() -> {
			double rate = this.new Metadata().getInfo().rate;
			// 输出使用GPU编码codec，输入不确定，使用cpu
			cmdMap.replace(FFmpegEnum.codec, "-c:v h264_qsv");
			if (rate != 0) {
				cmdMap.replace(FFmpegEnum.bitrate, String.format("-b:v %sK", (int) rate));
			}
		});
		return this;
	}

	public FFmpegCmd restoreQsv() {
		this.clear(FFmpegEnum.bitrate, FFmpegEnum.decode, FFmpegEnum.codec);
		return this;
	}

	public FFmpegCmd setTime(double start, double end) {
		cmdMap.replace(FFmpegEnum.time_off, String.format("-ss %s -to %s", start, end));
		return this;
	}

	public FFmpegCmd setCrop(double widthPercent, double heightPercent) {
		cmdMap.replace(FFmpegEnum.crop,
				String.format("-vf crop=iw*%s:ih*%s:(iw-ow)/2:(ih-oh)/2", widthPercent, heightPercent));
		return this;
	}

	public FFmpegCmd setDiyLine(String diyLine) {
		cmdMap.replace(FFmpegEnum.diyLine, diyLine);
		return this;
	}

	public FFmpegCmd setMap(String diyLine) {
		cmdMap.replace(FFmpegEnum.map, diyLine);
		return this;
	}

	public FFmpegCmd clear() {
		this.initCmdList();
		this.initCmdMap();
		this.inputFiles.clear();
		return this;
	}

	public FFmpegCmd clear(FFmpegEnum... fFmpegEnums) {
		Arrays.stream(fFmpegEnums).forEach(n -> {
			cmdMap.replace(n, "");
		});
		return this;
	}

	public FiltersSet getFiltersSet() {
		if (filtersSet == null) {
			filtersSet = new FiltersSet();
		}
		return filtersSet;
	}

	public SpecialFormat getSpecialFormat() {
		if (specialFormat == null) {
			specialFormat = new SpecialFormat();
		}
		return specialFormat;
	}

	public Boolean isVideo(String file) {
		if (file == null) {
			return false;
		}
		return Arrays.stream(FileSuffixEnum.video()).anyMatch(name -> file.matches(".*" + name + ".*"));
	}

	public boolean isWait() {
		return wait;
	}

	/**
	 * check the command feasible change to correct if possible
	 */
	private void feasible() {
		if (cmdMap.get("codec") != null && cmdMap.get("crop") != null) {
			throw new RuntimeException("can`t set crop and codec together!");
		}
	}

	private class ErrorMatcher {
		ArrayList<String> out;
		List<String> errors;

		public ErrorMatcher() {
			out.addAll(getError());
			out.addAll(getOutput());
			errors = new ArrayList<>(Arrays.asList("No such file or directory",
					"Invalid data found when processing input", "Conversion failed!"));
		}

		public void run() {
			for (String error : errors) {
				List<String> noFile = StringBox.findGroup(out, ".*(" + error + ").*");
				if (!noFile.isEmpty()) {
					Output.print(command);
					throw new RuntimeException("got error: " + noFile.toString());
				}
			}
		}
	}

	/**
	 * add filters to a system to manage just repeat it if filter not set a input or
	 * output stream
	 */
	public class FiltersSet {
		ArrayList<String> filters;
		String filterLine = "";

		public FiltersSet() {
			filters = new ArrayList<>();
		}

		/**
		 * apply all filter setting
		 *
		 * @return
		 */
		public FFmpegCmd toFFmpegCmd() {
			String filterLine = String.format("-filter_complex \"%s\"", String.join(",", filters));
			filters.clear();
			cmdMap.replace(FFmpegEnum.filter_complex, filterLine);
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

		/**
		 * mix first audio to the second,first audio is bgm ,request input file first
		 *
		 * @return
		 */
		public FiltersSet setAudioMix() {
			filters.add(String.format("aloop=loop=3:size=2e+09,amix=inputs=%s:duration=shortest:dropout_transition=2",
					inputFiles.size()));
			return this;
		}

		public FiltersSet setAudioPass(int low, int high) {
			filters.add(String.format("highpass=f=%s,lowpass=f=%s", low, high));
			return this;
		}

		public FiltersSet setAudioVolume(double db) {
			double meanVolume = toFFmpegCmd().new Metadata().getVolume().meanVolume;
			double slip = db - meanVolume;
			filters.add(String.format("volume=volume=%sdB", slip));
			return this;
		}

		public FiltersSet setAudioVolumePercent(double percent) {
			filters.add(String.format("volume=volume=%s", percent));
			return this;
		}

		public FiltersSet setAudioLoudnorm() {
			filters.add("loudnorm");
			return this;
		}

		/**
		 * select mult clips to one output request out files first
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
			if (output == null) {
				throw new RuntimeException("please set output first");
			}
			if (isVideo(output)) {
				selectJoin = String.format("select='%s',setpts=N/FRAME_RATE/TB;aselect='%s',asetpts=N/SR/TB", join,
						join);
			} else {
				selectJoin = String.format("aselect='%s',asetpts=N/SR/TB", join);
			}
			filters.add(selectJoin);
			return this;
		}

		public FiltersSet setCrop(double widthPercent, double heightPercent) {
			filters.add(String.format("crop=iw*%s:ih*%s:(iw-ow)/2:(ih-oh)/2", widthPercent, heightPercent));
			return this;
		}

		/**
		 * add new background with glasses blur from origin video warming :replaced map
		 *
		 * @param width
		 * @param height
		 * @return
		 */
		public FiltersSet setBoxblur(double width, double height) {
			String line = String.format("split=2[a][b];[a]scale=%s:%s,boxblur=20:20[1];"
					+ "[b]scale=%s:ih*%s/iw[2];[1][2]overlay=0:(H-h)/2", width, height, width, width);
			filters.add(line);
			FFmpegCmd.this.setMap(String.format("-aspect %d:%d", (int) width, (int) height));
			return this;
		}

		public FiltersSet setSubtitle(String file) {
			file = file.replace("\\", "/").replace(":", "\\:");
			filters.add(String.format("subtitles=filename='%s':force_style='Fontsize=%s'", file, 20));
			return this;
		}
	}

	public class SpecialFormat {

		public SpecialFormat baiduAipPCM() {
			String pcm = "-acodec pcm_s16le -f s16le -ac 1 -ar 16000";
			cmdMap.replace(FFmpegEnum.codec, pcm);
			return this;
		}
	}

	public Metadata getMetadata() {
		return this.new Metadata().getInfo();
	}

	public class Metadata {
		public double width, height;
		public double duration;
		public double rate;
		public double meanVolume;

		private Metadata() {
			if (cmdMap.get(FFmpegEnum.input) == null) {
				throw new RuntimeException("please set input file first");
			}
		}

		private Metadata getInfo() {
			String file = cmdMap.get(FFmpegEnum.input);
			String cmd = String.format("ffmpeg %s", file);
			RunCmd runCmd = new RunCmd(cmd);
			ArrayList<String> out = runCmd.getError();
			out.addAll(runCmd.getOutput());
			// get duration
			String regex = ".*Duration: (\\d{2}):(\\d{2}):(\\d{2}).(\\d{2}),.*";
			List<String> found = StringBox.findGroup(out, regex);
			if (found.size() == 4) {
				int h = Integer.parseInt(found.get(0));
				int m = Integer.parseInt(found.get(1));
				int s = Integer.parseInt(found.get(2));
				int fs = Integer.parseInt(found.get(3));
				duration = h * 3600 + m * 60 + s + (double) fs / 60;
			}
			// get size
			regex = ".*Stream.*Video: .*, (\\d+)x(\\d+), .*";
			found = StringBox.findGroup(out, regex);
			if (found.size() == 2) {
				width = Integer.parseInt(found.get(0));
				height = Integer.parseInt(found.get(1));
			}
			regex = ".*Duration:.* bitrate: (\\d+) kb/s";
			found = StringBox.findGroup(out, regex);
			if (found.size() == 1) {
				rate = Double.parseDouble(found.get(0));
			}
			return this;
		}

		private Metadata getVolume() {
			String file = cmdMap.get(FFmpegEnum.input);
			String cmd = String.format("ffmpeg %s -filter_complex volumedetect -f null -", file);
			RunCmd runCmd = new RunCmd(cmd);
			ArrayList<String> out = runCmd.getError();
			out.addAll(runCmd.getOutput());

			String regex = ".*mean_volume: (.*) dB.*";
			List<String> found = StringBox.findGroup(out, regex);
			if (found.size() > 0) {
				meanVolume = Double.parseDouble(found.get(0));
			}
			return this;
		}

	}
}
