package com.site.blog.my.core.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @Description:
 * @Author: yuanyuqing
 * @CreateDate: 2019/11/4/0004 11:44
 * @Version: 1.0
 */
public class JsoupList {

    public void jsoup(String url){
        try {
            Document document = Jsoup.connect(url).get();
            // 使用 css选择器 提取列表新闻 a 标签
            Elements elements = document.select("div > ul.note-list > li > div.content > a");

            for (Element element:elements){
//                System.out.println(element);

                // 获取详情页链接
                String d_url = element.attr("href");
                // 获取标题
                String title = element.ownText();

                System.out.println("详情页链接："+d_url+" ,详情页标题："+title);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String url = "https://www.jianshu.com/"  ;
        JsoupList jsoupList = new JsoupList();
        jsoupList.jsoup(url);
    }
}
