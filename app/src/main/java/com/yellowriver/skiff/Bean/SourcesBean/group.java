package com.yellowriver.skiff.Bean.SourcesBean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yellowriver.skiff.Adapter.TreeAdapter.GroupAdapter;


import java.util.List;

/**
 * 源管理
 * @author huang
 */
public class group extends AbstractExpandableItem<sources>  implements MultiItemEntity {
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

    private List<sources> Sourcess;



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

    public List<sources> getSourcess() {
        return Sourcess;
    }

    public void setSourcess(List<sources> sourcess) {
        Sourcess = sourcess;
    }

    public String getGroupIshave() {
        return groupIshave;
    }

    public void setGroupIshave(String groupIshave) {
        this.groupIshave = groupIshave;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return GroupAdapter.TYPE_LEVEL_0;
    }
}
