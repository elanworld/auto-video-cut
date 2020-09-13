package com.alan.text;

import com.alan.util.Output;
import org.junit.Test;

import java.util.List;

public class Subtitle {
    String subtitle = "F:\\Alan\\Videos\\Mine\\my.srt";

    @Test
    public void subtitleTest() {
        SubtitleBox subtitleBox = new SubtitleBox();
        subtitleBox.init(subtitle);
        List<String> chinese = subtitleBox.getByFilter(false,true,true,5,20);
        Output.print(chinese);
    }
}
