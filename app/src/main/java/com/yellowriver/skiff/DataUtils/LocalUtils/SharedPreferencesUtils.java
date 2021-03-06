package com.yellowriver.skiff.DataUtils.LocalUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * SharedPreferencesUtils
 *
 * @author huang
 */
public class SharedPreferencesUtils {

    //修改数据  列表排序
    public static void writeDataSort(String dataSort, Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("datasetting", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();

        if (!"".equals(dataSort)) {
            editor.putString("DataSort", dataSort);
        }

        editor.apply();
    }

    //读取数据  列表排序
    public static String readDataSort(Context mContext) {
        //获取数据正反
        SharedPreferences ReadSettings = mContext.getSharedPreferences("datasetting", 0);


        return ReadSettings.getString("DataSort", "正");
    }


    //读取数据  源管理 源变更
    public static boolean readdataChange(Context mContext) {
        //获取数据正反
        SharedPreferences DataSettings = mContext.getSharedPreferences("dataChange", 0);
        return DataSettings.getBoolean("Change", false);

    }

    //修改数据  源管理 源变更
    public static void dataChange(boolean dataChange, Context mContext) {
        SharedPreferences DataSettings = mContext.getSharedPreferences("dataChange", 0);
        SharedPreferences.Editor editor = DataSettings.edit();
        editor.putBoolean("Change", dataChange);
        editor.apply();
    }

    //读取数据  源管理 源变更 本地源与源市场之间使用
    public static boolean readdataChangeSource(Context mContext) {
        //获取数据正反
        SharedPreferences DataSettings = mContext.getSharedPreferences("dataChangeSource", 0);
        return DataSettings.getBoolean("ChangeSource", false);

    }

    //修改数据  源管理 源变更
    public static void dataChangeSource(boolean dataChange, Context mContext) {
        SharedPreferences DataSettings = mContext.getSharedPreferences("dataChangeSource", 0);
        SharedPreferences.Editor editor = DataSettings.edit();
        editor.putBoolean("ChangeSource", dataChange);
        editor.apply();
    }

    //源管理 是否重新加载
    public static boolean readSourceReload(Context mContext) {
        //获取数据正反
        SharedPreferences DataSettings = mContext.getSharedPreferences("sourceReload", 0);
        return DataSettings.getBoolean("sourcesettingReload", false);

    }

    //源管理 是否重新加载
    public static void writeSourceReload(boolean dataChange, Context mContext) {
        SharedPreferences DataSettings = mContext.getSharedPreferences("sourceReload", 0);
        SharedPreferences.Editor editor = DataSettings.edit();
        editor.putBoolean("sourcesettingReload", dataChange);
        editor.apply();
    }


    //读取数据  添加收藏
    public static boolean readFavoriteChange(Context mContext) {

        SharedPreferences DataSettings = mContext.getSharedPreferences("favoriteChange", 0);

        return DataSettings.getBoolean("favoriteChange", false);

    }

    //修改数据  收藏 变更
    public static void FavoriteChange(boolean dataChange, Context mContext) {
        SharedPreferences DataSettings = mContext.getSharedPreferences("favoriteChange", 0);
        SharedPreferences.Editor editor = DataSettings.edit();


        editor.putBoolean("favoriteChange", dataChange);


        editor.apply();
    }


    public static void fontSizeWrite(Context mContext, int fontSize) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (fontSize != 0) {
            editor.putInt("FontSzie", fontSize);
        }
        editor.apply();
    }

    public static void fontLineheightWrite(Context mContext, float lineheight) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (lineheight != 0) {
            editor.putFloat("Lineheight", lineheight);
        }
        editor.apply();
    }

    public static void fontLetterspaWrite(Context mContext, int letterspa) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (letterspa != 0) {
            editor.putInt("Letterspa", letterspa);
        }
        editor.apply();
    }

    public static void fontMarginWrite(Context mContext, int fontMargin) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (fontMargin != 0) {
            editor.putInt("FontMargin", fontMargin);
        }
        editor.apply();
    }

    public static void bottomMarginWrite(Context mContext, int bottomMargin) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (bottomMargin != 0) {
            editor.putInt("BottomMargin", bottomMargin);
        }
        editor.apply();
    }

    public static void fontColorWrite(Context mContext, int fontColor) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();

        if (fontColor != 0) {
            editor.putInt("FontColor", fontColor);
        }

        editor.apply();
    }

    public static void bgColorWrite(Context mContext, int bgColor) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();

        if (bgColor != 0) {
            editor.putInt("BGColor", bgColor);
        }
        editor.apply();
    }


    public static int fontSizeRead(Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        return ReadSettings.getInt("FontSzie", 18);
    }

    public static float fontLineheightRead(Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        float s = 1;
        try {
             s = (float) ReadSettings.getFloat("Lineheight", (float) 1);
        }catch (ClassCastException e){

        }
        return s;
    }

    public static int fontLetterspaRead(Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        return ReadSettings.getInt("Letterspa", 5);
    }

    public static int fontMarginRead(Context mContext) {

        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        return ReadSettings.getInt("FontMargin", 10);
    }

    public static int bottomMarginRead(Context mContext) {

        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        return ReadSettings.getInt("BottomMargin", 10);
    }


    public static int fontColorRead(Context mContext) {
        //获取数据正反
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        int mrFontColor = Color.parseColor("#000000");


        return ReadSettings.getInt("FontColor", mrFontColor);
    }

    public static int bgColorRead(Context mContext) {
        //获取数据正反
        SharedPreferences ReadSettings = mContext.getSharedPreferences("readsetting", 0);
        int mrBGColor = Color.parseColor("#A6B7A6");


        return ReadSettings.getInt("BGColor", mrBGColor);
    }

    //主题保存
    public static String themeRead(Context mContext) {

        SharedPreferences ReadSettings = mContext.getSharedPreferences("theme", 0);


        return ReadSettings.getString("themeColor", "默认蓝");
    }

    //主题读取
    public static void themeWrite(Context mContext, String themeColor) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("theme", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();

        if (themeColor != null) {
            editor.putString("themeColor", themeColor);
        }
        editor.apply();
    }

    //列表动画保存
    public static String listLoadAnimationRead(Context mContext) {

        SharedPreferences ReadSettings = mContext.getSharedPreferences("LoadAnimation", 0);


        return ReadSettings.getString("LoadAnimation", "默认无");
    }

    //列表动画读取
    public static void listLoadAnimationWrite(Context mContext, String LoadAnimation) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("LoadAnimation", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();

        if (LoadAnimation != null) {
            editor.putString("LoadAnimation", LoadAnimation);
        }
        editor.apply();
    }


    //阅读模式保存
    public static int readmodeRead(Context mContext) {

        SharedPreferences ReadSettings = mContext.getSharedPreferences("ReadMode", 0);


        return ReadSettings.getInt("ReadMode", 0);
    }

    //列表动画读取
    public static void readmodeWrite(Context mContext, int readMode) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("ReadMode", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();


        editor.putInt("ReadMode", readMode);

        editor.apply();
    }


    //搜索显示方式保存
    public static String searchViewTypeRead(Context mContext) {

        SharedPreferences ReadSettings = mContext.getSharedPreferences("SearchViewType", 0);


        return ReadSettings.getString("ViewType", "聚合搜索");
    }

    //搜索显示方式读取
    public static void searchViewTypeWrite(Context mContext, String SearchViewType) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("SearchViewType", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();

        if (SearchViewType != null) {
            editor.putString("ViewType", SearchViewType);
        }
        editor.apply();
    }

    //搜索线程大小保存
    public static int searchSumRead(Context mContext) {

        SharedPreferences ReadSettings = mContext.getSharedPreferences("SearchSum", 0);

        return ReadSettings.getInt("SearchSum", 3);
    }

    //搜索显示方式读取
    public static void searchSumWrite(Context mContext, int SearchSum) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("SearchSum", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();

        if (SearchSum != 0) {
            editor.putInt("SearchSum", SearchSum);
        }
        editor.apply();
    }


    //搜索方式保存
    public static String searchTypeRead(Context mContext) {
        SharedPreferences ReadSettings = null;
        try {
            ReadSettings = mContext.getSharedPreferences("SearchType", 0);


        } catch (NullPointerException e) {

        }
        try {
            return ReadSettings.getString("SearchType", "普通模式");
        }catch (NullPointerException e){
            return "普通模式";
        }
    }

    //搜索方式读取
    public static void searchTypeWrite(Context mContext, String SearchType) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("SearchType", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();

        if (SearchType != null) {
            editor.putString("SearchType", SearchType);
        }
        editor.apply();
    }

    //图片加载库使用
    public static String imgLoadRead(Context mContext) {
        SharedPreferences ReadSettings = null;
        try {
            ReadSettings = mContext.getSharedPreferences("ImgLoad", 0);


        } catch (NullPointerException e) {

        }
        try {
            return ReadSettings.getString("ImgLoad", "普通模式");
        }catch (NullPointerException e){
            return "普通模式";
        }
    }

    //搜索方式读取
    public static void imgLoideWrite(Context mContext, String imgLoad) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("ImgLoad", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();

        if (imgLoad != null) {
            editor.putString("ImgLoad", imgLoad);
        }
        editor.apply();
    }

}
