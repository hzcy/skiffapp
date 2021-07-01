package com.yellowriver.skiff.Bean.HomeBean;

/**
 * 每一步所需规则  每到一步，生成一次数据，用来传递数据 和 获取值
 * @author huang
 * @date 2019/7/27
 */
public class NowRuleBean {



    //要解析的地址
    private String url;
    //搜索关键字
    private String query;

    private String qzGroupName;
    private String qzSoucesType;
    private String qzSourcesName;

    private String finalSummary;

    private String qzStep;

    //列表Xpath 标题xpath  简介xpath 封面图片xpath 链接xapth  日期xpath
    private String listXpath;
    private String nextPageXpath;
    private String titleXpath;
    private String summaryXpath;
    private String coverXpath;
    private String linkXpath;
    private String dateXpath;
    private String readXpath;
    private String readImgSrc;
    private String readNextPage;
    //请求方式  get or post
    private String RequestMethod;
    //显示方式
    private String ViewMode;
    //链接类型
    private String LinkType;
    //请求头
    private String Headers;
    //搜索
    private String PostData;
    //页面编码
    private String Charset;
    //是否异步加载
    private String IsAjax;
    private String ReadIsAjax;

    //解析值处理
    //标题处理
    private String TitleProcessingValue;
    //封面处理
    private String CoverProcessingValue;
    //链接处理
    private String LinkProcessingValue;
    //下一页处理
    private String NextProcessingValue;

    public String getNextProcessingValue() {
        return NextProcessingValue;
    }

    public void setNextProcessingValue(String nextProcessingValue) {
        NextProcessingValue = nextProcessingValue;
    }

    public String getFinalSummary() {
        return finalSummary;
    }

    public void setFinalSummary(String finalSummary) {
        this.finalSummary = finalSummary;
    }

    public String getReadNextPage() {
        return readNextPage;
    }

    public void setReadNextPage(String readNextPage) {
        this.readNextPage = readNextPage;
    }

    public String getReadIsAjax() {
        return ReadIsAjax;
    }

    public void setReadIsAjax(String readIsAjax) {
        ReadIsAjax = readIsAjax;
    }

    public String getQzStep() {
        return qzStep;
    }

    public void setQzStep(String qzStep) {
        this.qzStep = qzStep;
    }

    public String getQzSourcesName() {
        return qzSourcesName;
    }

    public void setQzSourcesName(String qzSourcesName) {
        this.qzSourcesName = qzSourcesName;
    }

    public String getQzGroupName() {
        return qzGroupName;
    }

    public void setQzGroupName(String qzGroupName) {
        this.qzGroupName = qzGroupName;
    }

    public String getQzSoucesType() {
        return qzSoucesType;
    }

    public void setQzSoucesType(String qzSoucesType) {
        this.qzSoucesType = qzSoucesType;
    }

    public String getReadXpath() {
        return readXpath;
    }

    public void setReadXpath(String readXpath) {
        this.readXpath = readXpath;
    }

    public String getReadImgSrc() {
        return readImgSrc;
    }

    public void setReadImgSrc(String readImgSrc) {
        this.readImgSrc = readImgSrc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getListXpath() {
        return listXpath;
    }

    public void setListXpath(String listXpath) {
        this.listXpath = listXpath;
    }

    public String getTitleXpath() {
        return titleXpath;
    }

    public void setTitleXpath(String titleXpath) {
        this.titleXpath = titleXpath;
    }

    public String getSummaryXpath() {
        return summaryXpath;
    }

    public void setSummaryXpath(String summaryXpath) {
        this.summaryXpath = summaryXpath;
    }

    public String getCoverXpath() {
        return coverXpath;
    }

    public void setCoverXpath(String coverXpath) {
        this.coverXpath = coverXpath;
    }

    public String getLinkXpath() {
        return linkXpath;
    }

    public void setLinkXpath(String linkXpath) {
        this.linkXpath = linkXpath;
    }

    public String getDateXpath() {
        return dateXpath;
    }

    public void setDateXpath(String dateXpath) {
        this.dateXpath = dateXpath;
    }

    public String getRequestMethod() {
        return RequestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        RequestMethod = requestMethod;
    }

    public String getViewMode() {
        return ViewMode;
    }

    public void setViewMode(String viewMode) {
        ViewMode = viewMode;
    }

    public String getLinkType() {
        return LinkType;
    }

    public void setLinkType(String linkType) {
        LinkType = linkType;
    }

    public String getHeaders() {
        return Headers;
    }

    public void setHeaders(String headers) {
        Headers = headers;
    }

    public String getPostData() {
        return PostData;
    }

    public void setPostData(String postData) {
        PostData = postData;
    }

    public String getCharset() {
        return Charset;
    }

    public void setCharset(String charset) {
        Charset = charset;
    }

    public String getIsAjax() {
        return IsAjax;
    }

    public void setIsAjax(String isAjax) {
        IsAjax = isAjax;
    }

    public String getCoverProcessingValue() {
        return CoverProcessingValue;
    }

    public void setCoverProcessingValue(String coverProcessingValue) {
        CoverProcessingValue = coverProcessingValue;
    }

    public String getLinkProcessingValue() {
        return LinkProcessingValue;
    }

    public void setLinkProcessingValue(String linkProcessingValue) {
        LinkProcessingValue = linkProcessingValue;
    }

    public String getTitleProcessingValue() {
        return TitleProcessingValue;
    }

    public void setTitleProcessingValue(String titleProcessingValue) {
        TitleProcessingValue = titleProcessingValue;
    }

    public String getNextPageXpath() {
        return nextPageXpath;
    }

    public void setNextPageXpath(String nextPageXpath) {
        this.nextPageXpath = nextPageXpath;
    }
}
