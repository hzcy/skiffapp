package com.yellowriver.skiff.Bean.DataBaseBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;


/**
 * 主要数据库 实体
 * @author huang
 */
@Entity
public class HomeEntity {

    @Id(autoincrement = true)
    private Long id;
    //分组
    private String grouping = "";
    //标题
    private String title = "";
    private String type = ""; //1.首页 2.搜索

    private String desc = ""; //源介绍
    //日期
    private String date = "";


    //****************************************************一级 first 开始
    //first源地址
    @NotNull
    private String Firsturl = "";
    //first源该页面编码
    private String FirstCharset = ""; //1.utf-8 2.gbk
    //first源 js动态页面 异步加载的数据
    private String FirstAjax = ""; //1.否 2.是
    //first源根域名 用作拼接图片
    private String Firstrooturl = "";

    //是否为post 是否有cookie等
    //first请求方式  Request Method
    private String FirstRequestMethod = ""; //1.GET 2.POST
    //first post值  postdata
    private String FirstPostData = "";
    //postdata query urlencode
    private String FirstQueryIsURLencode = ""; //1.不编码 2.进行编码
    //first headers  headers
    private String FirstHeaders = "";

    //需要获取的值  标题 简介 图片 链接
    //first 列表xpath listxpath
    private String FirstListXpath = "";
    //first 下一页的xpath  获取到的值为下一页的链接
    private String FirstNextPageXpath = "";
    //处理
    private String FirstNextProcessingValue = ""; //如果是substring  1,2 逗号分开
    //first 标题xpath titlexpath
    private String FirstTitleXpath = "";

    //处理
    private String FirstTitleProcessingValue = ""; //如果是substring  1,2 逗号分开
    //first 简介xpath summaryxpath
    private String FirstSummaryXpath = "";


    //first 图片xpath coverxpath
    private String FirstCoverXpath = "";

    //处理
    private String FirstCoverProcessingValue = ""; //如果是substring  1,2 逗号分开



    //first 链接xpath linkxpath
    private String FirstLinkXpath = "";

    //处理两次

    private String FirstLinkProcessingValue = ""; //如果是substring  1,2 逗号分开


    //first 日期xpath datexpath
    private String FirstDateXpath = "";


    //获取数据后展现形式
    //first 数据显示模式 viewmode
    private String FirstViewMode = ""; //1.仅标题垂直列表 2.仅标题网格列表 3.标题加简介垂直列表  4.图片加标题垂直列表 6.图片加标题网格列表 7.图标加标题加简介垂直列表 8.图片加标题加简介网格列表 9.
    //first 链接类型 linktype
    private String FirstLinkType = ""; //1.继续解析 2.网页 3.阅读模式 4.视频 5.文件 6.音频 7.图片
    //first 阅读模式 ReadModeXpath  获取内容的xpath
    private String FirstReadModeXpath = "";
    //阅读模式网页编码Charset
    private String FirstReadModeCharset = "";  //1.utf-8 2.gbk
    //first 阅读模式图片拼接 ReadModeImgSrc  一般为图片src 前面加上域名
    private String FirstReadModeImgSrc = "";


    //****************************************************一级 first 结束

    //****************************************************二级 Second 开始
    //Second源地址
    private String Secondurl = "";
    //Second源该页面编码
    private String SecondCharset = ""; //1.utf-8 2.gbk
    //Second源 js动态页面 异步加载的数据
    private String SecondAjax = ""; //1.否 2.是
    //是否为post 是否有cookie等
    //Second请求方式  Request Method
    private String SecondRequestMethod = ""; //1.GET 2.POST
    //Second post值  postdata
    private String SecondPostData = "";
    //Second headers  headers
    private String SecondHeaders = "";

    //需要获取的值  标题 简介 图片 链接
    private String SecondListXpath = "";
    //Second 下一页的xpath  获取到的值为下一页的链接
    private String SecondNextPageXpath = "";
    //处理
    private String SecondNextProcessingValue = ""; //如果是substring  1,2 逗号分开
    //Second 标题xpath titlexpath
    private String SecondTitleXpath = "";
    //处理
    private String SecondTitleProcessingValue = ""; //如果是substring  1,2 逗号分开
    //Second 简介xpath summaryxpath
    private String SecondSummaryXpath = "";


    //Second 图片xpath coverxpath
    private String SecondCoverXpath = "";
    //处理两次

    private String SecondCoverProcessingValue = ""; //如果是substring  1,2 逗号分开


    //Second 链接xpath linkxpath
    private String SecondLinkXpath = "";
    //处理两次

    private String SecondLinkProcessingValue = ""; //如果是substring  1,2 逗号分开

    //Second 日期xpath datexpath
    private String SecondDateXpath = "";


    //获取数据后展现形式
    //Second 数据显示模式 viewmode
    private String SecondViewMode = ""; //1.仅标题垂直列表 2.仅标题网格列表 3.标题加简介垂直列表 4.标题加简介网格列表 5.图片加标题垂直列表 6.图片加标题网格列表 7.图标加标题加简介垂直列表 8.图片加标题加简介网格列表 9.
    //Second 链接类型 linktype
    private String SecondLinkType = ""; //1.继续解析 2.网页 3.阅读模式 4.视频 5.文件 6.音频 7.图片
    //Second 阅读模式 ReadModeXpath  获取内容的xpath
    private String SecondReadModeXpath = "";
    //阅读模式网页编码Charset
    private String SecondReadModeCharset = "";  //1.utf-8 2.gbk
    //Second 阅读模式图片拼接 ReadModeImgSrc  一般为图片src 前面加上域名
    private String SecondReadModeImgSrc = "";

    //****************************************************二级 Second 结束

    //****************************************************三级 Third 开始
    //Third源地址
    private String Thirdurl = "";
    //Third源该页面编码
    private String ThirdCharset = ""; //1.utf-8 2.gbk
    //Third源 js动态页面 异步加载的数据
    private String ThirdAjax = ""; //1.否 2.是
    //是否为post 是否有cookie等
    //Third请求方式  Request Method
    private String ThirdRequestMethod = ""; //1.GET 2.POST
    //Third post值  postdata
    private String ThirdPostData = "";
    //Third headers  headers
    private String ThirdHeaders = "";

    //需要获取的值  标题 简介 图片 链接
    private String ThirdListXpath = "";
    //Third 下一页的xpath  获取到的值为下一页的链接
    private String ThirdNextPageXpath = "";
    //处理
    private String ThirdNextProcessingValue = ""; //如果是substring  1,2 逗号分开
    //Third 标题xpath titlexpath
    private String ThirdTitleXpath = "";
    //处理
    private String ThirdTitleProcessingValue = ""; //如果是substring  1,2 逗号分开
    //Third 简介xpath summaryxpath
    private String ThirdSummaryXpath = "";

    //Third 图片xpath coverxpath
    private String ThirdCoverXpath = "";
    //处理两次

    private String ThirdCoverProcessingValue = ""; //如果是substring  1,2 逗号分开

    //Third 链接xpath linkxpath
    private String ThirdLinkXpath = "";
    //处理两次

    private String ThirdLinkProcessingValue = ""; //如果是substring  1,2 逗号分开


    //Third 日期xpath datexpath
    private String ThirdDateXpath = "";



    //获取数据后展现形式
    //Third 数据显示模式 viewmode
    private String ThirdViewMode = ""; //1.仅标题垂直列表 2.仅标题网格列表 3.标题加简介垂直列表 4.标题加简介网格列表 5.图片加标题垂直列表 6.图片加标题网格列表 7.图标加标题加简介垂直列表 8.图片加标题加简介网格列表 9.
    //Third 链接类型 linktype
    private String ThirdLinkType = ""; //1.继续解析 2.网页 3.阅读模式 4.视频 5.文件 6.音频 7.图片
    //Third 阅读模式 ReadModeXpath  获取内容的xpath
    private String ThirdReadModeXpath = "";
    //阅读模式网页编码Charset
    private String ThirdReadModeCharset = "";  //1.utf-8 2.gbk
    //Third 阅读模式图片拼接 ReadModeImgSrc  一般为图片src 前面加上域名
    private String ThirdReadModeImgSrc = "";
    //****************************************************三级 Third 结束
    @Generated(hash = 73363793)
    public HomeEntity(Long id, String grouping, String title, String type, String desc, String date, @NotNull String Firsturl,
            String FirstCharset, String FirstAjax, String Firstrooturl, String FirstRequestMethod, String FirstPostData,
            String FirstQueryIsURLencode, String FirstHeaders, String FirstListXpath, String FirstNextPageXpath,
            String FirstNextProcessingValue, String FirstTitleXpath, String FirstTitleProcessingValue, String FirstSummaryXpath,
            String FirstCoverXpath, String FirstCoverProcessingValue, String FirstLinkXpath, String FirstLinkProcessingValue,
            String FirstDateXpath, String FirstViewMode, String FirstLinkType, String FirstReadModeXpath, String FirstReadModeCharset,
            String FirstReadModeImgSrc, String Secondurl, String SecondCharset, String SecondAjax, String SecondRequestMethod,
            String SecondPostData, String SecondHeaders, String SecondListXpath, String SecondNextPageXpath, String SecondNextProcessingValue,
            String SecondTitleXpath, String SecondTitleProcessingValue, String SecondSummaryXpath, String SecondCoverXpath,
            String SecondCoverProcessingValue, String SecondLinkXpath, String SecondLinkProcessingValue, String SecondDateXpath,
            String SecondViewMode, String SecondLinkType, String SecondReadModeXpath, String SecondReadModeCharset, String SecondReadModeImgSrc,
            String Thirdurl, String ThirdCharset, String ThirdAjax, String ThirdRequestMethod, String ThirdPostData, String ThirdHeaders,
            String ThirdListXpath, String ThirdNextPageXpath, String ThirdNextProcessingValue, String ThirdTitleXpath,
            String ThirdTitleProcessingValue, String ThirdSummaryXpath, String ThirdCoverXpath, String ThirdCoverProcessingValue,
            String ThirdLinkXpath, String ThirdLinkProcessingValue, String ThirdDateXpath, String ThirdViewMode, String ThirdLinkType,
            String ThirdReadModeXpath, String ThirdReadModeCharset, String ThirdReadModeImgSrc) {
        this.id = id;
        this.grouping = grouping;
        this.title = title;
        this.type = type;
        this.desc = desc;
        this.date = date;
        this.Firsturl = Firsturl;
        this.FirstCharset = FirstCharset;
        this.FirstAjax = FirstAjax;
        this.Firstrooturl = Firstrooturl;
        this.FirstRequestMethod = FirstRequestMethod;
        this.FirstPostData = FirstPostData;
        this.FirstQueryIsURLencode = FirstQueryIsURLencode;
        this.FirstHeaders = FirstHeaders;
        this.FirstListXpath = FirstListXpath;
        this.FirstNextPageXpath = FirstNextPageXpath;
        this.FirstNextProcessingValue = FirstNextProcessingValue;
        this.FirstTitleXpath = FirstTitleXpath;
        this.FirstTitleProcessingValue = FirstTitleProcessingValue;
        this.FirstSummaryXpath = FirstSummaryXpath;
        this.FirstCoverXpath = FirstCoverXpath;
        this.FirstCoverProcessingValue = FirstCoverProcessingValue;
        this.FirstLinkXpath = FirstLinkXpath;
        this.FirstLinkProcessingValue = FirstLinkProcessingValue;
        this.FirstDateXpath = FirstDateXpath;
        this.FirstViewMode = FirstViewMode;
        this.FirstLinkType = FirstLinkType;
        this.FirstReadModeXpath = FirstReadModeXpath;
        this.FirstReadModeCharset = FirstReadModeCharset;
        this.FirstReadModeImgSrc = FirstReadModeImgSrc;
        this.Secondurl = Secondurl;
        this.SecondCharset = SecondCharset;
        this.SecondAjax = SecondAjax;
        this.SecondRequestMethod = SecondRequestMethod;
        this.SecondPostData = SecondPostData;
        this.SecondHeaders = SecondHeaders;
        this.SecondListXpath = SecondListXpath;
        this.SecondNextPageXpath = SecondNextPageXpath;
        this.SecondNextProcessingValue = SecondNextProcessingValue;
        this.SecondTitleXpath = SecondTitleXpath;
        this.SecondTitleProcessingValue = SecondTitleProcessingValue;
        this.SecondSummaryXpath = SecondSummaryXpath;
        this.SecondCoverXpath = SecondCoverXpath;
        this.SecondCoverProcessingValue = SecondCoverProcessingValue;
        this.SecondLinkXpath = SecondLinkXpath;
        this.SecondLinkProcessingValue = SecondLinkProcessingValue;
        this.SecondDateXpath = SecondDateXpath;
        this.SecondViewMode = SecondViewMode;
        this.SecondLinkType = SecondLinkType;
        this.SecondReadModeXpath = SecondReadModeXpath;
        this.SecondReadModeCharset = SecondReadModeCharset;
        this.SecondReadModeImgSrc = SecondReadModeImgSrc;
        this.Thirdurl = Thirdurl;
        this.ThirdCharset = ThirdCharset;
        this.ThirdAjax = ThirdAjax;
        this.ThirdRequestMethod = ThirdRequestMethod;
        this.ThirdPostData = ThirdPostData;
        this.ThirdHeaders = ThirdHeaders;
        this.ThirdListXpath = ThirdListXpath;
        this.ThirdNextPageXpath = ThirdNextPageXpath;
        this.ThirdNextProcessingValue = ThirdNextProcessingValue;
        this.ThirdTitleXpath = ThirdTitleXpath;
        this.ThirdTitleProcessingValue = ThirdTitleProcessingValue;
        this.ThirdSummaryXpath = ThirdSummaryXpath;
        this.ThirdCoverXpath = ThirdCoverXpath;
        this.ThirdCoverProcessingValue = ThirdCoverProcessingValue;
        this.ThirdLinkXpath = ThirdLinkXpath;
        this.ThirdLinkProcessingValue = ThirdLinkProcessingValue;
        this.ThirdDateXpath = ThirdDateXpath;
        this.ThirdViewMode = ThirdViewMode;
        this.ThirdLinkType = ThirdLinkType;
        this.ThirdReadModeXpath = ThirdReadModeXpath;
        this.ThirdReadModeCharset = ThirdReadModeCharset;
        this.ThirdReadModeImgSrc = ThirdReadModeImgSrc;
    }
    @Generated(hash = 1364639484)
    public HomeEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getGrouping() {
        return this.grouping;
    }
    public void setGrouping(String grouping) {
        this.grouping = grouping;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getFirsturl() {
        return this.Firsturl;
    }
    public void setFirsturl(String Firsturl) {
        this.Firsturl = Firsturl;
    }
    public String getFirstCharset() {
        return this.FirstCharset;
    }
    public void setFirstCharset(String FirstCharset) {
        this.FirstCharset = FirstCharset;
    }
    public String getFirstAjax() {
        return this.FirstAjax;
    }
    public void setFirstAjax(String FirstAjax) {
        this.FirstAjax = FirstAjax;
    }
    public String getFirstrooturl() {
        return this.Firstrooturl;
    }
    public void setFirstrooturl(String Firstrooturl) {
        this.Firstrooturl = Firstrooturl;
    }
    public String getFirstRequestMethod() {
        return this.FirstRequestMethod;
    }
    public void setFirstRequestMethod(String FirstRequestMethod) {
        this.FirstRequestMethod = FirstRequestMethod;
    }
    public String getFirstPostData() {
        return this.FirstPostData;
    }
    public void setFirstPostData(String FirstPostData) {
        this.FirstPostData = FirstPostData;
    }
    public String getFirstQueryIsURLencode() {
        return this.FirstQueryIsURLencode;
    }
    public void setFirstQueryIsURLencode(String FirstQueryIsURLencode) {
        this.FirstQueryIsURLencode = FirstQueryIsURLencode;
    }
    public String getFirstHeaders() {
        return this.FirstHeaders;
    }
    public void setFirstHeaders(String FirstHeaders) {
        this.FirstHeaders = FirstHeaders;
    }
    public String getFirstListXpath() {
        return this.FirstListXpath;
    }
    public void setFirstListXpath(String FirstListXpath) {
        this.FirstListXpath = FirstListXpath;
    }
    public String getFirstNextPageXpath() {
        return this.FirstNextPageXpath;
    }
    public void setFirstNextPageXpath(String FirstNextPageXpath) {
        this.FirstNextPageXpath = FirstNextPageXpath;
    }
    public String getFirstNextProcessingValue() {
        return this.FirstNextProcessingValue;
    }
    public void setFirstNextProcessingValue(String FirstNextProcessingValue) {
        this.FirstNextProcessingValue = FirstNextProcessingValue;
    }
    public String getFirstTitleXpath() {
        return this.FirstTitleXpath;
    }
    public void setFirstTitleXpath(String FirstTitleXpath) {
        this.FirstTitleXpath = FirstTitleXpath;
    }
    public String getFirstTitleProcessingValue() {
        return this.FirstTitleProcessingValue;
    }
    public void setFirstTitleProcessingValue(String FirstTitleProcessingValue) {
        this.FirstTitleProcessingValue = FirstTitleProcessingValue;
    }
    public String getFirstSummaryXpath() {
        return this.FirstSummaryXpath;
    }
    public void setFirstSummaryXpath(String FirstSummaryXpath) {
        this.FirstSummaryXpath = FirstSummaryXpath;
    }
    public String getFirstCoverXpath() {
        return this.FirstCoverXpath;
    }
    public void setFirstCoverXpath(String FirstCoverXpath) {
        this.FirstCoverXpath = FirstCoverXpath;
    }
    public String getFirstCoverProcessingValue() {
        return this.FirstCoverProcessingValue;
    }
    public void setFirstCoverProcessingValue(String FirstCoverProcessingValue) {
        this.FirstCoverProcessingValue = FirstCoverProcessingValue;
    }
    public String getFirstLinkXpath() {
        return this.FirstLinkXpath;
    }
    public void setFirstLinkXpath(String FirstLinkXpath) {
        this.FirstLinkXpath = FirstLinkXpath;
    }
    public String getFirstLinkProcessingValue() {
        return this.FirstLinkProcessingValue;
    }
    public void setFirstLinkProcessingValue(String FirstLinkProcessingValue) {
        this.FirstLinkProcessingValue = FirstLinkProcessingValue;
    }
    public String getFirstDateXpath() {
        return this.FirstDateXpath;
    }
    public void setFirstDateXpath(String FirstDateXpath) {
        this.FirstDateXpath = FirstDateXpath;
    }
    public String getFirstViewMode() {
        return this.FirstViewMode;
    }
    public void setFirstViewMode(String FirstViewMode) {
        this.FirstViewMode = FirstViewMode;
    }
    public String getFirstLinkType() {
        return this.FirstLinkType;
    }
    public void setFirstLinkType(String FirstLinkType) {
        this.FirstLinkType = FirstLinkType;
    }
    public String getFirstReadModeXpath() {
        return this.FirstReadModeXpath;
    }
    public void setFirstReadModeXpath(String FirstReadModeXpath) {
        this.FirstReadModeXpath = FirstReadModeXpath;
    }
    public String getFirstReadModeCharset() {
        return this.FirstReadModeCharset;
    }
    public void setFirstReadModeCharset(String FirstReadModeCharset) {
        this.FirstReadModeCharset = FirstReadModeCharset;
    }
    public String getFirstReadModeImgSrc() {
        return this.FirstReadModeImgSrc;
    }
    public void setFirstReadModeImgSrc(String FirstReadModeImgSrc) {
        this.FirstReadModeImgSrc = FirstReadModeImgSrc;
    }
    public String getSecondurl() {
        return this.Secondurl;
    }
    public void setSecondurl(String Secondurl) {
        this.Secondurl = Secondurl;
    }
    public String getSecondCharset() {
        return this.SecondCharset;
    }
    public void setSecondCharset(String SecondCharset) {
        this.SecondCharset = SecondCharset;
    }
    public String getSecondAjax() {
        return this.SecondAjax;
    }
    public void setSecondAjax(String SecondAjax) {
        this.SecondAjax = SecondAjax;
    }
    public String getSecondRequestMethod() {
        return this.SecondRequestMethod;
    }
    public void setSecondRequestMethod(String SecondRequestMethod) {
        this.SecondRequestMethod = SecondRequestMethod;
    }
    public String getSecondPostData() {
        return this.SecondPostData;
    }
    public void setSecondPostData(String SecondPostData) {
        this.SecondPostData = SecondPostData;
    }
    public String getSecondHeaders() {
        return this.SecondHeaders;
    }
    public void setSecondHeaders(String SecondHeaders) {
        this.SecondHeaders = SecondHeaders;
    }
    public String getSecondListXpath() {
        return this.SecondListXpath;
    }
    public void setSecondListXpath(String SecondListXpath) {
        this.SecondListXpath = SecondListXpath;
    }
    public String getSecondNextPageXpath() {
        return this.SecondNextPageXpath;
    }
    public void setSecondNextPageXpath(String SecondNextPageXpath) {
        this.SecondNextPageXpath = SecondNextPageXpath;
    }
    public String getSecondNextProcessingValue() {
        return this.SecondNextProcessingValue;
    }
    public void setSecondNextProcessingValue(String SecondNextProcessingValue) {
        this.SecondNextProcessingValue = SecondNextProcessingValue;
    }
    public String getSecondTitleXpath() {
        return this.SecondTitleXpath;
    }
    public void setSecondTitleXpath(String SecondTitleXpath) {
        this.SecondTitleXpath = SecondTitleXpath;
    }
    public String getSecondTitleProcessingValue() {
        return this.SecondTitleProcessingValue;
    }
    public void setSecondTitleProcessingValue(String SecondTitleProcessingValue) {
        this.SecondTitleProcessingValue = SecondTitleProcessingValue;
    }
    public String getSecondSummaryXpath() {
        return this.SecondSummaryXpath;
    }
    public void setSecondSummaryXpath(String SecondSummaryXpath) {
        this.SecondSummaryXpath = SecondSummaryXpath;
    }
    public String getSecondCoverXpath() {
        return this.SecondCoverXpath;
    }
    public void setSecondCoverXpath(String SecondCoverXpath) {
        this.SecondCoverXpath = SecondCoverXpath;
    }
    public String getSecondCoverProcessingValue() {
        return this.SecondCoverProcessingValue;
    }
    public void setSecondCoverProcessingValue(String SecondCoverProcessingValue) {
        this.SecondCoverProcessingValue = SecondCoverProcessingValue;
    }
    public String getSecondLinkXpath() {
        return this.SecondLinkXpath;
    }
    public void setSecondLinkXpath(String SecondLinkXpath) {
        this.SecondLinkXpath = SecondLinkXpath;
    }
    public String getSecondLinkProcessingValue() {
        return this.SecondLinkProcessingValue;
    }
    public void setSecondLinkProcessingValue(String SecondLinkProcessingValue) {
        this.SecondLinkProcessingValue = SecondLinkProcessingValue;
    }
    public String getSecondDateXpath() {
        return this.SecondDateXpath;
    }
    public void setSecondDateXpath(String SecondDateXpath) {
        this.SecondDateXpath = SecondDateXpath;
    }
    public String getSecondViewMode() {
        return this.SecondViewMode;
    }
    public void setSecondViewMode(String SecondViewMode) {
        this.SecondViewMode = SecondViewMode;
    }
    public String getSecondLinkType() {
        return this.SecondLinkType;
    }
    public void setSecondLinkType(String SecondLinkType) {
        this.SecondLinkType = SecondLinkType;
    }
    public String getSecondReadModeXpath() {
        return this.SecondReadModeXpath;
    }
    public void setSecondReadModeXpath(String SecondReadModeXpath) {
        this.SecondReadModeXpath = SecondReadModeXpath;
    }
    public String getSecondReadModeCharset() {
        return this.SecondReadModeCharset;
    }
    public void setSecondReadModeCharset(String SecondReadModeCharset) {
        this.SecondReadModeCharset = SecondReadModeCharset;
    }
    public String getSecondReadModeImgSrc() {
        return this.SecondReadModeImgSrc;
    }
    public void setSecondReadModeImgSrc(String SecondReadModeImgSrc) {
        this.SecondReadModeImgSrc = SecondReadModeImgSrc;
    }
    public String getThirdurl() {
        return this.Thirdurl;
    }
    public void setThirdurl(String Thirdurl) {
        this.Thirdurl = Thirdurl;
    }
    public String getThirdCharset() {
        return this.ThirdCharset;
    }
    public void setThirdCharset(String ThirdCharset) {
        this.ThirdCharset = ThirdCharset;
    }
    public String getThirdAjax() {
        return this.ThirdAjax;
    }
    public void setThirdAjax(String ThirdAjax) {
        this.ThirdAjax = ThirdAjax;
    }
    public String getThirdRequestMethod() {
        return this.ThirdRequestMethod;
    }
    public void setThirdRequestMethod(String ThirdRequestMethod) {
        this.ThirdRequestMethod = ThirdRequestMethod;
    }
    public String getThirdPostData() {
        return this.ThirdPostData;
    }
    public void setThirdPostData(String ThirdPostData) {
        this.ThirdPostData = ThirdPostData;
    }
    public String getThirdHeaders() {
        return this.ThirdHeaders;
    }
    public void setThirdHeaders(String ThirdHeaders) {
        this.ThirdHeaders = ThirdHeaders;
    }
    public String getThirdListXpath() {
        return this.ThirdListXpath;
    }
    public void setThirdListXpath(String ThirdListXpath) {
        this.ThirdListXpath = ThirdListXpath;
    }
    public String getThirdNextPageXpath() {
        return this.ThirdNextPageXpath;
    }
    public void setThirdNextPageXpath(String ThirdNextPageXpath) {
        this.ThirdNextPageXpath = ThirdNextPageXpath;
    }
    public String getThirdNextProcessingValue() {
        return this.ThirdNextProcessingValue;
    }
    public void setThirdNextProcessingValue(String ThirdNextProcessingValue) {
        this.ThirdNextProcessingValue = ThirdNextProcessingValue;
    }
    public String getThirdTitleXpath() {
        return this.ThirdTitleXpath;
    }
    public void setThirdTitleXpath(String ThirdTitleXpath) {
        this.ThirdTitleXpath = ThirdTitleXpath;
    }
    public String getThirdTitleProcessingValue() {
        return this.ThirdTitleProcessingValue;
    }
    public void setThirdTitleProcessingValue(String ThirdTitleProcessingValue) {
        this.ThirdTitleProcessingValue = ThirdTitleProcessingValue;
    }
    public String getThirdSummaryXpath() {
        return this.ThirdSummaryXpath;
    }
    public void setThirdSummaryXpath(String ThirdSummaryXpath) {
        this.ThirdSummaryXpath = ThirdSummaryXpath;
    }
    public String getThirdCoverXpath() {
        return this.ThirdCoverXpath;
    }
    public void setThirdCoverXpath(String ThirdCoverXpath) {
        this.ThirdCoverXpath = ThirdCoverXpath;
    }
    public String getThirdCoverProcessingValue() {
        return this.ThirdCoverProcessingValue;
    }
    public void setThirdCoverProcessingValue(String ThirdCoverProcessingValue) {
        this.ThirdCoverProcessingValue = ThirdCoverProcessingValue;
    }
    public String getThirdLinkXpath() {
        return this.ThirdLinkXpath;
    }
    public void setThirdLinkXpath(String ThirdLinkXpath) {
        this.ThirdLinkXpath = ThirdLinkXpath;
    }
    public String getThirdLinkProcessingValue() {
        return this.ThirdLinkProcessingValue;
    }
    public void setThirdLinkProcessingValue(String ThirdLinkProcessingValue) {
        this.ThirdLinkProcessingValue = ThirdLinkProcessingValue;
    }
    public String getThirdDateXpath() {
        return this.ThirdDateXpath;
    }
    public void setThirdDateXpath(String ThirdDateXpath) {
        this.ThirdDateXpath = ThirdDateXpath;
    }
    public String getThirdViewMode() {
        return this.ThirdViewMode;
    }
    public void setThirdViewMode(String ThirdViewMode) {
        this.ThirdViewMode = ThirdViewMode;
    }
    public String getThirdLinkType() {
        return this.ThirdLinkType;
    }
    public void setThirdLinkType(String ThirdLinkType) {
        this.ThirdLinkType = ThirdLinkType;
    }
    public String getThirdReadModeXpath() {
        return this.ThirdReadModeXpath;
    }
    public void setThirdReadModeXpath(String ThirdReadModeXpath) {
        this.ThirdReadModeXpath = ThirdReadModeXpath;
    }
    public String getThirdReadModeCharset() {
        return this.ThirdReadModeCharset;
    }
    public void setThirdReadModeCharset(String ThirdReadModeCharset) {
        this.ThirdReadModeCharset = ThirdReadModeCharset;
    }
    public String getThirdReadModeImgSrc() {
        return this.ThirdReadModeImgSrc;
    }
    public void setThirdReadModeImgSrc(String ThirdReadModeImgSrc) {
        this.ThirdReadModeImgSrc = ThirdReadModeImgSrc;
    }





}
