package com.yellowriver.skiff.Help;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.loadmore.BaseLoadMoreView;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yellowriver.skiff.R;

import org.jetbrains.annotations.NotNull;

/**
 * @author huang
 */
public class CustomLoadMoreView2 extends BaseLoadMoreView {

    @NotNull
    @Override
    public View getLoadComplete(@NotNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.findView(R.id.load_more_loading_view);
    }

    @NotNull
    @Override
    public View getLoadEndView(@NotNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.findView(R.id.load_more_loading_view);
    }

    @NotNull
    @Override
    public View getLoadFailView(@NotNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.findView(R.id.load_more_loading_view);
    }

    @NotNull
    @Override
    public View getLoadingView(@NotNull BaseViewHolder baseViewHolder) {
        return baseViewHolder.findView(R.id.load_more_loading_view);
    }

    @NotNull
    @Override
    public View getRootView(@NotNull ViewGroup viewGroup) {
        // 整个 LoadMore 布局
        return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.load_more, viewGroup, false);
    }
}
