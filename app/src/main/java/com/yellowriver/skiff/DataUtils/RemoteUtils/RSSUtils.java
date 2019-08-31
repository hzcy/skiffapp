package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;


import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndFeed;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.FeedException;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.SyndFeedInput;
import com.google.code.rome.android.repackaged.com.sun.syndication.io.XmlReader;


import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Model.SQLModel;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import java.net.URL;

import java.util.zip.DataFormatException;

/**
 * @author huang
 */
public class RSSUtils {

    private static final RSSUtils instance = new RSSUtils();

    private RSSUtils() {
    }

    public static RSSUtils getInstance() {
        return instance;
    }

    public static boolean insertRSS(String url, String group) {
        boolean isadd = false;

        String title = "";
        String xml = NetUtils.getInstance().getRequest(url,"1");
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = null;
        ByteArrayInputStream inputStream= null;
        try {
            inputStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            feed = input.build(new XmlReader(inputStream));
            Log.d("ok", "insertRSS: "+feed.getTitle());
            title = feed.getTitle();
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!"".equals(title)) {
            HomeEntity homeEntity = new HomeEntity();
            homeEntity.setTitle(title);
            homeEntity.setGrouping(group);
            homeEntity.setType("home");
            homeEntity.setFirsturl(url);
            homeEntity.setFirstLinkType("9");
            homeEntity.setFirstListXpath("{QZRSS}");

            long addresult = SQLModel.getInstance().addSouce(homeEntity);
            if (addresult >= 0) {
                isadd = true;
            }
        }
        return isadd;
    }



}
