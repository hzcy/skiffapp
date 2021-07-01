package com.yellowriver.skiff.Adapter.RecyclerViewAdapter;

import android.annotation.SuppressLint;
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
public class SimpAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SimpAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.titletv, item);
    }


}
