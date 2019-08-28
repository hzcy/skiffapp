package com.yellowriver.skiff.ViewUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;
import com.yellowriver.skiff.DataUtils.RemoteUtils.AnalysisUtils;
import com.yellowriver.skiff.View.Activity.NextActivity;
import com.yellowriver.skiff.View.Activity.ReadActivity;
import com.yellowriver.skiff.View.Activity.SearchActivity;
import com.yellowriver.skiff.View.Activity.WebViewActivity;

/**
 * 点击事件
 *
 * @author huang
 * @date 2019
 */
public class MainViewClick {
    private static final String TAG = "MainViewClick";
    private static final String HTTP = "http";
    private static final String FENGE = "||";
    /**
     * 主解析 根据源点击类型打开不同界面
     *
     * @param mContext    上下文 用于intent
     * @param nowRuleBean 源
     * @param mDataEntity 点击的数据
     * @param index       分组下标  打开搜索  搜索界面用该值对下拉框初始化
     */
    public static boolean OnClick(Context mContext, NowRuleBean nowRuleBean, DataEntity mDataEntity, int index, String upTitle,int readIndex) {
        Boolean result = true;
        switch (nowRuleBean.getLinkType()) {
            //继续解析
            case "1":
                Log.d(TAG, "OnClick: "+mDataEntity.getLink());
                if (null != mDataEntity.getLink()) {
                    if (mDataEntity.getLink().startsWith(HTTP)) {
                        result = true;
                        goNext(mContext, mDataEntity, nowRuleBean, index,readIndex);
                    } else {
                        result = false;
                    }
                } else {
                    result = false;
                }
                break;
            //网页
            case "2":
                Log.d(TAG, "OnClick: ");
                if (null != mDataEntity.getLink()) {
                    if (mDataEntity.getLink().startsWith(HTTP)) {
                        result = true;
                        openWebView(mContext, mDataEntity, nowRuleBean, false, upTitle);
                    } else {
                        result = false;
                    }
                } else {
                    result = false;
                }
                break;
            //阅读模式
            case "3":
                if (null != mDataEntity.getLink()) {
                    if (mDataEntity.getLink().startsWith(HTTP)) {
                        result = true;
                        openReadMode(mContext, mDataEntity, nowRuleBean, upTitle,readIndex);
                    } else {
                        result = false;
                    }
                }
                break;
            //视频
            case "4":
                if (null != mDataEntity.getLink()) {
                    if (mDataEntity.getLink().startsWith(HTTP)) {
                        result = true;
                        openVideoMode(mContext, mDataEntity);
                    } else {
                        result = false;
                    }
                }
                break;
            //文件
            case "5":
                if (null != mDataEntity.getLink()) {
                    result = false;

//                    if (mDataEntity.getLink().startsWith(HTTP)) {
//                        result = true;
//                        openFileMode(mContext, mDataEntity, nowRuleBean.getReadXpath());
//                    }else {
//                        result = false;
//                    }
                }
                break;
            //音频
            case "6":
                result = false;
                break;
            //图片
            case "7":
                result = false;
                break;
            //搜索
            case "8":
                openSearchMode(mContext, mDataEntity,nowRuleBean);
                break;
            //rss
            case "9":
                if (null != mDataEntity.getLink()) {
                    if (mDataEntity.getLink().startsWith(HTTP)) {
                        openRSS(mContext, mDataEntity,nowRuleBean, upTitle);
                    }else {
                        result = false;
                    }
                }
                break;
            default:
                break;
        }
        return result;

    }

    /**
     * 继续解析
     */
    public static void goNext(Context mContext, DataEntity mDataEntity, NowRuleBean nowRuleBean, int index,int readIndex) {
        Intent intent;
        intent = new Intent(mContext, NextActivity.class);
        //点击的分组名
        intent.putExtra("qzGroupName", nowRuleBean.getQzGroupName());
        //点击的标题
        intent.putExtra("qzTitle", mDataEntity.getTitle());
        //源的标题 根据源标题查询数据库
        intent.putExtra("qzSourceName", nowRuleBean.getQzSourcesName());
        intent.putExtra("qzindex", index);
        intent.putExtra("readIndex", readIndex);
        //需要继续解析的链接
        intent.putExtra("qzLink", mDataEntity.getLink());
        //链接类型
        intent.putExtra("qzLinktype", mDataEntity.getLinkType());
        //第几步
        intent.putExtra("qzStep", nowRuleBean.getQzStep());
        //首页还是搜索
        intent.putExtra("qzSoucesType", nowRuleBean.getQzSoucesType());
        //搜索关键字
        intent.putExtra("qzQuery", nowRuleBean.getQuery());
        intent.putExtra("qzCover", mDataEntity.getCover());
        intent.putExtra("qzSummary", mDataEntity.getSummary());
        intent.putExtra("qzDate", mDataEntity.getDate());
        mContext.startActivity(intent);

    }

    //打开浏览器
    public static void openWebView(Context mContext, DataEntity mDataEntity, NowRuleBean nowRuleBean, boolean isRss, String upTitle) {
        Intent intent;
        intent = new Intent(mContext, WebViewActivity.class);
        //点击的标题
        intent.putExtra("qzLink", mDataEntity.getLink());
        intent.putExtra("qzReadHost", nowRuleBean.getReadImgSrc());
        Log.d(TAG, "openWebView: "+nowRuleBean.getReadImgSrc());
        intent.putExtra("qzTitle", mDataEntity.getTitle());
        intent.putExtra("qzReadXpath", nowRuleBean.getReadXpath());
        intent.putExtra("qzCharset", nowRuleBean.getCharset());
        intent.putExtra("qzUpTitle", upTitle);
        if (isRss) {
            intent.putExtra("qzContent", mDataEntity.getSummary());
        }
        mContext.startActivity(intent);
    }

    //打开阅读模式
    public static void openReadMode(Context mContext, DataEntity mDataEntity, NowRuleBean nowRuleBean, String upTitle,int readIndex) {
        Intent intent;
        intent = new Intent(mContext, ReadActivity.class);
        //点击的标题
        intent.putExtra("qzLink", mDataEntity.getLink());
        intent.putExtra("qzReadHost", nowRuleBean.getReadImgSrc());
        intent.putExtra("readIndex", readIndex);
        intent.putExtra("qzSourcesName",nowRuleBean.getQzSourcesName());
        intent.putExtra("qzTitle", mDataEntity.getTitle());
        intent.putExtra("qzReadXpath", nowRuleBean.getReadXpath());
        intent.putExtra("qzCharset", nowRuleBean.getCharset());
        intent.putExtra("qzUpTitle", upTitle);
        intent.putExtra("qzContent", mDataEntity.getSummary());
        mContext.startActivity(intent);
    }

    public static void openRSS(Context mContext, DataEntity mDataEntity, NowRuleBean nowRuleBean, String upTitle) {
        openWebView(mContext, mDataEntity, nowRuleBean, true, upTitle);
    }


    //打开视频播放器
    public static void openVideoMode(Context mContext, DataEntity mDataEntity) {
        String url = mDataEntity.getLink();
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
        mediaIntent.setDataAndType(Uri.parse(url), mimeType);
        mContext.startActivity(mediaIntent);
    }

    //打开文件
    public static void openFileMode(Context mContext, DataEntity mDataEntity, String readXpath) {
        ProgressDialog waitingDialog =
                new ProgressDialog(mContext);
        waitingDialog.setTitle("提示");
        waitingDialog.setMessage("加载中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        //循环从readxpath从解析指定连接 最后使用浏览器打开 或者调用系统工具
        if (readXpath.contains("||")) {
            String[] sourceStrArray = readXpath.split(FENGE);
            if (sourceStrArray.length > 0) {
                final String[] newlink = {null};
                for (int i = 0; i < sourceStrArray.length; i++) {
                    String rex = sourceStrArray[i];
                    if (i == 0) {
                        String finalLink = mDataEntity.getLink();
                        new Thread(() -> newlink[0] = AnalysisUtils.getLink(finalLink, rex)).start();
                    } else {
                        new Thread(() -> newlink[0] = AnalysisUtils.getLink(newlink[0], rex)).start();
                    }
                }
                if (newlink[0] != null) {
                    waitingDialog.cancel();
                    Intent intent2 = new Intent(mContext, WebViewActivity.class);
                    //点击的标题
                    intent2.putExtra("link", newlink);

                    mContext.startActivity(intent2);
                }
            }
        } else {
            Log.d(TAG, "onItemClick: 进入文件模式" + mDataEntity.getLink());
            String finalLink1 = mDataEntity.getLink();
            new Thread(() -> {
                String newlink = AnalysisUtils.getLink(finalLink1, readXpath);
                Log.d(TAG, "run: " + newlink);

                if (newlink != null) {
                    waitingDialog.cancel();
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    //点击的标题
                    intent.putExtra("link", newlink);

                    mContext.startActivity(intent);
                }
            }).start();
        }
    }

    //打开搜索
    public static void openSearchMode(Context mContext, DataEntity mDataEntity,NowRuleBean nowRuleBean) {
        Intent intent;
        //使用点击项的标题去搜索
        intent = new Intent(mContext, SearchActivity.class);
        //点击的标题
        intent.putExtra("qzQuery", mDataEntity.getTitle());
        intent.putExtra("qzGroupName",nowRuleBean.getQzGroupName());

        mContext.startActivity(intent);
    }


}
