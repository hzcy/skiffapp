package com.yellowriver.skiff.Bean.DataBaseBean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 收藏表
 * @author huang
 */
@Entity
public class FavoriteEntity {

    @Id(autoincrement = true)
    private Long id;
    //进行处理
    //分组名称
    private String grouping = "";
    //类型
    private String type;
    //源名称
    private String sourcesName;
    //第几步
    private String step;

    private int spinnerSel;

    //数据显示
    //标题
    private String title = "";
    //简介
    private String Summary;
    //图片
    private String Cover;
    //链接
    private String Link;
    //日期
    private String Date;
    @Generated(hash = 1864024584)
    public FavoriteEntity(Long id, String grouping, String type, String sourcesName,
            String step, int spinnerSel, String title, String Summary, String Cover,
            String Link, String Date) {
        this.id = id;
        this.grouping = grouping;
        this.type = type;
        this.sourcesName = sourcesName;
        this.step = step;
        this.spinnerSel = spinnerSel;
        this.title = title;
        this.Summary = Summary;
        this.Cover = Cover;
        this.Link = Link;
        this.Date = Date;
    }
    @Generated(hash = 1184291067)
    public FavoriteEntity() {
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
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSourcesName() {
        return this.sourcesName;
    }
    public void setSourcesName(String sourcesName) {
        this.sourcesName = sourcesName;
    }
    public String getStep() {
        return this.step;
    }
    public void setStep(String step) {
        this.step = step;
    }
    public int getSpinnerSel() {
        return this.spinnerSel;
    }
    public void setSpinnerSel(int spinnerSel) {
        this.spinnerSel = spinnerSel;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSummary() {
        return this.Summary;
    }
    public void setSummary(String Summary) {
        this.Summary = Summary;
    }
    public String getCover() {
        return this.Cover;
    }
    public void setCover(String Cover) {
        this.Cover = Cover;
    }
    public String getLink() {
        return this.Link;
    }
    public void setLink(String Link) {
        this.Link = Link;
    }
    public String getDate() {
        return this.Date;
    }
    public void setDate(String Date) {
        this.Date = Date;
    }




}
