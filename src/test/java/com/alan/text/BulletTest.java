package com.alan.text;

import org.junit.Test;

import java.util.Arrays;

public class BulletTest {
    @Test
    public void compare() {
        System.out.println("cc".compareTo("bb"));
    }

    @Test
    public void getData() {
        BulletBox bulletBox = new BulletBox();
        bulletBox.setKeyWord("哈哈哈");
        bulletBox.setEpisodes(Arrays.asList(1,3,5,9));
        System.out.println(bulletBox.getClip());
    }
}
