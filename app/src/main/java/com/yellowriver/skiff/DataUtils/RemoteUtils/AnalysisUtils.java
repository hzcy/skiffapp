package com.yellowriver.skiff.DataUtils.RemoteUtils;


import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXDocument;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;
import com.yellowriver.skiff.Help.LogUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 * SearchGO 轻舟 的解析模式
 */
public class AnalysisUtils {

    private static final String JSONTYPE = "{QZJSon}";
    private static final String GBK = "2";

    private static final AnalysisUtils instance = new AnalysisUtils();

    private AnalysisUtils() {
    }

    public static AnalysisUtils getInstance() {
        return instance;
    }

    private static final String TAG = "AnalysisUtils";

    /**
     * Xpath通用解析方法
     *
     * @param homeEntity 数据库获取的源
     * @param step       步骤 根据源的不同步骤进行解析
     * @param url        解析URL
     * @param query      搜索关键字 如果是搜索类型
     */
    public Vector<DataEntity> MainAnalysis(HomeEntity homeEntity, String step, String url, String query, int page) {
        Log.d(TAG, "通用解析方法开始解析...");
        Vector<DataEntity> dataEntities;
        //计算加载时间
        long startTime = System.currentTimeMillis();
        //第一步 获取所需规则
        NowRuleBean sourceRule = getValueByStep(homeEntity, step, query, url);
        //JSON

        if (sourceRule.getListXpath().contains(JSONTYPE)) {
            //JSon
            Log.d(TAG, "解析数据类型为JSON");
            String json = JsonUtils.getInstance().getJson(sourceRule, page);
            dataEntities = JsonUtils.getInstance().getJsonData(json, sourceRule);

        } else {
            //Xpath
            //第二步 根据加载规则获取网页html-doc
            Log.d(TAG, "解析数据类型为xpath");

            if(!sourceRule.getListXpath().startsWith("//*")){


                //自动化解析
                String html = BoilerpipeGetHtml.getInstance().GetHtmlDoc(sourceRule, page);
                if(html!=null) {
                    dataEntities = BoilerpipeGetHtml.getInstance().GetData(html, sourceRule);

                }else{
                    dataEntities = null;
                }
            }else{

            Document doc = XpathUtils.getInstance().GetHtmlDoc(sourceRule, page);
            //第三步 根据xpath规则获取所需数据
            dataEntities = XpathUtils.getInstance().GetData(doc, sourceRule);
            }
        }
        long consumingTime = System.currentTimeMillis() - startTime;

        Log.d(TAG, "通用解析方法结束，加载耗时：" + consumingTime + "毫秒");

        //Log.d(TAG, "MainAnalysis: "+dataEntities.size());
        return dataEntities;

    }

    // 获取所需规则
    public NowRuleBean getValueByStep(HomeEntity homeEntity, String step, String query, String url) {
        NowRuleBean sourceRule = null;
        //根据不同步骤 获取该步骤的xpath
        switch (step) {
            case "1":
                //第一步
                sourceRule = fristStepRule(homeEntity, query);
                break;
            case "2":

                sourceRule = secondStepRule(homeEntity, query, url);

                break;
            case "3":
                sourceRule = thirdStepRule(homeEntity, query, url);
                break;
            default:
                break;
        }
        return sourceRule;
    }

    //第一步需要的数据
    private NowRuleBean fristStepRule(HomeEntity homeEntity, String query) {
        NowRuleBean sourceRule = new NowRuleBean();
        sourceRule.setQzGroupName(homeEntity.getGrouping());
        sourceRule.setQzSoucesType(homeEntity.getType());
        sourceRule.setQzSourcesName(homeEntity.getTitle());
        sourceRule.setQzStep("1");
        //源地址
        sourceRule.setUrl(homeEntity.getFirsturl());
        sourceRule.setNextPageXpath(homeEntity.getFirstNextPageXpath());
        sourceRule.setReadImgSrc(homeEntity.getFirstReadModeImgSrc());
        sourceRule.setReadNextPage(homeEntity.getFirstReadModeNextPage());
        sourceRule.setReadXpath(homeEntity.getFirstReadModeXpath());
        sourceRule.setReadIsAjax(homeEntity.getFirstReadAjax());
        sourceRule.setFinalSummary(homeEntity.getFinalSummary());
        //搜索关键字
        if (GBK.equals(homeEntity.getFirstQueryIsURLencode())) {

            try {
                if (GBK.equals(homeEntity.getFirstCharset())) {
                    sourceRule.setQuery(URLEncoder.encode(query, "GBK"));
                } else {
                    sourceRule.setQuery(URLEncoder.encode(query, "UTF-8"));
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            sourceRule.setQuery(query);
        }
        //列表xpath
        sourceRule.setListXpath(homeEntity.getFirstListXpath());
        //标题xpath
        sourceRule.setTitleXpath(homeEntity.getFirstTitleXpath());
        //简介xpath
        sourceRule.setSummaryXpath(homeEntity.getFirstSummaryXpath());
        //封面xpath
        sourceRule.setCoverXpath(homeEntity.getFirstCoverXpath());
        //链接xpath
        sourceRule.setLinkXpath(homeEntity.getFirstLinkXpath());
        //日期xpath
        sourceRule.setDateXpath(homeEntity.getFirstDateXpath());
        //请求方式 get or post
        sourceRule.setRequestMethod(homeEntity.getFirstRequestMethod());
        //显示方式
        sourceRule.setViewMode(homeEntity.getFirstViewMode());
        //链接类型
        sourceRule.setLinkType(homeEntity.getFirstLinkType());
        //请求头
        sourceRule.setHeaders(homeEntity.getFirstHeaders());
        //post的data
        sourceRule.setPostData(homeEntity.getFirstPostData());
        //页面编码
        sourceRule.setCharset(homeEntity.getFirstCharset());
        sourceRule.setIsAjax(homeEntity.getFirstAjax());
        sourceRule.setTitleProcessingValue(homeEntity.getFirstTitleProcessingValue());
        //封面处理
        sourceRule.setCoverProcessingValue(homeEntity.getFirstCoverProcessingValue());
        //链接处理
        sourceRule.setLinkProcessingValue(homeEntity.getFirstLinkProcessingValue());
        sourceRule.setNextProcessingValue(homeEntity.getFirstNextProcessingValue());

        return sourceRule;
    }


    //第二步需要的数据
    private NowRuleBean secondStepRule(HomeEntity homeEntity, String query, String url) {
        NowRuleBean sourceRule = new NowRuleBean();
        sourceRule.setQzGroupName(homeEntity.getGrouping());
        sourceRule.setQzSoucesType(homeEntity.getType());
        sourceRule.setQzSourcesName(homeEntity.getTitle());
        sourceRule.setQzStep("2");
        sourceRule.setFinalSummary(homeEntity.getFinalSummary());
        //第二步
        sourceRule.setUrl(url);
        sourceRule.setQuery(query);
        sourceRule.setNextPageXpath(homeEntity.getSecondNextPageXpath());
        sourceRule.setReadImgSrc(homeEntity.getSecondReadModeImgSrc());
        sourceRule.setReadXpath(homeEntity.getSecondReadModeXpath());
        sourceRule.setReadNextPage(homeEntity.getSecondReadModeNextPage());
        sourceRule.setReadIsAjax(homeEntity.getSecondReadAjax());
        //列表xpath
        sourceRule.setListXpath(homeEntity.getSecondListXpath());
        //标题xpath
        sourceRule.setTitleXpath(homeEntity.getSecondTitleXpath());
        //简介xpath
        sourceRule.setSummaryXpath(homeEntity.getSecondSummaryXpath());
        //封面xpath
        sourceRule.setCoverXpath(homeEntity.getSecondCoverXpath());
        //链接xpath
        sourceRule.setLinkXpath(homeEntity.getSecondLinkXpath());
        //日期xpath
        sourceRule.setDateXpath(homeEntity.getSecondDateXpath());
        //请求方式 get or post
        sourceRule.setRequestMethod(homeEntity.getSecondRequestMethod());
        //显示方式
        sourceRule.setViewMode(homeEntity.getSecondViewMode());
        //链接类型
        sourceRule.setLinkType(homeEntity.getSecondLinkType());
        //请求头
        sourceRule.setHeaders(homeEntity.getSecondHeaders());
        //post的data
        sourceRule.setPostData(homeEntity.getSecondPostData());
        //页面编码
        sourceRule.setCharset(homeEntity.getSecondCharset());
        sourceRule.setIsAjax(homeEntity.getSecondAjax());
        sourceRule.setTitleProcessingValue(homeEntity.getSecondTitleProcessingValue());
        //封面处理
        sourceRule.setCoverProcessingValue(homeEntity.getSecondCoverProcessingValue());
        //链接处理
        sourceRule.setLinkProcessingValue(homeEntity.getSecondLinkProcessingValue());
        sourceRule.setNextProcessingValue(homeEntity.getSecondNextProcessingValue());

        return sourceRule;
    }

    //第三步需要的数据
    private NowRuleBean thirdStepRule(HomeEntity homeEntity, String query, String url) {
        NowRuleBean sourceRule = new NowRuleBean();
        sourceRule.setQzGroupName(homeEntity.getGrouping());
        sourceRule.setQzSoucesType(homeEntity.getType());
        sourceRule.setQzSourcesName(homeEntity.getTitle());
        sourceRule.setQzStep("3");
        sourceRule.setFinalSummary(homeEntity.getFinalSummary());
        //第三步
        sourceRule.setUrl(url);
        sourceRule.setQuery(query);
        sourceRule.setNextPageXpath(homeEntity.getThirdNextPageXpath());
        sourceRule.setReadImgSrc(homeEntity.getThirdReadModeImgSrc());
        sourceRule.setReadXpath(homeEntity.getThirdReadModeXpath());
        sourceRule.setReadNextPage(homeEntity.getThirdReadModeNextPage());
        sourceRule.setReadIsAjax(homeEntity.getThirdReadAjax());
        //列表xpath
        sourceRule.setListXpath(homeEntity.getThirdListXpath());
        //标题xpath
        sourceRule.setTitleXpath(homeEntity.getThirdTitleXpath());
        //简介xpath
        sourceRule.setSummaryXpath(homeEntity.getThirdSummaryXpath());
        //封面xpath
        sourceRule.setCoverXpath(homeEntity.getThirdCoverXpath());
        //链接xpath
        sourceRule.setLinkXpath(homeEntity.getThirdLinkXpath());
        //日期xpath
        sourceRule.setDateXpath(homeEntity.getThirdDateXpath());
        //请求方式 get or post
        sourceRule.setRequestMethod(homeEntity.getThirdRequestMethod());
        //显示方式
        sourceRule.setViewMode(homeEntity.getThirdViewMode());
        //链接类型
        sourceRule.setLinkType(homeEntity.getThirdLinkType());
        //请求头
        sourceRule.setHeaders(homeEntity.getThirdHeaders());
        //post的data
        sourceRule.setPostData(homeEntity.getThirdPostData());
        //页面编码
        sourceRule.setCharset(homeEntity.getThirdCharset());
        sourceRule.setIsAjax(homeEntity.getThirdAjax());
        sourceRule.setTitleProcessingValue(homeEntity.getThirdTitleProcessingValue());
        //封面处理
        sourceRule.setCoverProcessingValue(homeEntity.getThirdCoverProcessingValue());
        //链接处理
        sourceRule.setLinkProcessingValue(homeEntity.getThirdLinkProcessingValue());
        sourceRule.setNextProcessingValue(homeEntity.getThirdNextProcessingValue());

        return sourceRule;
    }


    //处理值
    public String processingValue(String value, String processingValue, String url,int page) {
        String resultValue = value;
        if (JsonUtils.isJSONValid(processingValue)) {
            Log.d(TAG, "processingValue: 是json");
            if (processingValue.startsWith("[")) {
                resultValue = processingArrayValue(processingValue, value, url,page);

            } else {
                resultValue = processingOneValue(processingValue, value, url);
            }
        }else{
            Log.d(TAG, "processingValue: 不是json");
        }
        return resultValue;
    }




    /**
     * json 多个处理规则时
     *
     * @param arrayJson
     * @param value
     * @param url
     * @return
     */
    private String processingArrayValue(String arrayJson, String value, String url,int page) {
        JSONArray jsonArray = JSON.parseArray(arrayJson);
        String newValue = value;
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.size(); i++) {
                String type = jsonArray.getJSONObject(i).getString("type");
                String valueStr = jsonArray.getJSONObject(i).getString("value");
                switch (type) {
                    case "leftjoin":
                        if ("{QZLink}".equals(valueStr)) {
                            if (!"".equals(url)) {
                                newValue = url + newValue;
                            }
                        } else {
                            newValue = valueStr + newValue;
                        }
                        LogUtil.info("轻舟处理值","当前值"+newValue);
                        break;
                    case "rightjoin":
                        newValue = newValue + valueStr;

                        break;
                    case "substring":
                        String[] sourceStrArray = valueStr.split(",");
                        if (sourceStrArray.length == 2) {
                            String beginindex = sourceStrArray[0];
                            String endindex = sourceStrArray[1];
                            try {
                                if (endindex.startsWith("-")) {
                                    endindex = endindex.replace("-", "");
                                    newValue = newValue.substring(Integer.valueOf(beginindex), newValue.length() - Integer.valueOf(endindex));
                                } else {
                                    newValue = newValue.substring(Integer.valueOf(beginindex), Integer.valueOf(endindex));
                                }
                            }catch (StringIndexOutOfBoundsException e){

                            }catch (NullPointerException e)
                            {

                            }

                        }
                        break;
                    case "replace":
                        String[] sourceStrArray2 = valueStr.split(",");
                        if (sourceStrArray2.length == 2) {
                            String oldStr = sourceStrArray2[0];
                            String newStr = sourceStrArray2[1];

                            newValue = newValue.replace(oldStr, newStr);

                        }else{
                            LogUtil.info("轻舟处理值","当前值"+newValue);
                            newValue = newValue.replace(valueStr,"");
                        }
                        break;
                    case "delete":

                        if (value.indexOf(valueStr) != -1) {
                            newValue = "";
                        } else {
                            newValue = newValue;
                        }

                        break;
                    case "eval":
                        Log.d(TAG, "processingArrayValue: "+valueStr);
                        if(valueStr.indexOf("{QZPage}")!=-1){
                            newValue = valueStr.replace("{QZPage}",String.valueOf(page));

                        }
                        Log.d(TAG, "processingArrayValue: "+newValue);
                        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");
                        String expression = newValue;
                        Log.d(TAG, "processingArrayValue: cccccc"+expression);
                        try {
                            String result = String.valueOf(Math.round((double)(engine.eval(expression))));
                            Log.d(TAG, "processingArrayValue: 结果"+result);
                            newValue = result;
                        } catch (ScriptException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "imgReferer":
                        Log.d(TAG, "processingArrayValue: "+valueStr);
                        if(valueStr.indexOf("{QZLink}")!=-1){
                            newValue = newValue + "{QZ}" + valueStr;
                            newValue = newValue.replace("{QZLink}",url);

                        }else {
                            newValue = newValue + "{QZ}" + valueStr;
                        }
                        break;
                    case "Xpath":
                        Log.d(TAG, "processingArrayValue: "+valueStr);
                        newValue = XpathUtils.getXpath(newValue, valueStr);
                        break;

                    case "ReadReplace":
                        String[] sourceStrArray3 = valueStr.split(",");
                        if (sourceStrArray3.length == 2) {
                            String oldStr = sourceStrArray3[0];
                            String newStr = sourceStrArray3[1];

                            newValue = newValue.replaceAll(oldStr, newStr);

                        }else{
                            LogUtil.info("轻舟处理值","当前值"+newValue);
                            try {
                                newValue = newValue.replaceAll(valueStr, "");
                            }catch (PatternSyntaxException e){
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }

            }
        }
        return newValue;
    }




    /**
     * json 单个处理规则时
     *
     * @param arrayJson
     * @param value
     * @param url
     * @return
     */
    private String processingOneValue(String arrayJson, String value, String url) {
        String newValue = null;
        JSONObject jsonObject = JSON.parseObject(arrayJson);
        if (jsonObject != null) {
            for (int i = 0; i < jsonObject.size(); i++) {

                String type = jsonObject.getString("type");
                String valueStr = jsonObject.getString("value");
                switch (type) {
                    case "leftjoin":
                        if ("{QZLink}".equals(valueStr)) {
                            if (!"".equals(url)) {
                                newValue = url + value;
                            }
                        } else {
                            newValue = valueStr + value;
                        }
                        break;
                    case "rightjoin":
                        newValue = value + valueStr;

                        break;
                    case "substring":
                        String[] sourceStrArray = valueStr.split(",");
                        if (sourceStrArray.length == 2) {

                            String beginindex = sourceStrArray[0];
                            String endindex = sourceStrArray[1];
                            try {
                                if (endindex.startsWith("-")) {
                                    endindex = endindex.replace("-", "");
                                    newValue = value.substring(Integer.valueOf(beginindex), value.length() - Integer.valueOf(endindex));
                                } else {
                                    newValue = value.substring(Integer.valueOf(beginindex), Integer.valueOf(endindex));
                                }
                            }catch (NullPointerException e)
                            {

                            }catch (StringIndexOutOfBoundsException e){

                            }

                        }
                        break;
                    case "replace":
                        String[] sourceStrArray2 = valueStr.split(",");
                        if (sourceStrArray2.length == 2) {
                            String oldStr = sourceStrArray2[0];
                            String newStr = sourceStrArray2[1];
                            Log.d(TAG, "processingOneValue: "+oldStr+newStr);
                            newValue = value.replace(oldStr, newStr);
                            Log.d(TAG, "processingOneValue: "+value);

                        }else{
                            newValue = value.replace(valueStr,"");
                        }
                        Log.d(TAG, "processingOneValue: "+newValue);
                        break;
                    case "delete":

                        if (value.indexOf(valueStr) != -1) {
                            newValue = "";
                        } else {
                            newValue = value;
                        }

                        break;
                    case "imgReferer":
                        Log.d(TAG, "processingArrayValue: "+valueStr);
                        if(valueStr.indexOf("{QZLink}")!=-1){
                            newValue = value + "{QZ}" + valueStr;
                            newValue = newValue.replace("{QZLink}",url);

                        }else {
                            newValue = value + "{QZ}" + valueStr;
                        }
                        break;
                    case "ReadReplace":
                        String[] sourceStrArray3 = valueStr.split(",");
                        if (sourceStrArray3.length == 2) {
                            String oldStr = sourceStrArray3[0];
                            String newStr = sourceStrArray3[1];

                            newValue = value.replaceAll(oldStr, newStr);

                        }else{
                            LogUtil.info("轻舟处理值","当前值"+newValue);
                            newValue = value.replaceAll(valueStr,"");
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return newValue;
    }


    //根据xpathh获取网页的某个值
    public static String getLink(String url, String readXpath) {

        String link = null;
        // 获取文档内容
        Document doc = Jsoup.parse(NetUtils.getInstance().getRequest(url, "1"));

        if (doc != null) {
            //将DOM转为jx
            JXDocument jxDocument = JXDocument.create(doc.toString());
            //通过列表xpath获取的列表的dom

            List<Object> lists = jxDocument.sel(readXpath);
            if (lists != null) {
                if (lists.size() > 0) {
                    link = lists.get(0).toString();
                }
            }


        }

        return link;
    }

}
