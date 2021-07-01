package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;
import com.yellowriver.skiff.Bean.SimpleBean;
import com.yellowriver.skiff.Bean.dongmanzhijia;
import com.yellowriver.skiff.Help.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mozilla.javascript.NativeArray;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.seimicrawler.xpath.exception.XpathSyntaxErrorException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 网页 使用 xpath解析
 */
@SuppressWarnings("ALL")
class XpathUtils {
    private static final String GET = "GET";
    private static final String QZLINK = "{QZLink}";
    private static final String AJAX = "2";
    private static final String TAG = "XpathUtils";

    private static final XpathUtils instance = new XpathUtils();

    private XpathUtils() {
    }

    public static XpathUtils getInstance() {
        return instance;
    }

    // 根据加载规则获取网页html-doc
    @SuppressWarnings("unchecked")
    public Document GetHtmlDoc(NowRuleBean sourceRule, int page) {

        Document doc = null;
        if (sourceRule != null) {
            if ("GET".equals(sourceRule.getRequestMethod())) {

                doc = getDocument(sourceRule, page);
            } else {
                //post
                doc = postDocument(sourceRule);
            }
        } else {
            //Log.d(TAG, "GetHtmlDoc: 规则为空");
        }
        return doc;
    }


    //第三步 根据xpath规则获取所需数据
    public Vector<DataEntity> GetData(Document doc, NowRuleBean sourceRule) {



        Vector<DataEntity> dataEntities = new Vector<>();
        if (doc != null) {

            if (sourceRule.getListXpath().indexOf("var")!=-1)
            {
                String url = sourceRule.getUrl();
                String html  = NetUtils.getInstance().getRequest(url, null, sourceRule.getCharset());

                dataEntities = exJs(url,html,sourceRule.getListXpath(),"", sourceRule);
            }else {
                //将DOM转为jx
                JXDocument jxDocument = JXDocument.create(doc.toString());
                //通过列表xpath获取的列表的dom
                if (sourceRule.getListXpath().startsWith("//*")) {
                    List<Object> lists = null;
                    try {
                        lists = jxDocument.sel(sourceRule.getListXpath());
                        LogUtil.info("轻舟调试","列表规则："+sourceRule.getListXpath());
                        LogUtil.info("轻舟调试","链接规则"+sourceRule.getLinkXpath());
                        LogUtil.info("轻舟调试","标题规则"+sourceRule.getTitleXpath());
                        LogUtil.info("轻舟调试","图片规则"+sourceRule.getCoverXpath());
                        LogUtil.info("轻舟调试","简介规则"+sourceRule.getSummaryXpath());
                        LogUtil.info("轻舟调试","日期规则"+sourceRule.getDateXpath());
                    } catch (InputMismatchException e) {

                    }


                    if (lists != null) {
                        //Log.d(TAG, "GetData: "+lists.toString());
                        //先定义标题 图片 简介 链接 日期
                        String title = null, cover = null, summary = null, link = null, date = null;

                        LogUtil.info("轻舟调试","列表大小："+lists.size());
                        //LogUtil.info("轻舟调试","列表html："+lists.toString());
                        for (Object list : lists) {
                            //再分别解析标题和链接
                            jxDocument = JXDocument.create(list.toString());

                            title = getTitle(jxDocument, sourceRule);
                            link = getLink(jxDocument, sourceRule);
                            cover = getCover(jxDocument, sourceRule);
                            summary = getSummary(jxDocument, sourceRule);
                            date = getDate(jxDocument, sourceRule);

                            //标题和链接是必须选项
                            if (sourceRule.getLinkType().equals("8")) {
                                //搜索 不需要获取链接 用标题去搜索
                                if (title != null) {
                                    if (!title.equals("")) {
                                        //将获取的标题 简介 图片 链接保存为实体
                                        DataEntity dataEntity = new DataEntity(title, summary, cover, link, date, sourceRule.getViewMode(), sourceRule.getLinkType());
                                        dataEntities.add(dataEntity);

                                    } else {
                                        //Log.d(TAG, "Xpath: 标题为空");
                                    }
                                } else {
                                    //Log.d(TAG, "Xpath: 标题为空");
                                }
                            } else {
                                //不是搜索 必须有标题和链接
                                if (title != null || link != null) {
                                    if (!"".equals(title)) {
                                        //将获取的标题 简介 图片 链接保存为实体
                                        DataEntity dataEntity = new DataEntity(title, summary, cover, link, date, sourceRule.getViewMode(), sourceRule.getLinkType());
                                        dataEntities.add(dataEntity);

                                    } else {
                                        //Log.d(TAG, "Xpath: 标题或链接为空");
                                    }
                                } else {
                                    //Log.d(TAG, "Xpath: 标题或链接为空");
                                }
                            }

                        }

                    } else {
                        Log.d(TAG, "Xpath: 获取的数据为空");
                    }
                } else {
                    Log.d(TAG, "Xpath: 源配置不正确");
                }
            }
        }
        if(dataEntities!=null) {
            dataEntities = getNoSameObjectVector(dataEntities);
        }
        return dataEntities;
    }

    /**
     * 获取标题
     *
     * @param jxDocument
     * @param sourceRule
     * @return
     */
    private String getTitle(JXDocument jxDocument, NowRuleBean sourceRule) {
        String title = null;
        //标题xpaht不为空
        if (!"".equals(sourceRule.getTitleXpath())) {
            //获取列表中所有标题的值

            try {
                List<JXNode> titles = jxDocument.selN(sourceRule.getTitleXpath());

                if (titles.size() != 0) {

                    if(titles.get(0).toString()!=null) {
                        LogUtil.info("轻舟调试", "处理前的标题" +titles.get(0).toString());
                        //获取标题
                        title = titles.get(0).toString();
                        if (!"".equals(sourceRule.getTitleProcessingValue())) {
                            //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                            title = AnalysisUtils.getInstance().processingValue(title, sourceRule.getTitleProcessingValue(), sourceRule.getUrl(), 0);
                        }


                        LogUtil.info("轻舟调试", "处理后的标题" + title);

                    }

                } else {

                    LogUtil.info("轻舟调试警告", "没有解析到标题时显示html：" + titles.toString());
                }
            }catch (XpathSyntaxErrorException e){

            }
        }
        return title;
    }

    public static String getXpath(String url,String xpath) {
        String html = "";
        String result = "";
        html = NetUtils.getInstance().getRequest(url,"3");
        if(html!="") {

            JXDocument jxDocument = JXDocument.create(html);
            List<Object> results = null;
            try {
                results = jxDocument.sel(xpath);
            } catch (XpathSyntaxErrorException e) {

            }
            if (results != null) {
                if(results.size()==0){

                }else {
                    result = results.get(0).toString();
                }
            }
        }
        return result;
    }


    /**
     * 获取链接
     *
     * @param jxDocument
     * @param sourceRule
     * @return
     */
    private String getLink(JXDocument jxDocument, NowRuleBean sourceRule) {
        String link = null;
        //如果连接xpath存在
        if (!"".equals(sourceRule.getLinkXpath())) {
            //获取列表中所有连接的值

            List<Object> links = jxDocument.sel(sourceRule.getLinkXpath());
            if (links.size() != 0) {
                LogUtil.info("轻舟调试","处理前的链接"+links.get(0).toString());

                link = links.get(0).toString();
                if (!"".equals(sourceRule.getLinkProcessingValue())) {

                    link = AnalysisUtils.getInstance().processingValue(link, sourceRule.getLinkProcessingValue(), sourceRule.getUrl(), 0);
                }
                LogUtil.info("轻舟调试","处理后的连接"+link);

            }else {

                LogUtil.info("轻舟调试警告","没有解析到链接时显示html："+links.toString());
            }
        }
        if(link!=null){
            if(!link.startsWith("http")){
                link =  getHost(sourceRule.getUrl()) +link;
            }
        }
        return link;
    }

    /**
     * 获取图片
     *
     * @param jxDocument
     * @param sourceRule
     * @return
     */
    private String getCover(JXDocument jxDocument, NowRuleBean sourceRule) {
        String cover = null;
        //如果图片xpath存在
        if (!"".equals(sourceRule.getCoverXpath())) {
            //获取列表中所有图片的值

            List<Object> covers = jxDocument.sel(sourceRule.getCoverXpath());
            if (covers.size() != 0) {
                LogUtil.info("轻舟调试","处理前的图片"+covers.get(0).toString());

                cover = covers.get(0).toString();
                //图片处理完整
                if (!"".equals(sourceRule.getCoverProcessingValue())) {
                    cover = AnalysisUtils.getInstance().processingValue(cover, sourceRule.getCoverProcessingValue(), sourceRule.getUrl(), 0);
                }

                LogUtil.info("轻舟调试","处理后的图片"+cover);
            }else {

                LogUtil.info("轻舟调试警告","没有解析到图片时显示html："+covers.toString());
            }

        }
        return cover;
    }


    /**
     * 获取简介
     *
     * @param jxDocument
     * @param sourceRule
     * @return
     */
    private String getSummary(JXDocument jxDocument, NowRuleBean sourceRule) {
        String summary = null;
        //如果简介xpath存在
        if (!"".equals(sourceRule.getSummaryXpath())) {
            //获取列表中所有简介的值
            List<JXNode> summarys = jxDocument.selN(sourceRule.getSummaryXpath());
            if (summarys.size() != 0) {

                summary = summarys.get(0).toString();
                LogUtil.info("轻舟调试","简介："+summary);
            }
        }
        return summary;
    }

    /**
     * 获取日期
     *
     * @param jxDocument
     * @param sourceRule
     * @return
     */
    private String getDate(JXDocument jxDocument, NowRuleBean sourceRule) {
        String date = null;
        //如果日期xpath存在
        if (!"".equals(sourceRule.getDateXpath())) {
            //获取列表中所有日期的值
            List<JXNode> dates = jxDocument.selN(sourceRule.getDateXpath());
            if (dates.size() != 0) {
                //获取日期

                date = dates.get(0).toString();
                LogUtil.info("轻舟调试","日期："+date);
            }

        }
        return date;
    }

    /**
     * get 获取doc
     *
     * @param sourceRule
     * @param page
     * @return
     */
    private Document getDocument(NowRuleBean sourceRule, int page) {
        Document doc = null;
        //获取当前要解析的url
        String actionUrl = getUrl(sourceRule, page);
        //请求头  源使用json写hearder 将json格式的hearder转为hashmap
        HashMap<String, String> headerMap = getHeaders(sourceRule.getHeaders());
        //如果是js异步加载数据 用htmlutil  1.不是 2是
        String html;
        if (AJAX.equals(sourceRule.getIsAjax())) {
            //使用htmlunit获取html 再解析
             html = HtmlunitUtils.HtmlUtilGET(actionUrl,headerMap);
        } else {
            //获取dom
             html = NetUtils.getInstance().getRequest(actionUrl, headerMap, sourceRule.getCharset());

        }
        if (null != html) {

            doc = Jsoup.parse(html);
        }else{
            LogUtil.info("轻舟调试","html：为空");
        }
        return doc;
    }

    private Document postDocument(NowRuleBean sourceRule) {
        Document doc = null;
        String actionUrl = null;
        if (!"".equals(sourceRule.getQuery())) {
            //表示要搜索 替换url上的搜索关键字
            actionUrl = sourceRule.getUrl().replace("{QZQuery}", sourceRule.getQuery());
        }
        //请求头  源使用json写hearder 将json格式的hearder转为hashmap
        HashMap<String, String> headerMap = null;
        //postdata 源使用json写postdata 将json格式的postdata转为hashmap
        HashMap<String, String> paramsMap = null;
        String PostData;
        if (!"".equals(sourceRule.getPostData())) {
            //表示要搜索 替换postdata里的搜索关键字
            String Query = sourceRule.getQuery();
            if(sourceRule.getCharset().equals("2")){
                byte[] b = Query.getBytes();
                try {
                    Query = new String(b, "GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            PostData = sourceRule.getPostData().replace("{QZQuery}", Query);

            //Log.d(TAG, "GetHtmlDoc postdata: " + PostData);
            paramsMap = JSON.parseObject(PostData, HashMap.class);
        }
        if (!"".equals(sourceRule.getHeaders())) {
            headerMap = JSON.parseObject(sourceRule.getHeaders(), HashMap.class);
        }
        //如果是js异步加载数据 用htmlutil  1.不是 2是
        String html;
        if (AJAX.equals(sourceRule.getIsAjax())) {
            //使用htmlunit获取html 再解析
             html =  HtmlunitUtils.HtmlUtil(actionUrl,paramsMap);

        } else {
            //获取dom
             html = NetUtils.getInstance().requestPost(actionUrl, paramsMap, headerMap, sourceRule.getCharset());

        }
        if (null != html) {
            doc = Jsoup.parse(html);
        }
        return doc;
    }

    /**
     * get 生成当前的url
     *
     * @param sourceRule
     * @param page
     * @return
     */
    public String getUrl(NowRuleBean sourceRule, int page) {
        String actionUrl = null;
        //GET
        if (!"".equals(sourceRule.getQuery())) {
            //表示要搜索 替换url上的搜索关键字
            actionUrl = sourceRule.getUrl().replace("{QZQuery}", sourceRule.getQuery());


        } else {
            actionUrl = sourceRule.getUrl();
        }
        if (page == 1) {

        } else {
            String nexturlxpath = sourceRule.getNextPageXpath();
            Log.d(TAG, "GetHtmlDoc: ddd" + nexturlxpath);
            if (nexturlxpath.contains("{QZLink}")) {
                actionUrl = nexturlxpath.replace("{QZLink}", actionUrl);
                Log.d(TAG, "GetHtmlDoc: " + actionUrl);
                if (!"".equals(sourceRule.getNextProcessingValue())) {
                    //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                    actionUrl = AnalysisUtils.getInstance().processingValue(actionUrl, sourceRule.getNextProcessingValue(), sourceRule.getUrl(), page);
                }

                if(actionUrl!=null) {
                    Log.d(TAG, "GetHtmlDoc: " + actionUrl);
                    //包含
                    actionUrl = actionUrl.replace("{QZPage}", String.valueOf(page));
                    actionUrl = actionUrl.replace("{QZLink}", sourceRule.getUrl());
                    actionUrl = actionUrl.replace("{QZQuery}", sourceRule.getQuery());


                }else{
                    Log.d(TAG, "GetHtmlDoc:最后url为空 "  );
                }

            } else {
                //加载多页时有bug
                actionUrl = AnalysisUtils.getLink(actionUrl, nexturlxpath);
                if (!"".equals(sourceRule.getNextProcessingValue())) {
                    //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                    actionUrl = AnalysisUtils.getInstance().processingValue(actionUrl, sourceRule.getNextProcessingValue(), sourceRule.getUrl(), page);
                }



            }

        }

        LogUtil.info("轻舟调试","目标地址："+actionUrl);

        return actionUrl;
    }


    /**
     * get 获取header 转成hashmap
     *
     * @param headers
     * @return
     */
    public HashMap<String, String> getHeaders(String headers) {
        HashMap<String, String> headerMap = null;
        if (!"".equals(headers)) {
            try {
                headerMap = JSON.parseObject(headers, HashMap.class);
            }catch (JSONException e)
            {

            }

        }
        return headerMap;
    }

    private static Vector<DataEntity> exJs(String url, String html, String contentXpath, String readImgSrc,NowRuleBean sourceRule)
    {
        Vector<DataEntity> list = new Vector<>();
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
            engine.eval(btoa);
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
               LogUtil.info("轻舟",res.toString());
                Gson gson = new Gson();
                LogUtil.info("轻舟",gson.toJson(res));
                String json = gson.toJson(res);
                Type type2 = new TypeToken<List<dongmanzhijia>>() {
                }.getType();
                List<dongmanzhijia> dongmanzhijias = gson.fromJson(json, type2);
                if (dongmanzhijias!=null)
                {
                    for (dongmanzhijia dongmanzhijia : dongmanzhijias)
                    {
                        String title =  dongmanzhijia.getChapter_name();
                        String link = "https://m.dmzj.com/view/"+dongmanzhijia.getComic_id()+"/"+dongmanzhijia.getId()+".html";
                        DataEntity dataEntity = new DataEntity(title, "", "", link, null, sourceRule.getViewMode(), sourceRule.getLinkType());
                        list.add(dataEntity);
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

    public static String getHost(String url){
        if(url==null||url.trim().equals("")){
            return "";
        }
        String host = "";
        Pattern p =  Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(url);
        if(matcher.find()){
            host = matcher.group();
        }
        if(url.startsWith("http://")){
            host = "http://" + host;
        }else if(url.startsWith("https://")){
            host = "https://" + host;
        }
        return host;
    }


    private Vector getNoSameObjectVector(Vector vector){

     Vector tempVector = new Vector();

    HashSet set = new HashSet(vector);

     //addAll(Collection c);  //可以接受Set和List类型的参数

    tempVector.addAll(set);

     return tempVector;

    }
}
