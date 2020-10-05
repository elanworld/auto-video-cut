package com.alan.text;

import java.util.List;

public interface BulletBoxInterface {
    <T> List<List<T>> getBullet(String word);

    <T> List<List<Long>> getClip();
}
