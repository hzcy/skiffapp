package com.yellowriver.skiff.Adapter.RecyclerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
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
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.R;

/**
 * 主适配器
 * @author huang
 */
public class HomeAdapter extends BaseQuickAdapter<DataEntity, BaseViewHolder> {

    public HomeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void convert(BaseViewHolder helper, DataEntity item) {
        helper.setText(R.id.tv_title, item.getTitle());
        if (item.getSummary() == null&&item.getDate() == null&&item.getCover() == null)
        {
            helper.getView(R.id.searchgocd).setElevation(3);
        }
        if (item.getSummary() == null) {
            helper.getView(R.id.tv_summary).setVisibility(View.GONE);

        }else
        {
            if (item.getSummary().equals(""))
            {
                helper.getView(R.id.tv_summary).setVisibility(View.GONE);
            }else {
                helper.getView(R.id.tv_summary).setVisibility(View.VISIBLE);
               String summary =  Html.fromHtml(item.getSummary()).toString();
               summary = summary.replaceAll("￼","");
                helper.setText(R.id.tv_summary, summary);


            }
        }

        //如果没有日期  就隐藏日期的view
        if (item.getDate() == null) {
            helper.getView(R.id.tv_date).setVisibility(View.GONE);

        }else
        {
            if (item.getDate().equals(""))
            {
                helper.getView(R.id.tv_summary).setVisibility(View.GONE);
            }else {
                helper.getView(R.id.tv_date).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_date, item.getDate());
            }
        }
        //如果没有图片  就隐藏图片的view

        if (item.getCover() == null ) {
            helper.getView(R.id.iv_cover).setVisibility(View.GONE);
        }else
        {
            if (item.getCover().equals("")|| item.getCover().contains("gif")|| item.getCover().contains("GIF"))
            {
                helper.getView(R.id.iv_cover).setVisibility(View.GONE);
            }else {
                Log.d(TAG, "convert: " + item.getCover());
                helper.getView(R.id.iv_cover).setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(item.getCover())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d(TAG, "onLoadFailed: 加载失败");
                                //helper.getView(R.id.iv_cover).setVisibility(View.GONE);
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
        }
        helper.getView(R.id.iv_favorite).setVisibility(View.GONE);
    }

    /**
     * 去掉图片中的Image
     * @param htmlContent
     * @return
     */
    public static String deleteImageFromHtml( String htmlContent){
        String[] strs = htmlContent.split("<img src");
        for(int i=0;i<strs.length;i++){
            htmlContent = htmlContent.replace(htmlContent.substring(htmlContent.indexOf("<img src"),htmlContent.indexOf(">",htmlContent.indexOf("<img src"))+1),"");
        }
        return htmlContent;

    }
}
