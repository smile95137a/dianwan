package com.one.onekuji.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlProcessor {

    // 定义域名，供拼接完整URL时使用
    private static final String BASE_URL = "https://api.onemorelottery.tw:8080/img/";

    public static String processHtml(String html) {
        // 使用Jsoup解析HTML
        Document doc = Jsoup.parse(html);

        // 查找所有 <img> 标签
        Elements images = doc.select("img");

        // 遍历每一个img标签，补全src属性
        for (Element img : images) {
            String src = img.attr("src");

            // 如果src是相对路径，进行补全
            if (src.startsWith("/")) {
                img.attr("src", BASE_URL + src.substring(1));
            }
        }

        // 返回修改后的HTML
        return doc.body().html();
    }
}
