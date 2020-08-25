package com.alan.audio;

import com.alan.util.Output;
import com.baidu.aip.speech.AipSpeech;
import org.json.JSONObject;

public class BaiduAip {
    String appid = "18230513";
    String apiKey = "aG8uTFAvjEh22wVXMkz2MqBP";
    String secretKey = "boEiiN2IEKqaLvEPwykBOvqo3e1ogcIC";
    AipSpeech aip;
    public BaiduAip() {
        aip = new AipSpeech(appid, apiKey, secretKey);
    }
    public void speechRec(String pcmFile) {
        JSONObject json = aip.asr(pcmFile,"pcm",16000,null);
        Output.print(json);
    }
}