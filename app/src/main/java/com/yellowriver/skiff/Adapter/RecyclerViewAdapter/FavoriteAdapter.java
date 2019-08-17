package com.yellowriver.skiff.Adapter.RecyclerViewAdapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntity;
import com.yellowriver.skiff.R;

/**
 * 收藏
 * @author huang
 */
public class FavoriteAdapter extends BaseQuickAdapter<FavoriteEntity, BaseViewHolder> {

    public FavoriteAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, FavoriteEntity item) {
        helper.setText(R.id.tv_title, item.getTitle());
        if (item.getSummary() == null) {
            //收藏里 没有简介就显示“Date”
            helper.setText(R.id.tv_summary, item.getDate());

        }else
        {
            helper.getView(R.id.tv_summary).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_summary, item.getSummary());
        }

        //收藏的“日期”显示源名称
        helper.setText(R.id.tv_date, item.getSourcesName());

        //如果没有图片  就隐藏图片的view

        if (item.getCover() == null) {
            helper.getView(R.id.iv_cover).setVisibility(View.GONE);
        }else
        {
            helper.getView(R.id.iv_cover).setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(item.getCover())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.d(TAG, "onLoadFailed: 加载失败");
                            helper.getView(R.id.iv_cover).setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d(TAG, "onLoadFailed: 加载成功");
                            helper.getView(R.id.iv_cover).setVisibility(View.VISIBLE);
                            return false;
                        }

                    })
                    //.error(R.drawable.moren_new)
                    //.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into((ImageView) helper.getView(R.id.iv_cover));
        }
        helper.setImageResource(R.id.iv_favorite,R.drawable.ic_close_black_24dp);
        helper.addOnClickListener(R.id.iv_favorite);
    }
}
