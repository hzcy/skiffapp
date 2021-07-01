package com.yellowriver.skiff.ViewModel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.yellowriver.skiff.Repository.LocalSourceRepository;
import com.yellowriver.skiff.Repository.RemoteSourceRepository;

import java.util.List;

/**
 * 获取所有分组ViewModel
 * @author huang
 * @date 2019
 *
 */
public class RemoteSourceModel extends ViewModel {
    private static final String TAG = "RemoteSourceModel";
    private RemoteSourceRepository remoteSourceRepository = RemoteSourceRepository.getInstance();
    private MutableLiveData<Integer> ldPage;;
    private LiveData<List<BaseNode>> remoteSources;
    public LiveData<List<BaseNode>> getRemoteSources() {
        if (null == remoteSources) {
            ldPage = new MutableLiveData<>();
            remoteSources = Transformations.switchMap(ldPage, new Function<Integer, LiveData<List<BaseNode>>>() {
                @Override
                public LiveData<List<BaseNode>> apply(Integer page) {

                    return remoteSourceRepository.getData();
                }
            });
        }
        return remoteSources;

    }

    public void reload() {
        ldPage.setValue(1);
    }



}
