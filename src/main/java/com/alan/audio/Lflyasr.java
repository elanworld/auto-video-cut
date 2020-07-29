package com.alan.audio;

import com.alan.output.Output;
import com.iflytek.msp.lfasr.LfasrClient;
import com.iflytek.msp.lfasr.model.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

//need exit by manual
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
        new Output(object);
        if (object.getInt("status") == 9) {
            return true;
        } else {
            return false;
        }
    }

    public HashMap<double[], String> getResult() {
        HashMap<double[], String> map = new HashMap<double[], String>();
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        Message result = lfasrClient.getResult(task);
        String resultData = result.getData();
        Output.print(resultData);
        JSONArray objects = new JSONArray(resultData);
        for (Object object : objects) {
            JSONObject json = (JSONObject) object;
            double bg = json.getDouble("bg");
            double ed = json.getDouble("ed");
            double [] time = {bg,ed};
            String onebest = json.getString("onebest");
            map.put(time,onebest);
            Object[] data = {(Object)bg,(Object)ed, (Object)onebest};
            new Output(data[0]);
            list.add(data);
        }
        new Output(map);
        return map;
    }

    public HashMap<double[], String> loopGetResult() {
        boolean ok;
        while (ok = getFinish() != true) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (Exception e) {
            }
        }
        HashMap<double[], String> map = getResult();
        return map;
    }
    public void exit() {
        System.exit(0);
    }

}
