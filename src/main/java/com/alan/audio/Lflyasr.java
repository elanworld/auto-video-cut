package com.alan.audio;
/**
import com.alan.util.Output;
import com.iflytek.msp.lfasr.LfasrClient;
import com.iflytek.msp.lfasr.model.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class Lflyasr {
    private static final String APP_ID = "5f1e9675";
    private static final String SECRET_KEY = "b0d4a010d2982a1072f30f79def58c84";
    LfasrClient lfasrClient = LfasrClient.getInstance(APP_ID, SECRET_KEY);
    String task;

    public void upData(String file) {
        task = lfasrClient.upload(file).getData();
    }

    public boolean getFinish() {
        Message message = lfasrClient.getProgress(task);
        JSONObject object = new JSONObject(message.getData());
        Output.print(object);
        if (object.getInt("status") == 9) {
            return true;
        } else {
            return false;
        }
    }

    public LinkedHashMap<double[], String> getResult() {
        LinkedHashMap<double[], String> map = new LinkedHashMap<double[], String>();
        Message result = lfasrClient.getResult(task);
        String resultData = result.getData();
        Output.print(resultData);
        JSONArray objects = new JSONArray(resultData);
        for (Object object : objects) {
            JSONObject json = (JSONObject) object;
            double bg = json.getDouble("bg");
            double ed = json.getDouble("ed");
            double[] time = {bg, ed};
            String onebest = json.getString("onebest");
            map.put(time, onebest);
        }
        Output.print(map);
        return map;
    }

    public LinkedHashMap<double[], String> loopGetResult() {
        boolean ok;
        while (ok = getFinish() != true) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
            }
        }
        LinkedHashMap<double[], String> map = getResult();
        return map;
    }

    //need exit by manual
    public void exit() {
        System.exit(0);
    }

}
**/