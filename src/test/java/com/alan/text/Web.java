package com.alan.text;

import org.junit.Test;

public class Web {

    public void baidu() throws Exception{
        BaiduSearcher baiduSearcher = new BaiduSearcher();
        baiduSearcher.searchByJsoup("zack king 魔术");
        baiduSearcher.toFile("C:\\Users\\Alan\\Desktop\\baidu.html");
        // baiduSearcher.parse();
    }
}
