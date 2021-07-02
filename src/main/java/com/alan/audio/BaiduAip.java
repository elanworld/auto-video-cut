package com.alan.audio;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;

import com.alan.common.util.Output;
import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;

public class BaiduAip {
	private final static String appid = "18230513";
	private final static String apiKey = "aG8uTFAvjEh22wVXMkz2MqBP";
	private final static String secretKey = "boEiiN2IEKqaLvEPwykBOvqo3e1ogcIC";
	AipSpeech aip;

	public BaiduAip() {
		aip = new AipSpeech(appid, apiKey, secretKey);
	}
	public void speechRec(String pcmFile) {
		JSONObject json = aip.asr(pcmFile, "pcm", 16000, null);
		Output.print(json);
	}

	public void text2audio(String text, String outFile) {
		aip.setConnectionTimeoutInMillis(2000);
		aip.setSocketTimeoutInMillis(60000);
		HashMap<String, Object> options = new HashMap<>();
		options.put("spd", "5");
		options.put("pit", "5");
		options.put("per", "5003");
		TtsResponse res = aip.synthesis(text, "zh", 1, options);
		byte[] data = res.getData();
		JSONObject res1 = res.getResult();
		if (data != null) {
			try {
				Util.writeBytesToFileSystem(data, outFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (res1 != null) {
			System.out.println(res1.toString(2));
		}
	}
}
