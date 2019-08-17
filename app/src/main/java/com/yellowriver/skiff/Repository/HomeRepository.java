package com.yellowriver.skiff.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Interface.HomeDataInterface;
import com.yellowriver.skiff.Model.RemoteHomeDataSource;

import java.util.Vector;

/**
 * 加载数据
 * @author huang
 */
public class HomeRepository {

    private static final HomeRepository INSTANCE = new HomeRepository();
    private HomeRepository() {
    }
    public static HomeRepository getInstance() {
        return INSTANCE;
    }


    private Context context;
    private static HomeDataInterface remoteProjectDataSource = RemoteHomeDataSource.getInstance();
    //private HomeDataInterface localProjectDataSource = LocalProjectDataSource.getInstance();

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public  LiveData<Vector<DataEntity>> getData(HomeEntity homeEntity, String step, String url, String query , int page) {
        return remoteProjectDataSource.getData(homeEntity,step,url,query,page);
//        if (NetworkUtils.isConnected(context)) {
//            return remoteProjectDataSource.queryProjects(page);
//        } else {
//            return localProjectDataSource.queryProjects(page);
//        }
    }
}
