package com.yellowriver.skiff.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yellowriver.skiff.Model.SQLModel;

import java.util.List;

/**
 * 获取所有分组ViewModel
 * @author huang
 * @date 2019
 * 暂时弃用
 */
class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    private MutableLiveData<List<String>> groupNameList;
    public LiveData<List<String>> getGroupName() {
        if (groupNameList == null) {
            groupNameList = new MutableLiveData<>();
            getData();
        }
        return groupNameList;
    }

    private void setDataEntity(List<String> groupNameList) {
        this.groupNameList.postValue(groupNameList);
    }

    private void getData() {
        setDataEntity(SQLModel.getInstance().getGroup());
    }


}
