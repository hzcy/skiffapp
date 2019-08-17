package com.yellowriver.skiff.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntity;
import com.yellowriver.skiff.Model.SQLModel;

import java.util.List;

/**
 * 收藏加载数据ViewModel
 * @author huang
 * @date 2019
 */
public class FavoriteDataViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";
    private MutableLiveData<List<FavoriteEntity>> mDataEntity;
    public LiveData<List<FavoriteEntity>> getDataEntity(String group,boolean isSwipeRefresh) {

        if (mDataEntity == null) {
            mDataEntity = new MutableLiveData<>();
            getData(group);
          }
        else
        {
            if(isSwipeRefresh)
            {
                getData(group);
            }
        }




        return mDataEntity;
    }

    private void setDataEntity(List<FavoriteEntity> mDataEntity) {

        this.mDataEntity.postValue(mDataEntity);

    }

    private void getData(String group) {
        // Do an asyncronous operation to fetch users.



                setDataEntity(SQLModel.getInstance().getFavoritebyGroup(group));



    }
}
