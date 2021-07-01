package com.yellowriver.skiff.Adapter.RecyclerViewAdapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.R;

import java.util.List;

/**
 * 目录适配器
 * @author huang
 */
public class MenuAdapter extends BaseQuickAdapter<DataEntity, MenuAdapter.MyViewHolder> {
    private static String TAG = "MenuAdapter";
    public MenuAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(MyViewHolder helper, DataEntity item) {
        helper.setText(R.id.tv_title, item.getTitle());
    }



    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {

            if (payloads.get(0).toString().equals("updateok")) {
                Log.d(TAG, "onBindViewHolder2: "+position);


                holder.setText(R.id.tv_title,  ">>>"+getData().get(position).getTitle()+"<<<");


            } else if (payloads.get(0).toString().equals("closeok")){
                holder.setText(R.id.tv_title,  getData().get(position).getTitle());

            }
        }


    }

    public class MyViewHolder extends BaseViewHolder {


        public MyViewHolder(View view) {
            super(view);
        }

        public BaseViewHolder setTexts(@IdRes int viewId, int value) {
            TextView view = getView(viewId);
            //view.set(value);
            return this;
        }

    }
}
