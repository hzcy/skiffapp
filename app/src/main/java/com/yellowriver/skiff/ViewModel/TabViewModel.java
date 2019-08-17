package com.yellowriver.skiff.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yellowriver.skiff.Model.SQLModel;

import java.util.List;

/**
 * TabViewModel 弃用
 * @author huang
 * @date 2019
 */
class TabViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    private MutableLiveData<List<String>> tabIndicators;
    public LiveData<List<String>> getSourcesName(String title,String type) {
        if (tabIndicators == null) {
            tabIndicators = new MutableLiveData<>();
            getData(title,type);
        }
        return tabIndicators;
    }

    private void setDataEntity(List<String> tabIndicators) {
        this.tabIndicators.postValue(tabIndicators);

    }

    private void getData(String title,String type) {
        // Do an asyncronous operation to fetch users.
        setDataEntity(SQLModel.getInstance().getTitleByGroup(title, type));
    }


}
