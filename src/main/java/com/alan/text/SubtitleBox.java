package com.alan.text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alan.util.FilesBox;
import com.alan.util.StringBox;

/**
 * @Description: srt字幕工具
 * @Author: Alan
 * @Date: 2021/3/6
 */
public class SubtitleBox implements SubtitleBoxInterface {
	List<SubtitleBody> subtitleBodies;

	/**
	 * 初始化
	 *
	 * @param file
	 * @return
	 */
	public SubtitleBox init(String file) {
		subtitleBodies = read(file);
		return this;
	}

	@Override
	public List<String> getAll() {
		ArrayList<String> subtitles = new ArrayList<>();
		List<SubtitleBody> filters = filters(subtitleBodies, true, true, false, 0, 0, 0);
		for (SubtitleBody body : filters) {
			subtitles.add(body.toBody());
		}
		return subtitles;
	}

	@Override
	public List<String> getClip(double start, double end) {
		ArrayList<String> subtitles = new ArrayList<>();
		List<SubtitleBody> filters = filters(subtitleBodies, true, true, true, start, end, 0);
		for (SubtitleBody body : filters) {
			subtitles.add(body.toBody());
		}
		return subtitles;
	}

	@Override
	public List<String> getEnglish() {
		ArrayList<String> subtitles = new ArrayList<>();
		List<SubtitleBody> filters = filters(subtitleBodies, false, true, false, 0, 0, 0);
		for (SubtitleBody body : filters) {
			subtitles.add(body.toBody());
		}
		return subtitles;
	}

	@Override
	public List<String> getChinese() {
		ArrayList<String> subtitles = new ArrayList<>();
		List<SubtitleBody> filters = filters(subtitleBodies, true, false, false, 0, 0, 0);
		for (SubtitleBody body : filters) {
			subtitles.add(body.toBody());
		}
		return subtitles;
	}

	public void forEach(Consumer<SubtitleBody> consumer) {
		subtitleBodies.forEach(consumer);
	}

	public List<SubtitleBody> read(String file) {
		List<String> reader = FilesBox.reader(file);
		List<SubtitleBody> subtitleBodies = new ArrayList<>();
		String timeFormat = "(\\d+):(\\d+):(\\d+),(\\d+)";
		Pattern num = Pattern.compile("^(\\d{1,4})$");
		Pattern time = Pattern.compile(String.format("%s.*-->.*%s", timeFormat, timeFormat));
		SubtitleBody subtitleBody = new SubtitleBody();
		for (String line : reader) {
			Matcher numMat = num.matcher(line);
			Matcher timeMat = time.matcher(line);
			boolean isNum = numMat.find();
			boolean isTime = timeMat.find();
			if (isNum) {
				subtitleBodies.add(subtitleBody.clone());
				subtitleBody.init();
				subtitleBody.setNum(Integer.parseInt(numMat.group(1)));
			}
			if (isTime) {
				int h = Integer.parseInt(timeMat.group(1));
				int m = Integer.parseInt(timeMat.group(2));
				int s = Integer.parseInt(timeMat.group(3));
				int ms = Integer.parseInt(timeMat.group(4));
				int h1 = Integer.parseInt(timeMat.group(5));
				int m1 = Integer.parseInt(timeMat.group(6));
				int s1 = Integer.parseInt(timeMat.group(7));
				int ms1 = Integer.parseInt(timeMat.group(8));
				double start = h * 60 * 60 + m * 60 + s + ms / 1000.0;
				double end = h1 * 60 * 60 + m1 * 60 + s1 + ms1 / 1000.0;
				subtitleBody.setStart(start);
				subtitleBody.setEnd(end);
			}
			if ((!isNum & !isTime)) {
				if (!line.equals("")) {
					subtitleBody.addText(line);
				}
			}
		}
		return subtitleBodies;
	}

	private boolean checkChinese(String word) {
		return StringBox.checkChinese(word);
	}

	public SubtitleBody filter(SubtitleBody subtitleBody, boolean chinese, boolean english, boolean clip, double start,
			double end, double delay) {
		if (clip) {
			if (subtitleBody.start < start || subtitleBody.end > end) {
				return null;
			}
		}
		if (delay != 0) {
			subtitleBody.setStart(subtitleBody.getStart() + delay);
			subtitleBody.setEnd(subtitleBody.getEnd() + delay);
		}
		List<String> text = subtitleBody.text;
		for (int i = text.size() - 1; i > -1; i--) {
			if (!chinese) {
				if (checkChinese(text.get(i))) {
					text.remove(i);
				}
			}
			if (!english) {
				if (!checkChinese(text.get(i))) {
					text.remove(i);
				}
			}
		}
		if (text.isEmpty()) {
			return null;
		}
		return subtitleBody;
	}

	public List<SubtitleBody> filters(List<SubtitleBody> bodies, boolean chinese, boolean english, boolean clip,
			double start, double end, double delay) {
		ArrayList<SubtitleBody> newBodies = new ArrayList<>();
		for (SubtitleBody body : bodies) {
			SubtitleBody filter = filter(body, chinese, english, clip, start, end, delay);
			if (filter != null) {
				newBodies.add(filter);
			}
		}
		return newBodies;
	}

	public List<String> getByFilter(boolean chinese, boolean english, boolean clip, double start, double end,
			double delay) {
		ArrayList<String> subtitles = new ArrayList<>();
		List<SubtitleBody> filters = filters(subtitleBodies, chinese, english, clip, start, end, delay);
		for (SubtitleBody body : filters) {
			subtitles.add(body.toBody());
		}
		return subtitles;
	}

	public boolean write(List<String> subtitle, String file) {
		if (subtitle.isEmpty()) {
			return false;
		}
		FilesBox.writer(subtitle, file);
		return true;
	}

	public List<SubtitleBody> delay(double time) {
		for (SubtitleBody subtitleBody : subtitleBodies) {
			subtitleBody.setStart(subtitleBody.getStart() + time);
			subtitleBody.setEnd(subtitleBody.getEnd() + time);
		}
		return subtitleBodies;
	}

	public List<SubtitleBody> getSubtitleBodies() {
		return subtitleBodies;
	}

}
