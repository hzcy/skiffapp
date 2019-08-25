package com.yellowriver.skiff.Model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.SourcesBean.group;
import com.yellowriver.skiff.Bean.SourcesBean.sources;
import com.yellowriver.skiff.DataUtils.RemoteUtils.AnalysisUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.JsonUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.NetUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.SourceUtils;
import com.yellowriver.skiff.Interface.SourceDataInterface;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author huang
 */
public class SourceDataSource implements SourceDataInterface {

    private static final SourceDataSource INSTANCE = new SourceDataSource();

    private SourceDataSource() {
    }

    public static SourceDataSource getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean addSource(String url) {
        boolean isAdd = false;
        Gson gson = new Gson();
        try {
            URL url1 = new URL(url);
            url1.openConnection().connect();
            String data = NetUtils.getInstance().getRequest(url, "");
            if (data != null) {
                if (JsonUtils.isJSONValid(data)) {
                    Type type = new TypeToken<HomeEntity>() {
                    }.getType();
                    HomeEntity homeEntity = gson.fromJson(data, type);
                    if (homeEntity != null) {
                        String newtitle = homeEntity.getTitle();
                        String newtype = homeEntity.getType();
                        List<HomeEntity> homeEntityList = SQLModel.getInstance().getXpathbyTitle(newtitle, newtype);
                        if (!homeEntityList.isEmpty()) {
                            isAdd = false;
                        } else {
                            long addresult = SQLModel.getInstance().addSouce(homeEntity);

                            if (addresult >= 0) {
                                isAdd = true;
                            } else {
                                isAdd = false;
                            }
                        }
                    } else {
                        isAdd = false;
                    }
                } else {
                    isAdd = false;
                }
            } else {
                isAdd = false;
            }
        } catch (MalformedURLException e) {
            isAdd = false;
            e.printStackTrace();
        } catch (IOException e) {
            isAdd = false;
            e.printStackTrace();
        }
        return isAdd;
    }

    @Override
    public LiveData<List<MultiItemEntity>> getLocalSourcesData() {
        final MutableLiveData<List<MultiItemEntity>> LocalSources = new MutableLiveData<>();

        List<MultiItemEntity> list = new ArrayList<>();
        List<String> titleslist;
        List<HomeEntity> homeEntities;
        titleslist = SQLModel.getInstance().getGroup();
        for (int i = 0; i < titleslist.size(); i++) {
            group groupBean = new group();
            groupBean.setGroupName(titleslist.get(i));
            homeEntities = SQLModel.getInstance().gethomeEntitiesByGroup(titleslist.get(i));

            //可展开数据相关
            List<sources> sourcesBeans = new ArrayList<>();
            for (int j = 0; j < homeEntities.size(); j++) {
                sources sourcesBean = new sources();
                sourcesBean.setSourcesName(homeEntities.get(j).getTitle());
                if (homeEntities.get(j).getType() != null) {
                    if (!"".equals(homeEntities.get(j).getType())) {
                        sourcesBean.setSourcesType(homeEntities.get(j).getType());
                    }
                }
                sourcesBean.setSourcesDate(homeEntities.get(j).getDate());
                sourcesBeans.add(sourcesBean);
                groupBean.addSubItem(sourcesBean);
            }
            groupBean.setSourcess(sourcesBeans);
            list.add(groupBean);
        }
        LocalSources.postValue(list);

        return LocalSources;
    }

    @Override
    public LiveData<List<MultiItemEntity>> getRemoteSourcesData() {
        final MutableLiveData<List<MultiItemEntity>> remoteSources = new MutableLiveData<>();



        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024));
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<MultiItemEntity> remoteSourcesList = SourceUtils.getSourceAllGroup();


                remoteSources.postValue(remoteSourcesList);

            }
        };
        singleThreadPool.execute(runnable);

        return remoteSources;
    }


}
