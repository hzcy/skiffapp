package com.yellowriver.skiff.Bean.SourcesBean;


import com.chad.library.adapter.base.entity.node.BaseNode;


import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 源管理
 * @author huang
 */
public class group extends BaseNode {
    //源名称
    private String groupName;
    //源日期
    private String groupDate;
    //源地址
    private String groupLink;
    //源介绍
    private String groupDesc;
    //源是否导入
    private String groupIshave;

    private List<BaseNode> childNode;

    public group(List<BaseNode> childNode,String groupIshave,String groupDesc,String groupLink,String groupDate,String groupName)
    {
        this.groupName = groupName;
        this.childNode = childNode;
        this.groupIshave = groupIshave;
        this.groupDesc = groupDesc;
        this.groupDate = groupDate;
        this.groupLink = groupLink;

    }

    public group(String groupName,String groupLink,List<BaseNode> childNode)
    {
        this.groupName = groupName;
        this.childNode = childNode;
        this.groupIshave = groupIshave;
        this.groupDesc = groupDesc;
        this.groupDate = groupDate;
        this.groupLink = groupLink;

    }


    public void setChildNode(List<BaseNode> childNode) {
        this.childNode = childNode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDate() {
        return groupDate;
    }

    public void setGroupDate(String groupDate) {
        this.groupDate = groupDate;
    }

    public String getGroupLink() {
        return groupLink;
    }

    public void setGroupLink(String groupLink) {
        this.groupLink = groupLink;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }


    public String getGroupIshave() {
        return groupIshave;
    }

    public void setGroupIshave(String groupIshave) {
        this.groupIshave = groupIshave;
    }



    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return childNode;
    }
}
