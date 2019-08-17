package com.yellowriver.skiff.Adapter.RecyclerViewAdapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.R;

/**
 * 目录适配器
 * @author huang
 */
public class MenuAdapter extends BaseQuickAdapter<DataEntity, BaseViewHolder> {

    public MenuAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataEntity item) {
        helper.setText(R.id.tv_title, item.getTitle());


    }
}
