package com.alan.text;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class BulletTest {
    @Test
    public void compare() {
        System.out.println("cc".compareTo("bb"));
    }

    @Test
    public void getData() {
        BulletBox bulletBox = new BulletBox();
        List<List<Object>> result = bulletBox.getBullet("打开方式");
        System.out.println(result);
        HashMap<Integer, HashMap<Long, List<String>>> integerHashMapHashMap = bulletBox.getByEpisode(result);
        HashMap<Integer, HashMap<Long, Integer>> bulletNum = bulletBox.getBulletNum(integerHashMapHashMap);
        List<HashMap<Integer, Long>> hashMaps = bulletBox.sortedNum(bulletNum);
        System.out.println(hashMaps);
    }
}
