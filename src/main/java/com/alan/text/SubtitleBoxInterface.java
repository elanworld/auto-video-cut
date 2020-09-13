package com.alan.text;

import java.util.List;

public interface SubtitleBoxInterface {
    List<String> getClip(double start, double end);
    List<String> getEnglish();
    List<String> getChinese();
}
