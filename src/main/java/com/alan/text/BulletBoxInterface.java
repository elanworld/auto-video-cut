package com.alan.text;

import java.util.List;

public interface BulletBoxInterface {
    <T> List<List<T>> getBullet(String word);

    /**
     * get most important clip like [[episode,start,end],[episode,start,end]...]
     * @param <T>
     * @return
     */
    <T> List<List<Long>> getClip();
}
