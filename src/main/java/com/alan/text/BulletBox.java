package com.alan.text;

import com.alan.data.Sqlite3Box;
import org.omg.CORBA.OBJ_ADAPTER;
import org.python.antlr.ast.Str;

import java.sql.ResultSet;
import java.util.*;

public class BulletBox implements BulletBoxInterface {
    Sqlite3Box sql;
    String db = "G:\\Alan\\Documents\\OneDrive\\Documents\\program\\resources\\text_box.db";

    public BulletBox() {
        sql = new Sqlite3Box(this.db);
    }

    @Override
    public List<List<Object>> getBullet(String word) {
        List<List<Object>> bullets = new ArrayList<>();
        String cmd = String.format("select * from qingyunian where episode = 1", word);
        // String cmd = String.format("select * from qingyunian where bullet like '%%%s%%'", word);
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

    public HashMap<Integer, HashMap<Long, Integer>> getBulletNum(HashMap<Integer, HashMap<Long, List<String>>> data) {
        HashMap<Integer, HashMap<Long, Integer>> box = new HashMap<>();
        for (Map.Entry<Integer, HashMap<Long, List<String>>> entry : data.entrySet()) {
            Integer episode = entry.getKey();
            HashMap<Long, List<String>> bullet = entry.getValue();
            HashMap<Long, Integer> bulletBox = new HashMap<>();
            for (Map.Entry<Long, List<String>> bulletEntry : bullet.entrySet()) {
                Long timePoint = bulletEntry.getKey();
                int size = bulletEntry.getValue().size();
                bulletBox.put(timePoint, size);
            }
            box.put(episode, bulletBox);
        }
        return box;
    }

    public List<HashMap<Integer, Long>> sortedNum(HashMap<Integer, HashMap<Long, Integer>> data) {
        List<HashMap<Integer, Long>> box = new ArrayList<>();
        List<List<Integer>> sorted = new ArrayList<>();
        for (Map.Entry<Integer, HashMap<Long, Integer>> entry : data.entrySet()) {
            Integer episode = entry.getKey();
            HashMap<Long, Integer> value = entry.getValue();
            for (Map.Entry<Long, Integer> bullet : value.entrySet()) {
                Integer time = Math.toIntExact(bullet.getKey());
                Integer num = bullet.getValue();
                List<Integer> integers = Arrays.asList(episode, time, num);
                sorted.add(integers);
            }
        }
        sorted.sort((o1, o2) -> o2.get(2).compareTo(o1.get(2)));
        for (List<Integer> integers: sorted) {
            Integer ep = integers.get(0);
            Long t = Long.valueOf(integers.get(1));
            HashMap<Integer,Long> hash = new HashMap<>();
            hash.put(ep,t);
            box.add(hash);
        }
        return box;
    }

    @Override
    public <T> List<List<Long>> getClip() {
        return null;
    }
}
