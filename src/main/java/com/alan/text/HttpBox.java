package com.alan.text;

import com.alan.util.FilesBox;
import org.apache.http.util.TextUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpBox implements HttpBoxInterface {

    @Override
    public String get(String link) {
        String result = null;
        InputStream is;
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            for (Map.Entry<String,String> entry : getHead().entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
            if (conn.getResponseCode() == 302) {
                String url302 = conn.getHeaderField("Location");
                if (TextUtils.isEmpty(url302)) {
                    url302 = conn.getHeaderField("location");
                }
                if (!(url302.startsWith("http://") || url302.startsWith("https://"))) {
                    URL originalUrl = new URL(link);
                    url302 = originalUrl.getProtocol() + "://" + originalUrl.getHost() + ":" + originalUrl.getPort() + url302;
                }
                return get(url302);
            }
            if (conn.getResponseCode() >= 400 ) {
                is = conn.getErrorStream();
            }
            else{
                is = conn.getInputStream();
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private HashMap<String, String> getHead() {
        HashMap<String, String> header = new HashMap<>();
        // header.put("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.0.249.0 Safari/532.5");
        header.put("User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
        return header;
    }

    @Override
    public String post(String url) {
        return null;
    }

}
