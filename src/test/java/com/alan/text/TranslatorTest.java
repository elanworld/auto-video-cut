package com.alan.text;

import org.junit.Test;

public class TranslatorTest {
    @Test
    public void run() {
        String good = new Translator().run("good");
        System.out.println(good);
    }
}
