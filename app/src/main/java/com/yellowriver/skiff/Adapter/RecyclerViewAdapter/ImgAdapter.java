package com.yellowriver.skiff.Adapter.RecyclerViewAdapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
//import com.facebook.drawee.view.SimpleDraweeView;
import com.yellowriver.skiff.Bean.SimpleBean;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
//import com.yellowriver.skiff.Help.WrapContentDraweeView;
import com.yellowriver.skiff.R;

import java.util.List;


/**
 * 阅读适配器
 * @author huang
 */
public class ImgAdapter extends BaseQuickAdapter<SimpleBean, ImgAdapter.MyViewHolder> {

    private static String TAG = "ImgAdapter";

    public ImgAdapter(int layoutResId) {
        super(layoutResId);

    }




    @Override
    protected void convert(MyViewHolder helper, SimpleBean item) {

//
//        Uri uri = Uri.parse(item.getContent());
//        helper.setImageUrl(R.id.iv_cover,uri);



        GlideUrl glideUrl = null;
        if(item.getContent()!=null) {

            if (item.getImageStr() != null) {
                Log.d(TAG, "convert: "+item.getImageStr());
                try {
                    glideUrl = new GlideUrl(item.getContent(), new LazyHeaders.Builder()
                            .addHeader("Referer", item.getImageStr())
                            .build());
                }catch (IllegalArgumentException e){

                }
            } else {

                if(item.getContent().indexOf("{QZ}")!=-1){
                    String[] sourceStrArray2 = item.getContent().split("\\{QZ\\}");
                    if (sourceStrArray2.length == 2) {
                        String imgurl = sourceStrArray2[0];
                        String imgref = sourceStrArray2[1];
                        try {
                            glideUrl = new GlideUrl(imgurl, new LazyHeaders.Builder()
                                    .addHeader("Referer", imgref)
                                    .build());
                        }catch (IllegalArgumentException e){

                        }

                    }else {
                        try {
                            glideUrl = new GlideUrl(item.getContent(), new LazyHeaders.Builder()
                                    .build());
                        }catch (IllegalArgumentException e){

                        }
                    }
                }else {
                    Log.d(TAG, "convert: hear为空");
                    try {
                        glideUrl = new GlideUrl(item.getContent(), new LazyHeaders.Builder()
                                .build());
                    }catch (IllegalArgumentException e){

                    }
                }

            }

            if(glideUrl!=null) {
                Glide.with(getContext())
                        .load(glideUrl)
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
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.loaderr)

                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into((ImageView) helper.getView(R.id.iv_cover));
            }

        }






    }


    public class MyViewHolder extends BaseViewHolder {


        public MyViewHolder(View view) {
            super(view);
        }

//        public BaseViewHolder setImageUrl(@IdRes int viewId, Uri value) {
//            WrapContentDraweeView view = getView(viewId);
//            view.setImageURI(value);
//            return this;
//        }



    }


}
