package com.yellowriver.skiff.ViewModel;


import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Repository.HomeRepository;

import java.util.Vector;

/**
 * 主要源加载数据ViewModel
 * @author huang
 * @date 2019
 */
public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";
    private HomeRepository projectsRepository = HomeRepository.getInstance();
    private MutableLiveData<Integer> ldPage;;
    private LiveData<Vector<DataEntity>> ldProjects;

    public LiveData<Vector<DataEntity>> getProjects(HomeEntity homeEntity, String step, String url, String query , int page) {
        Log.d(TAG , "测试-->getProjects");
        if (null == ldProjects) {
            ldPage = new MutableLiveData<>();
            ldProjects = Transformations.switchMap(ldPage, new Function<Integer, LiveData<Vector<DataEntity>>>() {
                @Override
                public LiveData<Vector<DataEntity>> apply(Integer page) {
                    //Log.d(TAG, "apply: "+projectsRepository.getData(homeEntity,step,url,query,page).getValue().size());
                    return projectsRepository.getData(homeEntity,step,url,query,page);
                }
            });
        }
        return ldProjects;
    }

    public void reload() {
        ldPage.setValue(1);
    }

    public void loadMore(int page) {
        ldPage.setValue(page);
    }


}
