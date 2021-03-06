package com.yellowriver.skiff.ViewModel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.yellowriver.skiff.Repository.LocalSourceRepository;

import java.util.List;
import java.util.Vector;

/**
 * 获取所有分组ViewModel
 * @author huang
 * @date 2019
 *
 */
public class LocalSourceModel extends ViewModel {
    private static final String TAG = "LocalSourceModel";
    private LocalSourceRepository localSourceRepository = LocalSourceRepository.getInstance();
    private MutableLiveData<Integer> ldPage;;
    private LiveData<List<BaseNode>> localSources;
    public LiveData<List<BaseNode>> getLocalSources() {
        if (null == localSources) {
            ldPage = new MutableLiveData<>();
            localSources = Transformations.switchMap(ldPage, new Function<Integer, LiveData<List<BaseNode>>>() {
                @Override
                public LiveData<List<BaseNode>> apply(Integer page) {

                    return localSourceRepository.getData();
                }
            });
        }
        return localSources;

    }

    public void reload() {
        ldPage.setValue(1);
    }



}
