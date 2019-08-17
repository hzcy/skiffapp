package com.yellowriver.skiff.Adapter.RecyclerViewAdapter;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yellowriver.skiff.Bean.SimpleBean;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.Help.FileUtil;
import com.yellowriver.skiff.R;


/**
 * 阅读适配器
 * @author huang
 */
public class ReadAdapter extends BaseQuickAdapter<SimpleBean, ReadAdapter.MyViewHolder> {



    public ReadAdapter(int layoutResId) {
        super(layoutResId);

    }


    @Override
    protected void convert(MyViewHolder helper, SimpleBean item) {


        int fontColor = SharedPreferencesUtils.fontColorRead(mContext);//字体颜色
        int fontSize = SharedPreferencesUtils.fontSizeRead(mContext);//字体大小
        int bgColor = SharedPreferencesUtils.bgColorRead(mContext);//背景颜色
        int lineHeight = SharedPreferencesUtils.fontLineheightRead(mContext); //行高
        int letterspa = SharedPreferencesUtils.fontLetterspaRead(mContext);  //字间距
        int margin = SharedPreferencesUtils.fontMarginRead(mContext); //左右边距

        String fontColorstr = Integer.toHexString(fontColor);
        fontColorstr = fontColorstr.substring(2, fontColorstr.length());
        String CSS_STYLE = "<style>* {font-size:" + fontSize + "px;" +
                "color:#" + fontColorstr + ";" +
                "text-justify:inter-ideograph;" +
                "text-align:justify;" +
                "line-height:1."+lineHeight+"em;" +
                "letter-spacing:"+letterspa+"px;"+
                "margin: auto "+margin+"px;" +
                "} " +
                "</style>";


        helper.setWebView(R.id.webView,CSS_STYLE + item.getContent(),bgColor);




    }


    public class MyViewHolder extends BaseViewHolder {


        public MyViewHolder(View view) {
            super(view);
        }

        public BaseViewHolder setTextSize(@IdRes int viewId, int value) {
            TextView view = getView(viewId);
            view.setTextSize(value);
            return this;
        }

        public BaseViewHolder setMovementMethod(@IdRes int viewId) {
            TextView view = getView(viewId);
            view.setMovementMethod(new ScrollingMovementMethod());
            return this;
        }
        public BaseViewHolder setWebView(@IdRes int viewId, String data,int bgColor) {
            WebView view = getView(viewId);
            view.loadDataWithBaseURL("file:///android_asset/",data,"text/html","utf-8",null);
            view.getSettings().setDefaultTextEncodingName("utf-8");
            view.getSettings().setSupportZoom(false);
            view.getSettings().setBuiltInZoomControls(false);
            view.setBackgroundColor(bgColor);
            view.setHorizontalScrollBarEnabled(false);
            return this;
        }

    }


}
