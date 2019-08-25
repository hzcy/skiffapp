package com.yellowriver.skiff.Interface;

import androidx.lifecycle.LiveData;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;

import java.util.List;
import java.util.Vector;

/**
 * 源数据接口
 * @author huang
 */
public interface SourceDataInterface {

    /**
     * 添加源
     * @param url
     * @return
     */
    boolean addSource(String url);

    LiveData<List<MultiItemEntity>> getLocalSourcesData();

    LiveData<List<MultiItemEntity>> getRemoteSourcesData();
}
