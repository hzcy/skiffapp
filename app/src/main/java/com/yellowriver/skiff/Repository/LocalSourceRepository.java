package com.yellowriver.skiff.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Interface.HomeDataInterface;
import com.yellowriver.skiff.Interface.SourceDataInterface;
import com.yellowriver.skiff.Model.RemoteHomeDataSource;
import com.yellowriver.skiff.Model.SourceDataSource;

import java.util.List;
import java.util.Vector;

public class LocalSourceRepository {
    private static final LocalSourceRepository INSTANCE = new LocalSourceRepository();
    private LocalSourceRepository() {
    }
    public static LocalSourceRepository getInstance() {
        return INSTANCE;
    }


    private Context context;
    private static SourceDataInterface sourceDataInterface = SourceDataSource.getInstance();
    //private HomeDataInterface localProjectDataSource = LocalProjectDataSource.getInstance();

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public LiveData<List<BaseNode>> getData() {
        return sourceDataInterface.getLocalSourcesData();

    }
}
