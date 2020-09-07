package com.alan.text;

import com.alan.util.FilesBox;
import com.alan.util.Output;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class BaiduSearcher extends HttpBox {
    String link = "http://www.baidu.com/s?wd=";
    String html;

    public void search(String word) {
        String link = "http://www.baidu.com/s?wd=" + word;
        html = get(link);
    }


    public void searchByJsoup(String word) throws Exception{
        Document document = Jsoup.connect(link+word).get();
        String title = document.title();
        Output.print(title);
        html = document.html();
    }

    public void parse() {
        Document document = Jsoup.parse(html);
        Element elementById = document.getElementById("1");
        Output.print(elementById.html());
        Elements a = document.select("h3 > a");
        Output.print(a.html());
    }

    public boolean toFile(String fileName) {
        return FilesBox.writer(html, fileName);
    }
}
