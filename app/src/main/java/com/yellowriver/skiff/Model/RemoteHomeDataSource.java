package com.yellowriver.skiff.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Interface.HomeDataInterface;
import com.yellowriver.skiff.DataUtils.RemoteUtils.AnalysisUtils;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 主要数据加载远程
 * @author huang
 */
public class RemoteHomeDataSource implements HomeDataInterface {

    private static final RemoteHomeDataSource INSTANCE = new RemoteHomeDataSource();

    private RemoteHomeDataSource() {
    }

    public static RemoteHomeDataSource getInstance() {
        return INSTANCE;
    }


    @Override
    public LiveData<Vector<DataEntity>> getData(HomeEntity homeEntity, String step, String url, String query , int page) {
        final MutableLiveData<Vector<DataEntity>> data = new MutableLiveData<>();

         ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Vector<DataEntity> dataEntities = AnalysisUtils.getInstance().MainAnalysis(homeEntity,step,url,query,page);
                Log.d("dddd", "run: "+dataEntities.size());

                data.postValue(dataEntities);

            }
        };
        singleThreadPool.execute(runnable);
        return data;
    }
}
