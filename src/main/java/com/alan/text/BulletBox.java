package com.alan.text;

import java.sql.ResultSet;
import java.util.*;

import com.alan.common.data.Sqlite3Box;

public class BulletBox implements BulletBoxInterface {
	Sqlite3Box sql;
	String db = "G:\\Alan\\Documents\\OneDrive\\Documents\\program\\resources\\text_box.db";
	List<Integer> episodes;
	String keyWord;
	int unitDuration;

	public BulletBox() {
		sql = new Sqlite3Box(this.db);
	}

	/**
	 * get bullet like [[episode,timePoint,bullet]...]
	 *
	 * @param word
	 * @return
	 */
	@Override
	public List<List<Object>> getBullet(String word) {
		List<List<Object>> bullets = new ArrayList<>();
		String cmd = String.format("select * from qingyunian where bullet like '%%%s%%'", word);
		ResultSet resultSet = sql.runSql(cmd, true);
		try {
			while (resultSet.next()) {
				Integer episode = resultSet.getInt("episode");
				Long timePoint = resultSet.getLong("time_point");
				String bulletText = resultSet.getString("bullet");
				List<Object> bullet = Arrays.asList(episode, timePoint, bulletText);
				bullets.add(bullet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bullets;
	}

	public <T> HashMap<Integer, HashMap<Long, List<String>>> getByEpisode(List<List<T>> data) {
		HashMap<Integer, HashMap<Long, List<String>>> box = new HashMap<>();
		for (List<T> bullet : data) {
			Integer episode = (Integer) bullet.get(0);
			Long timePoint = (Long) bullet.get(1);
			String bulletText = (String) bullet.get(2);
			HashMap<Long, List<String>> episodeBox = box.get(episode);
			if (episodeBox == null) {
				episodeBox = new HashMap<>();
			}
			List<String> bulletTexts = episodeBox.get(timePoint);
			if (bulletTexts == null) {
				bulletTexts = new ArrayList<>();
			}
			bulletTexts.add(bulletText);
			episodeBox.put(timePoint, bulletTexts);
			box.put(episode, episodeBox);

		}
		return box;
	}

	/**
	 * get data like {episode:{timePoint:size}...} filter not exists episode filter
	 * press close to timePoint
	 *
	 * @param data
	 * @return
	 */
	public HashMap<Integer, HashMap<Long, Integer>> getHashData(HashMap<Integer, HashMap<Long, List<String>>> data) {
		HashMap<Integer, HashMap<Long, Integer>> box = new HashMap<>();
		for (Map.Entry<Integer, HashMap<Long, List<String>>> entry : data.entrySet()) {
			Integer episode = entry.getKey();
			HashMap<Long, List<String>> timePoints = entry.getValue();
			HashMap<Long, Integer> bulletBox = new HashMap<>();
			if (containsEpisode(episode, episodes)) {
				List<Long> times = new ArrayList<>();
				for (Map.Entry<Long, List<String>> bulletEntry : timePoints.entrySet()) {
					Long timePoint = bulletEntry.getKey();
					int size = bulletEntry.getValue().size();
					boolean lonely = true;
					for (Long time : times) {
						if (timePoint < time + unitDuration & timePoint > time - 5) {
							lonely = false;
							break;
						}
					}
					if (lonely) {
						bulletBox.put(timePoint, size);
						times.add(timePoint);
					}
				}
				box.put(episode, bulletBox);
			}
		}
		return box;
	}

	/**
	 * get data waht is sorted by bullet num like [[episode,start,end]...]
	 *
	 * @param data
	 * @return
	 */
	public List<List<Long>> sortedHashData(HashMap<Integer, HashMap<Long, Integer>> data) {
		List<List<Long>> box = new LinkedList<>();
		List<List<Long>> sorted = new LinkedList<>();
		for (Map.Entry<Integer, HashMap<Long, Integer>> entry : data.entrySet()) {
			long episode = entry.getKey().longValue();
			HashMap<Long, Integer> value = entry.getValue();
			for (Map.Entry<Long, Integer> bullet : value.entrySet()) {
				Long timePoint = bullet.getKey();
				long num = bullet.getValue().longValue();
				sorted.add(Arrays.asList(episode, timePoint, num));
			}
		}
		sorted.sort((o1, o2) -> o2.get(2).compareTo(o1.get(2)));
		for (List<Long> longs : sorted) {
			long start = longs.get(1) - 5;
			long end = longs.get(1) + 5;
			if (start < 0) {
				start = 0;
			}
			box.add(Arrays.asList(longs.get(0), start, end));
		}
		return box;
	}

	/**
	 * get sorted clip like [[episode,timePoint,num]]
	 *
	 * @param data
	 * @return
	 */
	public List<List<Long>> sortedListNum(HashMap<Integer, HashMap<Long, List<String>>> data) {
		List<List<Long>> box = new ArrayList<>();
		for (Map.Entry<Integer, HashMap<Long, List<String>>> entry : data.entrySet()) {
			Integer episode = entry.getKey();
			long epi = episode.longValue();
			HashMap<Long, List<String>> bullet = entry.getValue();
			for (Map.Entry<Long, List<String>> bulletEntry : bullet.entrySet()) {
				Long timePoint = bulletEntry.getKey();
				Integer size = bulletEntry.getValue().size();
				long sizeLong = size.longValue();
				box.add(Arrays.asList(epi, timePoint, sizeLong));
			}
			Collections.sort(box, new Comparator<List<Long>>() {
				@Override
				public int compare(List<Long> o1, List<Long> o2) {
					return o2.get(2).compareTo(o1.get(2));
				}
			});
		}
		return box;
	}

	public boolean containsEpisode(Integer episode, List<Integer> episodes) {
		if (episodes.contains(episode)) {
			return true;
		}
		return false;
	}

	public List<Integer> getEpisodes() {
		return episodes;
	}

	public void setEpisodes(List<Integer> episodes) {
		this.episodes = episodes;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	/**
	 * get most important clip like [[episode,start,end],[episode,start,end]...]
	 *
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> List<List<Long>> getClip() {
		return sortedHashData(getHashData(getByEpisode(getBullet(keyWord))));
	}
}
