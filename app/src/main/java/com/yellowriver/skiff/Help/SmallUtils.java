package com.yellowriver.skiff.Help;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Objects;

/**
 * 小工具
 * @author huang
 */
public class SmallUtils {
    private static SmallUtils instance;

    private SmallUtils() {
    }

    public static SmallUtils getInstance() {
        if (instance == null) {
            instance = new SmallUtils();
        }
        return instance;
    }

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    public boolean openBrowser(String targetUrl, Context mContext) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            return false;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri mUri = Uri.parse(targetUrl);
        intent.setData(mUri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        return true;
    }

    /**
     * 复制字符串
     *
     * @param context
     * @param text
     */
    public void toCopy(Context context, String text) {

        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        Objects.requireNonNull(mClipboardManager).setPrimaryClip(ClipData.newPlainText(null, text));

    }
}