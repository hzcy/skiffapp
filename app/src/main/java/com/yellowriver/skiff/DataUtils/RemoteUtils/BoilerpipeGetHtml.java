package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;
import com.yellowriver.skiff.Help.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.exception.XpathSyntaxErrorException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Vector;

public class BoilerpipeGetHtml {
    private static final String AJAX = "2";
    private static final String GET = "GET";
    private static final String QZLINK = "{QZLink}";


    private static final BoilerpipeGetHtml instance = new BoilerpipeGetHtml();

    private BoilerpipeGetHtml() {
    }

    public static BoilerpipeGetHtml getInstance() {
        return instance;
    }


    public String GetHtmlDoc(NowRuleBean sourceRule, int page) {

        String html = null;
        if (sourceRule != null) {
            if ("GET".equals(sourceRule.getRequestMethod())) {

                html = getHtml(sourceRule, page);
            } else {
                //post
                html = postHtml(sourceRule);
            }
        } else {
            //Log.d(TAG, "GetHtmlDoc: 规则为空");
        }
        if(html!=null){
            LogUtil.info("轻舟自动解析",  "html" + html + "");

        }else{
            LogUtil.info("轻舟自动解析",  "html为空"+sourceRule.getUrl());

        }
        return html;
    }


    private String getHtml(NowRuleBean sourceRule, int page) {


        String html = null;
        //获取当前要解析的url
        String actionUrl = XpathUtils.getInstance().getUrl(sourceRule, page);
        //请求头  源使用json写hearder 将json格式的hearder转为hashmap
        HashMap<String, String> headerMap = XpathUtils.getInstance().getHeaders(sourceRule.getHeaders());
        //如果是js异步加载数据 用htmlutil  1.不是 2是
        LogUtil.info("轻舟自动解析","get地址"+actionUrl);
        if (AJAX.equals(sourceRule.getIsAjax())) {
            //使用htmlunit获取html 再解析
            html = HtmlunitUtils.HtmlUtilGET(actionUrl, headerMap);
        } else {
            //获取dom
            html = NetUtils.getInstance().getRequest(actionUrl, headerMap, sourceRule.getCharset());

        }

        return html;
    }


    private String postHtml(NowRuleBean sourceRule) {
        String html = null;
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
            if (sourceRule.getCharset().equals("2")) {
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

        if (AJAX.equals(sourceRule.getIsAjax())) {
            //使用htmlunit获取html 再解析
            html = HtmlunitUtils.HtmlUtil(actionUrl, paramsMap);

        } else {
            //获取dom
            html = NetUtils.getInstance().requestPost(actionUrl, paramsMap, headerMap, sourceRule.getCharset());

        }

        return html;
    }

    //第三步 根据xpath规则获取所需数据
    public Vector<DataEntity> GetData(String html, NowRuleBean sourceRule) {
        Vector<DataEntity> dataEntities = new Vector<>();
        if (html != null) {
            String newhtml = BoilerpipeUtils.highlightingHtml(html, "1");

            List<Object> data = getRootContent(newhtml, sourceRule);

            dataEntities = getListContent(data, sourceRule);

        }
        return dataEntities;
    }


    public static List<Object> getRootContent(String html, NowRuleBean sourceRule) {
        JXDocument jxDocument = JXDocument.create(html);
        List<Object> listElements = null;
        try {
            String elementStr = "";
            String listXpath =sourceRule.getListXpath();
            if (listXpath != null) {
                switch (listXpath) {
                    case "1":
                        elementStr = "//*/table";
                        break;
                    case "2":
                        elementStr = "//*/table";
                        break;
                    default:
                        if (listXpath.startsWith("[")) {
                            elementStr = "//*" +listXpath;
                        } else {
                            elementStr = "//*/" + listXpath;
                        }
                        break;
                }
                try {
                    listElements = jxDocument.sel(elementStr);
                }catch (XpathSyntaxErrorException e){

                }

            }
            try {
                LogUtil.info("轻舟自动解析", elementStr + "共有" + listElements.size() + "个");

            }catch (NullPointerException e){
                e.printStackTrace();
            }
        } catch (InputMismatchException e) {

        }
        return listElements;
    }


    public Vector<DataEntity> getListContent(List<Object> listElements,NowRuleBean sourceRule) {
        Vector<DataEntity> dataEntities = new Vector<>();

        String title = null, cover = null, summary = null, link = null, date = null;
        JXDocument jxDocument = null;
        if (listElements != null) {


            String imgStr = sourceRule.getCoverXpath();
            if (listElements.size() > 0) {


//                for (int i = 0;i<listElements.size();i++){
//
//                    jxDocument = JXDocument.create(listElements.get(i).toString());
//                    List<Object> textlists = jxDocument.sel("//*[@class=\"x-boilerpipe-mark1\"]/text()");
//                    List<Object> textlistsq = null;
//                    if(i>0&&i<listElements.size()){
//                        textlistsq = JXDocument.create(listElements.get(i-1).toString()).sel("//*[@class=\"x-boilerpipe-mark1\"]/text()");
//
//                    }
//                    if(textlistsq!=null) {
//                        try {
//                            for (int j = 0; j < textlists.size(); j++) {
//                                for (int k = 0; k < textlistsq.size(); k++) {
//                                    if (textlists.get(j).toString().length()==textlistsq.get(k).toString().length()) {
//                                        //表示相同？
//                                        LogUtil.info("轻舟自动去重", "当前text" + textlists.get(j).toString() + ",上一个的text" + textlistsq.get(k).toString());
//                                        textlists.remove(j);
//                                    }
//                                }
//
//                            }
//                        }catch (IndexOutOfBoundsException e){
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                }



                for (Object list2 : listElements) {

                    jxDocument = JXDocument.create(list2.toString());

                    List<Object> alists = jxDocument.sel("//*/a/@href");
                    String imgXpath = "";
                    if (imgStr != null) {
                        switch (imgStr) {
                            case "1":
                                imgXpath = "//*/img/@src";
                                break;
                            case "2":
                                imgXpath = "//*/img/@data-src";
                                break;
                            case "3":
                                imgXpath = "//*/img/@data-original";
                                break;
                            case "4":
                                imgXpath = "//*/img/@_src";
                                break;
                            case "5":
                                imgXpath = "//*/img/@_Src";
                                break;
                            default:
                                if (imgStr.equals("")) {
                                    imgXpath = "//*/img/@src";
                                } else {
                                    imgXpath = "//*/" + imgStr;
                                }
                                break;
                        }
                    }
                    List<Object> imglists = jxDocument.sel(imgXpath);
                    List<Object> textlists = jxDocument.sel("//*[@class=\"x-boilerpipe-mark1\"]/text()");

                    if (alists.size() != 0 && textlists.size() != 0) {



                        title = getTitle(textlists, sourceRule);
                        summary = getSummary(textlists, sourceRule);
                        date = getDate(textlists, sourceRule);

                        link = getLink(alists, sourceRule);
                        cover = getCover(imglists, sourceRule);

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

                                if (!"".equals(title)&&title.length()>=2) {
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


                    }else{
                        LogUtil.info("轻舟自动调试", "获取的数据为空" );

                    }
                }


            }
        }
        return dataEntities;
    }


    private String getTitle(List<Object> textlists, NowRuleBean sourceRule) {
        String title = null;
        //标题xpaht不为空
        if (!"".equals(sourceRule.getTitleXpath())) {
            //获取列表中所有标题的值

            try {
                title = textlists.get(Integer.parseInt(sourceRule.getTitleXpath()) - 1).toString();
                LogUtil.info("轻舟自动调试", "处理前的标题" + title);

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            if (title != null) {
                if (!"".equals(sourceRule.getTitleProcessingValue())) {
                    //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                    title = AnalysisUtils.getInstance().processingValue(title, sourceRule.getTitleProcessingValue(), sourceRule.getUrl(), 0);
                }


                LogUtil.info("轻舟调试", "处理后的标题" + title);
            } else {

                LogUtil.info("轻舟调试警告", "没有解析到标题时");
            }
        }
        return title;
    }


    private String getLink(List<Object> alists, NowRuleBean sourceRule) {
        String link = null;
        //标题xpaht不为空
        if (!"".equals(sourceRule.getLinkXpath())) {
            //获取列表中所有标题的值

            try {
                link = alists.get(Integer.parseInt(sourceRule.getLinkXpath()) - 1).toString();
                LogUtil.info("轻舟自动调试","处理前的链接"+link);

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            if (link != null) {
                if (!"".equals(sourceRule.getLinkProcessingValue())) {
                    //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                    link = AnalysisUtils.getInstance().processingValue(link, sourceRule.getLinkProcessingValue(), sourceRule.getUrl(), 0);
                }

                if(!link.startsWith("http")){
                    link = XpathUtils.getHost(sourceRule.getUrl())+link;
                }

                LogUtil.info("轻舟自动调试","处理后的连接"+link);
            } else {

                LogUtil.info("轻舟自动调试警告","没有解析到链接");
            }
        }
        return link;
    }

    private String getCover(List<Object> imglists, NowRuleBean sourceRule) {
        String cover = null;
        //标题xpaht不为空
        if (!"".equals(sourceRule.getCoverXpath())) {
            //获取列表中所有标题的值

            try {
                cover = imglists.get(0).toString();
                LogUtil.info("轻舟自动调试", "处理前的图片" + cover);

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            if (cover != null) {
                if (!"".equals(sourceRule.getCoverProcessingValue())) {
                    //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                    cover = AnalysisUtils.getInstance().processingValue(cover, sourceRule.getCoverProcessingValue(), sourceRule.getUrl(), 0);
                }
                if(!cover.startsWith("http")){
                    cover = XpathUtils.getHost(sourceRule.getUrl())+cover;
                }

                LogUtil.info("轻舟自动调试", "处理后的图片" + cover);
            } else {

                LogUtil.info("轻舟自动调试", "没有解析到图片");
            }
        }
        return cover;
    }

    private String getSummary(List<Object> textlists, NowRuleBean sourceRule) {
        String summary = null;
        //标题xpaht不为空
        if (!"".equals(sourceRule.getSummaryXpath())) {
            //获取列表中所有标题的值

            try {
                summary = textlists.get(Integer.parseInt(sourceRule.getSummaryXpath()) - 1).toString();
                LogUtil.info("轻舟自动调试", "处理前的简介" + summary);

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            if (summary != null) {


                LogUtil.info("轻舟自动调试", "处理后的简介" + summary);
            } else {

                LogUtil.info("轻舟自动调试", "没有解析到简介");
            }
        }
        return summary;
    }


    private String getDate(List<Object> textlists, NowRuleBean sourceRule) {
        String date = null;
        //标题xpaht不为空
        if (!"".equals(sourceRule.getDateXpath())) {
            //获取列表中所有标题的值

            try {
                date = textlists.get(Integer.parseInt(sourceRule.getDateXpath()) - 1).toString();
                LogUtil.info("轻舟自动调试", "处理前的日期" + date);

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            if (date != null) {


                LogUtil.info("轻舟自动调试", "处理后的日期" + date);
            } else {

                LogUtil.info("轻舟自动调试", "没有解析到日期");
            }
        }
        return date;
    }


    private void ps(List<Object> textlists)
    {

    }
}
