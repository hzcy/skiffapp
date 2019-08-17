package com.yellowriver.skiff.Interface;

import androidx.lifecycle.LiveData;

import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;

import java.util.Vector;

/**
 * 主加载接口
 * @author huang
 */
public interface HomeDataInterface {
    /**
     * 主要获取数据方法
     * @param homeEntity
     * @param step
     * @param url
     * @param query
     * @param page
     * @return
     */
    LiveData<Vector<DataEntity>> getData(HomeEntity homeEntity, String step, String url, String query , int page);
}
