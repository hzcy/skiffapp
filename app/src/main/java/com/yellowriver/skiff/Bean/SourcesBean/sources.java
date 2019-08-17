package com.yellowriver.skiff.Bean.SourcesBean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yellowriver.skiff.Adapter.TreeAdapter.GroupAdapter;

/**
 * @author huang
 */
public class sources implements MultiItemEntity {

    //源名称
    private String sourcesName;
    //源日期
    private String sourcesDate;
    //源地址
    private String sourcesLink;
    //源介绍
    private String sourcesDesc;
    //源是否导入
    private String sourcesIshave;


    public String getSourcesName() {
        return sourcesName;
    }

    public void setSourcesName(String sourcesName) {
        this.sourcesName = sourcesName;
    }

    public String getSourcesDate() {
        return sourcesDate;
    }

    public void setSourcesDate(String sourcesDate) {
        this.sourcesDate = sourcesDate;
    }

    public String getSourcesLink() {
        return sourcesLink;
    }

    public void setSourcesLink(String sourcesLink) {
        this.sourcesLink = sourcesLink;
    }

    public String getSourcesDesc() {
        return sourcesDesc;
    }

    public void setSourcesDesc(String sourcesDesc) {
        this.sourcesDesc = sourcesDesc;
    }

    public String getSourcesIshave() {
        return sourcesIshave;
    }

    public void setSourcesIshave(String sourcesIshave) {
        this.sourcesIshave = sourcesIshave;
    }


    @Override
    public int getItemType() {
        return GroupAdapter.TYPE_LEVEL_1;
    }
}
