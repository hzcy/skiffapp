package com.yellowriver.skiff.Bean.SourcesBean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseNode;


import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author huang
 */
public class sources extends BaseNode {


    //源名称
    private String sourcesName;
    //源日期
    private String sourcesDate;
    //源类型
    private String sourcesType;
    //源地址
    private String sourcesLink;
    //源介绍
    private String sourcesDesc;
    //源是否导入
    private String sourcesIshave;

    public String getSourcesType() {
        return sourcesType;
    }

    public void setSourcesType(String sourcesType) {
        this.sourcesType = sourcesType;
    }

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




    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
