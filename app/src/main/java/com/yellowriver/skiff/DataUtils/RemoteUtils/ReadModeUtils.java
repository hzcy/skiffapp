package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;

import com.yellowriver.skiff.Bean.MultipleItem;
import com.yellowriver.skiff.Bean.SimpleBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 阅读模式
 */
public class ReadModeUtils {

    private static final String TAG = "ReadModeUtils";


    public static String getContext(String url, String contentXpath, String readCharset,String readImgSrc)
    {

        Element content = null;
        if ("".equals(contentXpath)) {
            //content = Jsoup.parse(textcontent).children().get(0);
            //Log.d(TAG, "getContent: "+content);
        } else {
            // 获取文档内容
            Document doc = Jsoup.parse(NetUtils.getInstance().getRequest(url, readCharset));

            if (doc != null) {

                Log.d(TAG, "getContent: " + doc.toString());
                // 获取文章内容
                content = doc.select(contentXpath).get(0);


            }
        }
        String contentstr = content.toString();
        for (int j = 0; j < content.children().size(); j++) {
            Element c = content.child(j); // 获取每个元素

            Log.d(TAG, "getContent: "+c.select("img").size());
            // 抽取出图片
            if (c.select("img").size() > 0) {
                Elements imgs = c.getElementsByTag("img");
                for (Element img : imgs) {
                    if (!"".equals(img.attr("src"))) {

                        Log.d(TAG, "getRickContext: "+img.parent().attr("href"));
                        Log.d(TAG, "getRickContext: "+img.attr("src"));
                        // 大图链接
                        if (!"".equals(img.parent().attr("href"))) {
                            if (!img.parent().attr("href").startsWith("http"))
                            {
                                Log.d(TAG, "getRickContext: "+readImgSrc);
                                Log.d(TAG, "getRickContext: "+img.parent().attr("href"));
                                contentstr = content.toString().replace(img.parent().attr("href").toString(),readImgSrc + img.parent().attr("href").toString());
                            }

                            System.out.println("href="
                                    + img.parent().attr("href"));

                        }
                        if (!img.attr("src").startsWith("http"))
                        {
                            Log.d(TAG, "getRickContext: "+readImgSrc);
                            contentstr = content.toString().replace(img.attr("src").toString(),readImgSrc + img.attr("src").toString());
                        }
                    }
                }
            }
        }
        return contentstr;
    }


    public static Vector<MultipleItem> getRickContext(String url, String contentXpath, String readImgSrc, String readCharset)
    {
        Vector<MultipleItem> multipleItems = new Vector<>();
        Element content = null;
        if ("".equals(contentXpath)) {
            //content = Jsoup.parse(textcontent).children().get(0);
            //Log.d(TAG, "getContent: "+content);
        } else {
            // 获取文档内容
            Document doc = Jsoup.parse(NetUtils.getInstance().getRequest(url, readCharset));

            if (doc != null) {

                Log.d(TAG, "getContent: " + doc.toString());
                // 获取文章内容
                content = doc.select(contentXpath).get(0);

            }
        }
        Log.d(TAG, "getRickContext: "+content.html());
        String contentstr = content.toString();
        for (int j = 0; j < content.children().size(); j++) {
            Element c = content.child(j); // 获取每个元素

            Log.d(TAG, "getContent: "+c.select("img").size());
            // 抽取出图片
            if (c.select("img").size() > 0) {
                Elements imgs = c.getElementsByTag("img");
                for (Element img : imgs) {
                    if (!"".equals(img.attr("src"))) {

                        Log.d(TAG, "getRickContext: "+img.parent().attr("href"));
                        Log.d(TAG, "getRickContext: "+img.attr("src"));
                        // 大图链接
                        if (!"".equals(img.parent().attr("href"))) {
                            if (!img.parent().attr("href").startsWith("http"))
                            {
                                Log.d(TAG, "getRickContext: "+readImgSrc);
                                Log.d(TAG, "getRickContext: "+img.parent().attr("href"));
                                contentstr = content.toString().replace(img.parent().attr("href").toString(),readImgSrc + img.parent().attr("href").toString());
                            }

                            System.out.println("href="
                                    + img.parent().attr("href"));

                        }
                        if (!img.attr("src").startsWith("http"))
                        {
                            Log.d(TAG, "getRickContext: "+readImgSrc);
                            contentstr = content.toString().replace(img.attr("src").toString(),readImgSrc + img.attr("src").toString());
                        }
                    }
                }
            }
        }
        MultipleItem multipleItem = new MultipleItem();
        multipleItem.setContent(contentstr);
        multipleItems.add(multipleItem);
        return multipleItems;
    }





    public static List<SimpleBean> getContent(String url, String contentXpath, String readImgSrc, String readCharset, String textcontent) {
        List<SimpleBean> list = new ArrayList<>();

        Element content = null;
        if ("".equals(contentXpath)) {
            content = Jsoup.parse(textcontent).children().get(0);
            Log.d(TAG, "getContent: "+content);
        } else {
            // 获取文档内容
            String html = NetUtils.getInstance().getRequest(url, readCharset);
            if (html != null) {
                Document doc = Jsoup.parse(html);

                if (doc != null) {

                    Log.d(TAG, "getContent: " + doc.toString());
                    // 获取文章内容
                    content = doc.select(contentXpath).get(0);

                }
            }

        }

        if (content!=null) {
            SimpleBean simpleBean = new SimpleBean();
            simpleBean.setContent(ToDBC(content.html()));
            list.add(simpleBean);

        }
        Log.d(TAG, "getContent: "+list.size());

        return list;
    }

    /**
     * 半角转换为全角 全角---指一个字符占用两个标准字符位置。 半角---指一字符占用一个标准的字符位置。
     *
     * @param input
     * @return
     */
    private static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }
}
