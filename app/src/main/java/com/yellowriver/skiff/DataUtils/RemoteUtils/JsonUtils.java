package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;

import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

/**
 * Json格式解析
 */
public class JsonUtils {
    private static final String TAG = "JsonUtils";
    private static final String GET = "GET";
    private static final String QZLINK = "{QZLink}";
    private static final JsonUtils instance = new JsonUtils();

    private JsonUtils() {
    }

    public static JsonUtils getInstance() {
        return instance;
    }

    //json相关
    public String getJson(NowRuleBean sourceRule, int page) {
        String actionUrl = null;
        String json;
        if (GET.equals(sourceRule.getRequestMethod())) {

            //GET
            if (!"".equals(sourceRule.getQuery())) {
                //表示要搜索 替换url上的搜索关键字
                actionUrl = sourceRule.getUrl().replace("{QZQuery}", sourceRule.getQuery());
                actionUrl = actionUrl.replace("{QZPage}", String.valueOf(page));
                //Log.d(TAG, "GetJson: " + actionUrl);
            } else {
                actionUrl = sourceRule.getUrl();
            }

            if (page == 1) {

            } else {
                String nexturlxpath = sourceRule.getNextPageXpath();
                Log.d(TAG, "GetJson: " + nexturlxpath);
                if (nexturlxpath.contains(QZLINK)) {

                    actionUrl = nexturlxpath.replace("{QZLink}", actionUrl);
                    Log.d(TAG, "GetHtmlDoc: " + actionUrl);
                    if (!"".equals(sourceRule.getNextProcessingValue())) {
                        //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                        actionUrl = AnalysisUtils.getInstance().processingValue(actionUrl, sourceRule.getNextProcessingValue(), sourceRule.getUrl(), page);
                    }
                    Log.d(TAG, "GetHtmlDoc: " + actionUrl);
                    //包含
                    actionUrl = actionUrl.replace("{QZPage}", String.valueOf(page));
                    actionUrl = actionUrl.replace("{QZLink}", sourceRule.getUrl());


                } else {
                    actionUrl = AnalysisUtils.getLink(null, nexturlxpath);
                }

            }
            Log.d(TAG, "处理JSON类：目标地址： " + actionUrl);


            //请求头  源使用json写hearder 将json格式的hearder转为hashmap
            HashMap<String, String> headerMap = null;
            if (!"".equals(sourceRule.getHeaders())) {
                headerMap = JSON.parseObject(sourceRule.getHeaders(), HashMap.class);
            }
            //如果是js异步加载数据 用htmlutil  1.不是 2是

            //获取dom
            //Log.d(TAG, "FirstAnalysis: "+headerMap+url);

            json = NetUtils.getInstance().getRequest(actionUrl, headerMap, sourceRule.getCharset());


        } else {
            //post
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
                PostData = sourceRule.getPostData().replace("{QZQuery}", sourceRule.getQuery());
                //Log.d(TAG, "GetHtmlDoc postdata: " + PostData);
                paramsMap = JSON.parseObject(PostData, HashMap.class);
            }
            if (!"".equals(sourceRule.getHeaders())) {
                headerMap = JSON.parseObject(sourceRule.getHeaders(), HashMap.class);
            }

            //如果是js异步加载数据 用htmlutil  1.不是 2是

            //获取dom
            //Log.d(TAG, "FirstAnalysis: "+headerMap+url);
            json = NetUtils.getInstance().requestPost(actionUrl, paramsMap, headerMap, sourceRule.getCharset());


        }
        return json;
    }


    public Vector<DataEntity> getJsonData(String json, NowRuleBean sourceRule) {

        Vector<DataEntity> dataEntities = new Vector<>();

        if (json != null) {
            //先定义标题 图片 简介 链接 日期
            String title = null, cover = null, summary = null, link = null, date = null;

            JSONObject jsonObject = JSON.parseObject(json);
            String[] sourceStrArray = sourceRule.getListXpath().replace("{QZJSon}", "").split("\\.");
            JSONArray datalist = null;
            if (sourceStrArray.length > 0) {


                for (int i = 0; i < sourceStrArray.length; i++) {

                    //最后一次获取的是JSONArray
                    if (i == sourceStrArray.length - 1 && jsonObject != null) {
                        datalist = jsonObject.getJSONArray(sourceStrArray[i]);
                    } else {
                        //获取的为JSONObject
                        jsonObject = Objects.requireNonNull(jsonObject).getJSONObject(sourceStrArray[i]);
                    }


                }


            } else {

                datalist = jsonObject.getJSONArray(sourceRule.getListXpath());
            }
            //Log.d(TAG, "GetJsonData: " + datalist);
            if (datalist != null) {
                if (datalist.size() > 0) {
                    Log.d(TAG, "JSon获取的数据大小: " + datalist.size());
                    for (int i = 0; i < datalist.size(); i++) {
                        JSONObject listarray = datalist.getJSONObject(i);

                        if (!"".equals(sourceRule.getTitleXpath())) {

                            if (sourceRule.getTitleXpath().contains("\\.")) {
                                title = getJSONbyxpath(listarray, sourceRule.getTitleXpath());

                            } else {
                                title = listarray.getString(sourceRule.getTitleXpath());
                            }


                        }
                        //如果连接xpath存在
                        if (!"".equals(sourceRule.getLinkXpath())) {
                            if (sourceRule.getLinkXpath().contains("\\.")) {
                                link = getJSONbyxpath(listarray, sourceRule.getLinkXpath());

                            } else {
                                link = listarray.getString(sourceRule.getLinkXpath());
                            }
                            if (!"".equals(sourceRule.getLinkProcessingValue())) {
                                //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                                link = AnalysisUtils.getInstance().processingValue(link, sourceRule.getLinkProcessingValue(), sourceRule.getUrl(), 0);
                            }
                        }
                        //如果简介xpath存在
                        if (!"".equals(sourceRule.getSummaryXpath())) {
                            if (sourceRule.getSummaryXpath().contains("\\.")) {
                                summary = getJSONbyxpath(listarray, sourceRule.getSummaryXpath());

                            } else {
                                summary = listarray.getString(sourceRule.getSummaryXpath());
                            }
                        }
                        //如果图片xpath存在
                        if (!"".equals(sourceRule.getCoverXpath())) {

                            if (sourceRule.getCoverXpath().contains("\\.")) {
                                cover = getJSONbyxpath(listarray, sourceRule.getCoverXpath());

                            } else {
                                cover = listarray.getString(sourceRule.getCoverXpath());
                            }
                            if (!"".equals(sourceRule.getCoverProcessingValue())) {
                                //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                                cover = AnalysisUtils.getInstance().processingValue(cover, sourceRule.getCoverProcessingValue(), sourceRule.getUrl(), 0);
                            }
                        }
                        //如果日期xpath存在
                        if (!"".equals(sourceRule.getDateXpath())) {
                            if (sourceRule.getDateXpath().contains("\\.")) {
                                date = getJSONbyxpath(listarray, sourceRule.getDateXpath());

                            } else {
                                date = listarray.getString(sourceRule.getDateXpath());
                            }

                        }
                        //标题和链接是必须选项
                        if (title != null && link != null) {


                            //将获取的标题 简介 图片 链接保存为实体
                            DataEntity dataEntity = new DataEntity(title, summary, cover, link, date, sourceRule.getViewMode(), sourceRule.getLinkType());
                            dataEntities.add(dataEntity);

                        } else {
                            Log.d(TAG, "JSon: 加载出错");
                        }

                    }
                }
            } else {
                Log.d(TAG, "JSon: 加载的数据为空");
            }

        }
        return dataEntities;
    }


    private static String getJSONbyxpath(JSONObject jsonObject, String key) {

        String[] sourceStrArray = key.split("\\.");
        String value;
        JSONArray datalist = null;
        if (sourceStrArray.length > 0) {


            for (int i = 0; i < sourceStrArray.length; i++) {

                if (i == sourceStrArray.length - 1) {
                    datalist = jsonObject.getJSONArray(sourceStrArray[i]);
                } else {
                    jsonObject = jsonObject.getJSONObject(sourceStrArray[i]);
                }


            }
            value = Objects.requireNonNull(datalist).toString();

        } else {

            value = jsonObject.getString(key);
        }
        return value;
    }


    public static boolean isJSONValid(String test) {
        try {
            JSONObject.parseObject(test);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
