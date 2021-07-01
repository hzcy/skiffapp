package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.os.Environment;
import android.util.Log;

import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Help.LogUtil;

import org.seimicrawler.xpath.JXDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextDocument;
import de.l3s.boilerpipe.extractors.CommonExtractors;

import de.l3s.boilerpipe.sax.BoilerpipeSAXInput;
import de.l3s.boilerpipe.sax.HTMLDocument;
import de.l3s.boilerpipe.sax.HTMLFetcher;
import de.l3s.boilerpipe.sax.HTMLHighlighter;


/**
 * 自动删除html无关内容
 * <p>
 * final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
 * //final BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;
 * //final BoilerpipeExtractor extractor = CommonExtractors.KEEP_EVERYTHING_EXTRACTOR;
 * // final BoilerpipeExtractor extractor = CommonExtractors.CANOLA_EXTRACTOR;
 * // final BoilerpipeExtractor extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;
 */
public class BoilerpipeUtils {


    /**
     * @param html
     * @param type "1": 全部高亮
     * @return
     */
    public static String highlightingHtml(String html, String type) {


        BoilerpipeExtractor extractor = null;

        switch (type) {
            case "1":
                //全部高亮
                extractor = CommonExtractors.KEEP_EVERYTHING_EXTRACTOR;
                break;
            case "2":
                //默认
                extractor = CommonExtractors.DEFAULT_EXTRACTOR;
                break;
            case "3":
                //文章类型
                extractor = CommonExtractors.ARTICLE_EXTRACTOR;
                break;
            case "4":
                //
                extractor = CommonExtractors.CANOLA_EXTRACTOR;
                break;
            case "5":
                //最大内容输出
                extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;
                break;
            default:
                //默认 全部高亮
                extractor = CommonExtractors.KEEP_EVERYTHING_EXTRACTOR;
                break;
        }
        //使用html高亮输出模式
        final HTMLHighlighter hh = HTMLHighlighter.newHighlightingInstance();


       // hh.setBodyOnly(bodyonly);


        TextDocument doc = null;
        String finalHtml = null;
        try {

            doc = (new BoilerpipeSAXInput(new InputSource(new StringReader(html)))).getTextDocument();
            extractor.process(doc);

            finalHtml = hh.process(doc, html);
            finalHtml = HTMLDocument.restoreTextEncodedImageTags(finalHtml, "UTF-8");
           // LogUtil.info("轻舟自动", "高亮HTML" + finalHtml);

            File fs = new File(Environment.getExternalStorageDirectory() + "/msc/" + "highlighted1.html");
            FileOutputStream outputStream = new FileOutputStream(fs);
            outputStream.write(finalHtml.getBytes());
            outputStream.flush();
            outputStream.close();

        } catch (BoilerpipeProcessingException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return finalHtml;
    }
























    public static String getFullContent(String html) {

        if (html != null) {
            JXDocument jxDocument = JXDocument.create(html);

            //测试第二步 包含图片 标题 简介 日期 的列表
            try {
                LogUtil.info("轻舟自动", "内容" + jxDocument.sel("//*[@class=\"x-boilerpipe-mark1\"]/..").get(0).toString());
                html = jxDocument.sel("//*[@class=\"x-boilerpipe-mark1\"]/..").get(0).toString();


            } catch (InputMismatchException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            html = "";
        }


        LogUtil.info("轻舟自动", "内容结果" + html);

        return html;
    }

    public static String StringFilter(String str) throws PatternSyntaxException {

        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


}
