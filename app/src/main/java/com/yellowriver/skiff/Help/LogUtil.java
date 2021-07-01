package com.yellowriver.skiff.Help;

import static android.util.Log.e;
import static android.util.Log.i;

public class LogUtil {
    private static boolean debug = true;
    private static int showLength = 3999;
    private static String Debug1 = "轻舟调试";
    private static String Debug2 = "轻舟自动获取图片";
    private static String Debug3 = "轻舟自动获取xpath";
    /**
     * 分段打印出较长log文本
     *
     * @param logContent 打印文本
     * @param tag        打印log的标记
     */
    public static void info(String tag, String logContent) {
        if (!debug) {
            return;
        }
        if(tag.indexOf(Debug1)!=-1)
        {
            //return;
        }
        if(tag.indexOf(Debug2)!=-1)
        {
            return;
        }
        if(tag.indexOf(Debug3)!=-1)
        {
            //return;
        }
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            i(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                info(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                i(tag, printLog);
            }
        } else {
            e(tag, logContent);
        }
    }
}
