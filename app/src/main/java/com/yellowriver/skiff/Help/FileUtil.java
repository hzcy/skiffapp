package com.yellowriver.skiff.Help;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class FileUtil {
    public static String getFileContent(Context context, String file) {
        String content = "";
        try {
            // 把数据从文件中读入内存
            InputStream is = context.getResources().getAssets().open(file);
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int i = is.read(buffer, 0, buffer.length);
            while (i > 0) {
                bs.write(buffer, 0, i);
                i = is.read(buffer, 0, buffer.length);
            }
            content = new String(bs.toByteArray(), Charset.forName("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return content;
    }
}
