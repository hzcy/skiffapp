package com.yellowriver.skiff.Help;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.yellowriver.skiff.R;

/**
 * @author huang
 */
public class CustomLoadMoreView extends LoadMoreView {

    @Override
    public int getLayoutId() {
        return R.layout.load_more;
    }


    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    /**
     * isLoadEndGone()为true，可以返回0
     * isLoadEndGone()为false，不能返回0
     */
    @Override
    protected int getLoadEndViewId() {
        return 0;
    }

}
