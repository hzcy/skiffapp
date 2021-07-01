package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.content.Context;
import android.util.Log;

import com.yellowriver.skiff.Bean.SimpleBean;
import com.yellowriver.skiff.Help.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;
import org.mozilla.javascript.NativeArray;

import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.exception.XpathSyntaxErrorException;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.HTMLHighlighter;

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
                if (doc.select(contentXpath)!=null) {
                    content = doc.select(contentXpath).get(0);
                }


            }
        }
        String contentstr ="";
        if (content != null) {
             contentstr = content.toString();
            for (int j = 0; j < content.children().size(); j++) {
                Element c = content.child(j); // 获取每个元素

                //Log.d(TAG, "getContent: " + c.select("img").size());
                // 抽取出图片
                if (c.select("img").size() > 0) {
                    Elements imgs = c.getElementsByTag("img");
                    for (Element img : imgs) {
                        if (!"".equals(img.attr("src"))) {

                            Log.d(TAG, "getRickContext: " + img.parent().attr("href"));
                            Log.d(TAG, "getRickContext: " + img.attr("src"));
                            // 大图链接
                            if (!"".equals(img.parent().attr("href"))) {
                                if (!img.parent().attr("href").startsWith("http")) {
                                    Log.d(TAG, "getRickContext: " + readImgSrc);
                                    Log.d(TAG, "getRickContext: " + img.parent().attr("href"));
                                    contentstr = content.toString().replace(img.parent().attr("href").toString(), readImgSrc + img.parent().attr("href").toString());
                                }

                                System.out.println("href="
                                        + img.parent().attr("href"));

                            }
                            if (!img.attr("src").startsWith("http")) {
                               // Log.d(TAG, "getRickContext: " + readImgSrc);
                                contentstr = content.toString().replace(img.attr("src").toString(), readImgSrc + img.attr("src").toString());
                            }
                        }
                    }
                }
            }
        }
        return contentstr;
    }





    private static final String AJAX = "2";


    public static List<SimpleBean> getContent(String url, String contentXpath, String readImgSrc, String readCharset, String textcontent) {
        List<SimpleBean> list = new ArrayList<>();
        String newStr = "";
        String newresult = "";
        Element content = null;
       // s();
        String html = NetUtils.getInstance().getRequest(url, readCharset);
        // 获取文档内容

        LogUtil.info("轻舟自动 url","ddd"+url);


        if ("".equals(contentXpath)) {
         //   LogUtil.info("轻舟自动 为空","ddd");

            if (html != null) {
                html = BoilerpipeUtils.highlightingHtml(html, "5");

                html = BoilerpipeUtils.getFullContent(html);

                content = Jsoup.parse(html);
            }

            if(textcontent!=null) {
                if (!textcontent.isEmpty()) {
                    try {
                        content = Jsoup.parse(textcontent).children().get(0);
                    } catch (IndexOutOfBoundsException e) {

                    }

                }
            }


            //Log.d(TAG, "getContent: "+content);
        } else {


            LogUtil.info("轻舟自动 不为空","ddd");

            if(contentXpath.startsWith("{QZ}")){
                if (html != null) {
                    html = BoilerpipeUtils.highlightingHtml(html, "5");

                    html = BoilerpipeUtils.getFullContent(html);

                    content = Jsoup.parse(html);

                    newStr = contentXpath.replace("{QZ}", "");
                }
            }else {




                if (html != null) {
                    Document doc = Jsoup.parse(html);

                    if (doc != null) {

                        //Log.d(TAG, "getContent: " + doc.toString());
                        // 获取文章内容

                        try {
                            if (contentXpath.indexOf("{QZ}") != -1) {
                                //包含正则替换
                                String[] sourceStrArray2 = contentXpath.split("\\{QZ\\}");
                                if (sourceStrArray2.length == 2) {
                                    contentXpath = sourceStrArray2[0];
                                    newStr = sourceStrArray2[1];
                                    LogUtil.info("轻舟小说调试", "正则2" + contentXpath);
                                    LogUtil.info("轻舟小说调试", "正则3" + newStr);
                                } else {

                                }
                            } else {

                            }

                            if (doc.select(contentXpath) != null) {
                                // Log.d(TAG, "getContent: "+doc.select(contentXpath).text());
                                try {
                                    content = doc.select(contentXpath).get(0);

                                } catch (IndexOutOfBoundsException e) {

                                } catch (Selector.SelectorParseException e) {

                                }


                            }
                        } catch (Selector.SelectorParseException e) {

                        }
                        if (content != null) {
                            //Log.d(TAG, "getContentfffffffff: "+content);
                            if ("".equals(content)) {
                                JXDocument jxDocument = JXDocument.create(doc.toString());
                                List<Object> results = jxDocument.sel(contentXpath);
                                if (results.size() != 0) {

                                    newresult = results.get(0).toString();
                                    if (!newStr.equals("")) {

                                        if (newresult != null) {

                                            newresult.replaceAll(newStr, "");
                                        }
                                    }

                                }
                            }
                        } else {
                            JXDocument jxDocument = JXDocument.create(doc.toString());
                            List<Object> results = jxDocument.sel(contentXpath);
                            if (results.size() != 0) {

                                newresult = results.get(0).toString();
                                if (!newStr.equals("")) {
                                    if (newresult != null) {

                                        newresult.replaceAll(newStr, "");
                                    }
                                }
                            }
                        }
                    }
                }

            }

        }
        if(readImgSrc!="")
        {
            newresult = AnalysisUtils.getInstance().processingValue(newresult, readImgSrc, url, 0);
        }
        //Log.d(TAG, "dfd"+content.outerHtml());

        if (content!=null) {
            if (!"".equals(content)) {
                SimpleBean simpleBean = new SimpleBean();
                newresult = ToDBC(content.html());
                if(!newStr.equals("")){
                   // LogUtil.info("轻舟小说调试","正则5"+newresult);
                    if(newresult != null) {
                        if(newStr.indexOf("value")!=-1){
                            Log.d(TAG, "getContent: "+newStr);
                            newresult = AnalysisUtils.getInstance().processingValue(newresult,newStr,"",0);

                            Log.d(TAG, "getContent: "+newresult);
                        }else {
                            newresult = newresult.replaceAll(newStr, "");
                        }

                    }
                }else{

                }
                if(newresult != null) {
                    newresult = newresult.replaceFirst("<br>", "<p>");
                    newresult = newresult.replaceFirst("&nbsp;", "<p>");
                    newresult = newresult.replace("&nbsp;", "");
                    newresult = newresult.replaceFirst("<br>", "");
                    newresult = newresult.replaceAll("<br>", "<p>");
                    newresult = newresult.replaceAll("一秒记住[\\s\\S]*免费阅读!", "");
                    newresult = getnewStr(newresult);
                }
                LogUtil.info("轻舟小说调试","正则5"+newresult);
                simpleBean.setContent(newresult);
                list.add(simpleBean);

            }else{
                SimpleBean simpleBean = new SimpleBean();
                simpleBean.setContent(newresult);
                list.add(simpleBean);
            }
        }else{
            SimpleBean simpleBean = new SimpleBean();
            simpleBean.setContent(newresult);
            list.add(simpleBean);
        }
        Log.d(TAG, "getContent: "+newresult);

        return list;
    }


    private static String getnewStr(String result)
    {
        result = result.replaceAll("一秒记住.*gt","");
        result = result.replaceAll("\\[搜索最新.*com\\]","");
        result = result.replaceAll("猎文.*Cc","");
        result = result.replaceAll("http.*html","");
        result = result.replaceAll("天才一秒.*com","");
        return result;
    }

    public static List<SimpleBean> getContent2(String url, String contentXpath, String readImgSrc, String readCharset, String textcontent,String readAjax,Context context) {
        List<SimpleBean> list = new ArrayList<>();

        String newresult = "";
        Element content = null;
        if ("".equals(contentXpath)) {
            try{
                content = Jsoup.parse(textcontent).children().get(0);
            }catch (IndexOutOfBoundsException e){

            }

            //Log.d(TAG, "getContent: "+content);
        } else {
            String html;
            // 获取文档内容
            if (AJAX.equals(readAjax)) {
                html = HtmlunitUtils.HtmlUtilGET(url,null);
            }else{
                html = NetUtils.getInstance().getRequest(url, readCharset);
            }

            Log.d(TAG, "getContent2: 加载url："+url);

            if (html != null) {

                if(contentXpath.indexOf("function")!=-1)
                {

                    list = exJs(url,html,contentXpath,readImgSrc);

                }else {
                    Document doc = Jsoup.parse(html);

                    if (doc != null) {

                        Log.d(TAG, "getContent2: " + doc.toString());
                        // 获取文章内容
                        try {
                            if (doc.select(contentXpath) != null) {

                                try {
                                    content = doc.select(contentXpath).get(0);
                                } catch (IndexOutOfBoundsException e) {

                                } catch (Selector.SelectorParseException e) {

                                }


                            }
                        } catch (Selector.SelectorParseException e) {

                        }

                        if (content != null) {

                            if ("".equals(content)) {
                                JXDocument jxDocument = JXDocument.create(doc.toString());
                                List<Object> results = jxDocument.sel(contentXpath);
                                if (results.size() != 0) {

                                    newresult = results.get(0).toString();

                                }
                            }
                        } else {
                            JXDocument jxDocument = JXDocument.create(doc.toString());
                            List<Object> results = null;
                            try {
                                 results = jxDocument.sel(contentXpath);
                            }catch (XpathSyntaxErrorException e)
                            {
                                
                            }
                            Log.d(TAG, "getContent2: " + contentXpath);
                            if(results!=null) {

                                Log.d(TAG, "getContent2: " + results.toString());
                                if (results.size() != 0) {


                                    if (results.size() > 1) {
                                        for (Object result : results) {
                                            newresult = result.toString();
                                            SimpleBean simpleBean = new SimpleBean();
                                            if (!"".equals(readImgSrc)) {
                                                Log.d(TAG, "getContent2: 处理");
                                                if (readImgSrc.indexOf("value") != -1) {
                                                    newresult = AnalysisUtils.getInstance().processingValue(newresult, readImgSrc, url, 0);

                                                } else {
                                                    if (readImgSrc.indexOf("{QZLink}") != -1) {

                                                        readImgSrc = readImgSrc.replace("{QZLink}", url);
                                                    }
                                                    Log.d(TAG, "getContent2: 处理" + readImgSrc);
                                                    simpleBean.setImageStr(readImgSrc);
                                                }
                                            }

                                            simpleBean.setContent(newresult);
                                            list.add(simpleBean);
                                        }
                                    } else {
                                        newresult = results.get(0).toString();
                                        if (!"".equals(readImgSrc)) {
                                            SimpleBean simpleBean = new SimpleBean();
                                            if (!"".equals(readImgSrc)) {
                                                Log.d(TAG, "getContent2: 处理");
                                                if (readImgSrc.indexOf("value") != -1) {
                                                    newresult = AnalysisUtils.getInstance().processingValue(newresult, readImgSrc, url, 0);

                                                } else {
                                                    if (readImgSrc.indexOf("{QZLink}") != -1) {

                                                        readImgSrc = readImgSrc.replace("{QZLink}", url);
                                                    }
                                                    Log.d(TAG, "getContent2: 处理" + readImgSrc);
                                                    simpleBean.setImageStr(readImgSrc);
                                                }
                                            }

                                            simpleBean.setContent(newresult);
                                            list.add(simpleBean);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }

        }
        if (list.size()<1)
        {
            if (content!=null) {
                if (!"".equals(content)) {
                    SimpleBean simpleBean = new SimpleBean();
                    simpleBean.setContent(ToDBC(content.html()));
                    list.add(simpleBean);

                }else{
                    SimpleBean simpleBean = new SimpleBean();
                    simpleBean.setContent(newresult);
                    list.add(simpleBean);
                }
            }else{
                SimpleBean simpleBean = new SimpleBean();
                simpleBean.setContent(newresult);
                list.add(simpleBean);
            }
        }else{

        }


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


    private static List<SimpleBean> exJs(String url,String html,String contentXpath,String readImgSrc)
    {
        if(readImgSrc!=null) {
            if (readImgSrc.indexOf("{QZLink}") != -1) {
                readImgSrc = url;
            }
        }

        List<SimpleBean> list = new ArrayList<>();
        //执行js代码
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

        LogUtil.info("轻舟", contentXpath);
        try {
            engine.put("base64hash","ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
            String btoa = "function atob (s) {\n" +
                    "            s = s.replace(/\\s|=/g, '');\n" +
                    "            var cur,\n" +
                    "                prev,\n" +
                    "                mod,\n" +
                    "                i = 0,\n" +
                    "                result = [];\n" +
                    "\n" +
                    "\n" +
                    "            while (i < s.length) {\n" +
                    "                cur = base64hash.indexOf(s.charAt(i));\n" +
                    "                mod = i % 4;\n" +
                    "\n" +
                    "\n" +
                    "                switch (mod) {\n" +
                    "                    case 0:\n" +
                    "                        //TODO\n" +
                    "                        break;\n" +
                    "                    case 1:\n" +
                    "                        result.push(String.fromCharCode(prev << 2 | cur >> 4));\n" +
                    "                        break;\n" +
                    "                    case 2:\n" +
                    "                        result.push(String.fromCharCode((prev & 0x0f) << 4 | cur >> 2));\n" +
                    "                        break;\n" +
                    "                    case 3:\n" +
                    "                        result.push(String.fromCharCode((prev & 3) << 6 | cur));\n" +
                    "                        break;\n" +
                    "                        \n" +
                    "                }\n" +
                    "\n" +
                    "\n" +
                    "                prev = cur;\n" +
                    "                i ++;\n" +
                    "            }\n" +
                    "\n" +
                    "\n" +
                    "            return result.join('');\n" +
                    "        }";


            //String jquery = NetUtils.getInstance().getRequest("https://code.jquery.com/jquery-3.1.1.min.js","");


            String ajax = "var xmlhttp;" +
                    "function ajax(url)\n" +
                    "{\n" +
                    "xmlhttp=null;\n" +
                    "\n" +
                    "xmlhttp=new XMLHttpRequest();\n" +
                    "  \n" +
                    "\n" +
                    "if (xmlhttp!=null)\n" +
                    "  {\n" +
                    "  xmlhttp.onreadystatechange=state_Change;\n" +
                    "  xmlhttp.open(\"GET\",url,true);\n" +
                    "  xmlhttp.send(null);\n" +
                    "  }\n" +
                    "else\n" +
                    "  {\n" +
                    "  \n" +
                    "  }\n" +
                    "}\n" +
                    "\n" +
                    "function state_Change()\n" +
                    "{\n" +
                    "if (xmlhttp.readyState==4)\n" +
                    "  {// 4 = \"loaded\"\n" +
                    "  if (xmlhttp.status==200)\n" +
                    "    {\n" +
                    "       return xmlhttp.responseText;\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
            engine.eval(btoa);
            engine.eval(ajax);
            engine.put("html", html);
            if (contentXpath.indexOf("baseUrl") != -1) {
                engine.put("baseUrl", readImgSrc);
            }
            NativeArray res = null;
            if (contentXpath.indexOf("getImgList")!=-1) {
                Log.d(TAG, "exJs: getImgList");
                engine.eval(contentXpath);
                Invocable invocable = (Invocable) engine;
                 res = (NativeArray) invocable.invokeFunction("getImgList");


            }else{
                Log.d(TAG, "exJs: ssd");
                engine.put("result", html);

                 res = (NativeArray) engine.eval(contentXpath);

            }

            if (res != null) {

                if (contentXpath.indexOf("atob") != -1) {
                    if(res.get(0).toString().indexOf("qingtiandy")!=-1) {
                        String[] imgstr = res.get(0).toString().split("\\$qingtiandy\\$");
                        if (imgstr != null) {
                            for (int i = 0; i < imgstr.length; ++i) {
                                Log.d("js获取的图片 atob", imgstr[i]);
                                String imgurl = imgstr[i];
                                SimpleBean simpleBean = new SimpleBean();
                                simpleBean.setImageStr(readImgSrc);
                                simpleBean.setContent(imgurl);
                                list.add(simpleBean);
                            }
                        }
                    }else {

                        for (int i = 0; i < res.size(); i++) {
                            Log.d("js获取的图片 js", res.get(i).toString());
                            String imgurl = res.get(i).toString();
                            SimpleBean simpleBean = new SimpleBean();
                            simpleBean.setImageStr(readImgSrc);
                            simpleBean.setContent(imgurl);
                            list.add(simpleBean);
                        }
                    }

                } else {
                    for (int i = 0; i < res.size(); i++) {
                        Log.d("js获取的图片 js", res.get(i).toString());
                        String imgurl = res.get(i).toString();
                        if (imgurl.indexOf("\\")!=-1)
                        {
                            imgurl = imgurl.replaceAll("\\\\","");
                        }
                        SimpleBean simpleBean = new SimpleBean();
                        simpleBean.setImageStr(readImgSrc);
                        simpleBean.setContent(imgurl);
                        list.add(simpleBean);
                    }
                }

            }

        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String jsCallJava(String url) {
        return "js call Java Rhino";
    }

    /** js调用Java中的方法 */
    private static final String JS_CALL_JAVA_FUNCTION = //
            "var ScriptAPI = java.lang.Class.forName(\"" + "com.yellowriver.skiff.DataUtils.RemoteUtils" + "\", true, javaLoader);" + //
                    "var methodRead = ScriptAPI.getMethod(\"jsCallJava\", [java.lang.String]);" + //
                    "function jsCallJava(url) {return methodRead.invoke(null, url);}" + //
                    "function Test(){ return jsCallJava(); }";

    private static void s()
    {
        URL url = null;
        try {
            url = new URL(
                            "https://www.x23qb.com/book/11786/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // choose from a set of useful BoilerpipeExtractors...
        //final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
         //final BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;
        final BoilerpipeExtractor extractor = CommonExtractors.KEEP_EVERYTHING_EXTRACTOR;
        // final BoilerpipeExtractor extractor = CommonExtractors.CANOLA_EXTRACTOR;
        // final BoilerpipeExtractor extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;

        // choose the operation mode (i.e., highlighting or extraction)
        final HTMLHighlighter hh = HTMLHighlighter.newHighlightingInstance();

        // final HTMLHighlighter hh = HTMLHighlighter.newExtractingInstance();


        //PrintWriter out = null;
        try {
            hh.setBodyOnly(true);
            hh.setIncludeImages(false);
            //hh.setOutputHighlightOnly(true);

           // out = new PrintWriter("/tmp/highlighted.html", "UTF-8");
            //out.println("<base href=\"" + url + "\" >");
            //out.println("<meta http-equiv=\"Content-Type\" content=\"text-html; charset=utf-8\" />");
           // out.println(hh.process(url, extractor));
            LogUtil.info("自动获取内容","内容"+hh.process(url, extractor));

           // out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        System.out.println("Now open file:///tmp/highlighted.html in your web browser");
    }
}

