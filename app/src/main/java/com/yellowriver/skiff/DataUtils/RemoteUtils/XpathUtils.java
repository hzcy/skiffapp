package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.seimicrawler.xpath.JXDocument;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Vector;

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
            //将DOM转为jx
            JXDocument jxDocument = JXDocument.create(doc.toString());
            //通过列表xpath获取的列表的dom
            if (sourceRule.getListXpath().startsWith("//*")) {
                List<Object> lists = null;
                try {
                    lists  = jxDocument.sel(sourceRule.getListXpath());
                }catch (InputMismatchException e)
                {

                }


                if (lists != null) {
                    //Log.d(TAG, "GetData: "+lists.toString());
                    //先定义标题 图片 简介 链接 日期
                    String title = null, cover = null, summary = null, link = null, date = null;
                    Log.d(TAG, "Xpath获取的数据大小: " + lists.size());
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
                                    Log.d(TAG, "Xpath: 标题为空");
                                }
                            } else {
                                Log.d(TAG, "Xpath: 标题为空");
                            }
                        } else {
                            //不是搜索 必须有标题和链接
                            if (title != null || link != null) {
                                if (!"".equals(title)) {
                                    //将获取的标题 简介 图片 链接保存为实体
                                    DataEntity dataEntity = new DataEntity(title, summary, cover, link, date, sourceRule.getViewMode(), sourceRule.getLinkType());
                                    dataEntities.add(dataEntity);

                                } else {
                                    Log.d(TAG, "Xpath: 标题或链接为空");
                                }
                            } else {
                                Log.d(TAG, "Xpath: 标题或链接为空");
                            }
                        }

                    }

                } else {
                    Log.d(TAG, "Xpath: 获取的数据为空");
                }
            }else {
                Log.d(TAG, "Xpath: 源配置不正确");
            }
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
            List<Object> titles = jxDocument.sel(sourceRule.getTitleXpath());
            if (titles.size() != 0) {
                Log.d(TAG, "处理前的标题 " + titles.get(0).toString());
                //获取标题
                title = titles.get(0).toString();
                if (!"".equals(sourceRule.getTitleProcessingValue())) {
                    //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                    title = AnalysisUtils.getInstance().processingValue(title, sourceRule.getTitleProcessingValue(), sourceRule.getUrl(), 0);
                }

                Log.d(TAG, "处理后的标题" + title);


            } else {
                Log.d(TAG, "没有解析到标题时显示html：" + titles.toString());
            }
        }
        return title;
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

                Log.d(TAG, "处理前的链接 " + links.get(0).toString());
                link = links.get(0).toString();
                if (!"".equals(sourceRule.getLinkProcessingValue())) {

                    link = AnalysisUtils.getInstance().processingValue(link, sourceRule.getLinkProcessingValue(), sourceRule.getUrl(), 0);
                }

                Log.d(TAG, "处理后的连接" + link);
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
                Log.d(TAG, "处理前的图片 " + covers.get(0).toString());
                cover = covers.get(0).toString();
                //图片处理完整
                if (!"".equals(sourceRule.getCoverProcessingValue())) {
                    cover = AnalysisUtils.getInstance().processingValue(cover, sourceRule.getCoverProcessingValue(), sourceRule.getUrl(), 0);
                }
                Log.d(TAG, "处理后的图片 " + cover);
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
            List<Object> summarys = jxDocument.sel(sourceRule.getSummaryXpath());
            if (summarys.size() != 0) {

                summary = summarys.get(0).toString();

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
            List<Object> dates = jxDocument.sel(sourceRule.getDateXpath());
            if (dates.size() != 0) {
                //获取日期

                date = dates.get(0).toString();

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
            PostData = sourceRule.getPostData().replace("{QZQuery}", sourceRule.getQuery());
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
    private String getUrl(NowRuleBean sourceRule, int page) {
        String actionUrl = null;
        //GET
        if (!"".equals(sourceRule.getQuery())) {
            //表示要搜索 替换url上的搜索关键字
            actionUrl = sourceRule.getUrl().replace("{QZQuery}", sourceRule.getQuery());
            Log.d(TAG, "GetHtmlDoc: " + actionUrl);
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

                //包含
                actionUrl = actionUrl.replace("{QZPage}", String.valueOf(page));
                actionUrl = actionUrl.replace("{QZLink}", sourceRule.getUrl());
                actionUrl = actionUrl.replace("{QZQuery}", sourceRule.getQuery());
                Log.d(TAG, "GetHtmlDoc: " + actionUrl);

            } else {
                //加载多页时有bug
                actionUrl = AnalysisUtils.getLink(actionUrl, nexturlxpath);
                if (!"".equals(sourceRule.getNextProcessingValue())) {
                    //第一步处理 处理图片 如果需要处理的值不为空  （一般情况下使用左拼接将图片拼接完整）
                    actionUrl = AnalysisUtils.getInstance().processingValue(actionUrl, sourceRule.getNextProcessingValue(), sourceRule.getUrl(), page);
                }

                Log.d(TAG, "GetHtmlDoc: " + actionUrl);
            }

        }
        Log.d(TAG, "GetHtmlDoc: " + actionUrl);


        return actionUrl;
    }


    /**
     * get 获取header 转成hashmap
     *
     * @param headers
     * @return
     */
    private HashMap<String, String> getHeaders(String headers) {
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


}
