package com.yellowriver.skiff.Bean.HomeBean;

import java.io.Serializable;

/**
 * 获取的数据实体
 * @author huang
 */
public class DataEntity implements Serializable {

    //标题  简介  图片 链接 日期
    private String Title;
    private String Summary;
    private String Cover;
    private String Link;
    private String Date;
    private String color;

    //数据展示类型
    private String ViewMode;
    //链接类型 处理方式
    private String LinkType;

    private String fromSource;
    //状态
    private int state;

    public DataEntity(String Title,String Summary,String Cover,String Link,String Date,String ViewMode,String LinkType)
    {
        this.Title = Title;
        this.Summary = Summary;
        this.Cover = Cover;
        this.Link = Link;
        this.Date = Date;
        this.ViewMode = ViewMode;
        this.LinkType = LinkType;

    }


    public DataEntity(String Title,String Summary,String Cover,String Link,String Date)
    {
        this.Title = Title;
        this.Summary = Summary;
        this.Cover = Cover;
        this.Link = Link;
        this.Date = Date;

    }

    public DataEntity()
    {

    }

    public String getColor() {
        return color;
    }

    public String getFromSource() {
        return fromSource;
    }

    public void setFromSource(String fromSource) {
        this.fromSource = fromSource;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String cover) {
        Cover = cover;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


}
