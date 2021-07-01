package com.yellowriver.skiff.Adapter.RecyclerViewAdapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.yellowriver.skiff.Bean.SimpleBean;
import com.yellowriver.skiff.R;

//import com.facebook.drawee.view.SimpleDraweeView;
//import com.yellowriver.skiff.Help.WrapContentDraweeView;


/**
 * 阅读适配器
 * @author huang
 */
public class Img2Adapter extends BaseQuickAdapter<SimpleBean, Img2Adapter.MyViewHolder> {



    public Img2Adapter(int layoutResId) {
        super(layoutResId);

    }




    @Override
    protected void convert(MyViewHolder helper, SimpleBean item) {


        Uri uri = Uri.parse(item.getContent());
     //   helper.setImageUrl(R.id.iv_cover,uri);





    }


    public class MyViewHolder extends BaseViewHolder {


        public MyViewHolder(View view) {
            super(view);
        }

//        public BaseViewHolder setImageUrl(int viewId, Uri value) {
//            WrapContentDraweeView view = getView(viewId);
//            view.setImageURI(value);
//            return this;
//        }



    }


}
