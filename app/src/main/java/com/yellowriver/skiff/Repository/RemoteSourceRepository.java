package com.yellowriver.skiff.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yellowriver.skiff.Interface.SourceDataInterface;
import com.yellowriver.skiff.Model.SourceDataSource;

import java.util.List;

public class RemoteSourceRepository {
    private static final RemoteSourceRepository INSTANCE = new RemoteSourceRepository();
    private RemoteSourceRepository() {
    }
    public static RemoteSourceRepository getInstance() {
        return INSTANCE;
    }


    private Context context;
    private static SourceDataInterface sourceDataInterface = SourceDataSource.getInstance();
    //private HomeDataInterface localProjectDataSource = LocalProjectDataSource.getInstance();

    public void init(Context context) {
        this.context = context.getApplicationContext();
    }

    public LiveData<List<MultiItemEntity>> getData() {
        return sourceDataInterface.getRemoteSourcesData();

    }
}
