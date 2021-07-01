package com.yellowriver.skiff.Adapter.TreeAdapter;

import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yellowriver.skiff.Bean.SourcesBean.group;
import com.yellowriver.skiff.R;

import org.jetbrains.annotations.NotNull;

public class RootNodeProvider extends BaseNodeProvider {
    @Override
    public int getItemViewType() {
        return 0;
    }

    @Override
    public int getLayoutId() {
        return  R.layout.vertical_sourcegroup_item;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, BaseNode data) {
        group entity = (group) data;
        helper.setText(R.id.tv_title, entity.getGroupName());
    }
}
