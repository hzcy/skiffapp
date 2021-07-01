package com.yellowriver.skiff.DataUtils.LocalUtils;

import android.content.Context;
import android.content.SharedPreferences;

public class MyImportSharedPreferences {

    //修改数据
    public static void writeTitle(String step, String titlexpath, Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (!"".equals(titlexpath)) {
            switch (step) {
                case "第一步":
                    editor.putString("FirstTitleXpath", titlexpath);
                    break;
                case "第二步":
                    editor.putString("SecondTitleXpath", titlexpath);
                    break;
                case "第三步":
                    editor.putString("ThirdTitleXpath", titlexpath);
                    break;
                default:
                    break;
            }
        }
        editor.apply();
    }

    //读取数据
    public static String readTitle(String step,Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        String title = "";
        switch (step) {
            case "第一步":
                title = ReadSettings.getString("FirstTitleXpath", "");
                break;
            case "第二步":
                title = ReadSettings.getString("SecondTitleXpath", "");
                break;
            case "第三步":
                title = ReadSettings.getString("ThirdTitleXpath", "");
                break;
            default:
                break;
        }
        return title;
    }


    //修改数据
    public static void writeSummary(String step, String xpath, Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (!"".equals(xpath)) {
            switch (step) {
                case "第一步":
                    editor.putString("FirstSummaryXpath", xpath);
                    break;
                case "第二步":
                    editor.putString("SecondSummaryXpath", xpath);
                    break;
                case "第三步":
                    editor.putString("ThirdSummaryXpath", xpath);
                    break;
                default:
                    break;
            }
        }
        editor.apply();
    }

    //读取数据
    public static String readSummary(String step,Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        String value = "";
        switch (step) {
            case "第一步":
                value = ReadSettings.getString("FirstSummaryXpath", "");
                break;
            case "第二步":
                value = ReadSettings.getString("SecondSummaryXpath", "");
                break;
            case "第三步":
                value = ReadSettings.getString("ThirdSummaryXpath", "");
                break;
            default:
                break;
        }
        return value;
    }



    //修改数据
    public static void writeDate(String step, String xpath, Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (!"".equals(xpath)) {
            switch (step) {
                case "第一步":
                    editor.putString("FirstDateXpath", xpath);
                    break;
                case "第二步":
                    editor.putString("SecondDateXpath", xpath);
                    break;
                case "第三步":
                    editor.putString("ThirdDateXpath", xpath);
                    break;
                default:
                    break;
            }
        }
        editor.apply();
    }

    //读取数据
    public static String readDate(String step,Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        String value = "";
        switch (step) {
            case "第一步":
                value = ReadSettings.getString("FirstDateXpath", "");
                break;
            case "第二步":
                value = ReadSettings.getString("SecondDateXpath", "");
                break;
            case "第三步":
                value = ReadSettings.getString("ThirdDateXpath", "");
                break;
            default:
                break;
        }
        return value;
    }


    //修改数据
    public static void writeCover(String step, String xpath, Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (!"".equals(xpath)) {
            switch (step) {
                case "第一步":
                    editor.putString("FirstCoverXpath", xpath);
                    break;
                case "第二步":
                    editor.putString("SecondCoverXpath", xpath);
                    break;
                case "第三步":
                    editor.putString("ThirdCoverXpath", xpath);
                    break;
                default:
                    break;
            }
        }
        editor.apply();
    }

    //读取数据
    public static String readCover(String step,Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        String value = "";
        switch (step) {
            case "第一步":
                value = ReadSettings.getString("FirstCoverXpath", "");
                break;
            case "第二步":
                value = ReadSettings.getString("SecondCoverXpath", "");
                break;
            case "第三步":
                value = ReadSettings.getString("ThirdCoverXpath", "");
                break;
            default:
                break;
        }
        return value;
    }



    //修改数据
    public static void writeLink(String step, String xpath, Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (!"".equals(xpath)) {
            switch (step) {
                case "第一步":
                    editor.putString("FirstLinkXpath", xpath);
                    break;
                case "第二步":
                    editor.putString("SecondLinkXpath", xpath);
                    break;
                case "第三步":
                    editor.putString("ThirdLinkXpath", xpath);
                    break;
                default:
                    break;
            }
        }
        editor.apply();
    }

    //读取数据
    public static String readLink(String step,Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        String value = "";
        switch (step) {
            case "第一步":
                value = ReadSettings.getString("FirstLinkXpath", "");
                break;
            case "第二步":
                value = ReadSettings.getString("SecondLinkXpath", "");
                break;
            case "第三步":
                value = ReadSettings.getString("ThirdLinkXpath", "");
                break;
            default:
                break;
        }
        return value;
    }


    //修改数据
    public static void writeNextPage(String step, String xpath, Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        SharedPreferences.Editor editor = ReadSettings.edit();
        if (!"".equals(xpath)) {
            switch (step) {
                case "第一步":
                    editor.putString("FirstNextPageXpath", xpath);
                    break;
                case "第二步":
                    editor.putString("SecondNextPageXpath", xpath);
                    break;
                case "第三步":
                    editor.putString("ThirdNextPageXpath", xpath);
                    break;
                default:
                    break;
            }
        }
        editor.apply();
    }

    //读取数据
    public static String readNextPage(String step,Context mContext) {
        SharedPreferences ReadSettings = mContext.getSharedPreferences("MyImport", 0);
        String value = "";
        switch (step) {
            case "第一步":
                value = ReadSettings.getString("FirstNextPageXpath", "");
                break;
            case "第二步":
                value = ReadSettings.getString("SecondNextPageXpath", "");
                break;
            case "第三步":
                value = ReadSettings.getString("ThirdNextPageXpath", "");
                break;
            default:
                break;
        }
        return value;
    }

}
