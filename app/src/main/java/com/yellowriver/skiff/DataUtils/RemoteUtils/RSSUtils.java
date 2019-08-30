package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;

//import com.icosillion.podengine.exceptions.InvalidFeedException;
//import com.icosillion.podengine.exceptions.MalformedFeedException;
//import com.icosillion.podengine.models.Podcast;
import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.prof.rssparser.Article;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Model.SQLModel;

import org.jetbrains.annotations.NotNull;
import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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


    //添加RSS
    public static boolean insertRSS(String url, String group) {
        boolean isadd = false;
//       RSSReader reader = new RSSReader();
        String title = "";

        InputStream inputStream = null;
        try {
            inputStream = new URL(url).openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Feed feed = null;
        try {
            feed = EarlParser.parseOrThrow(inputStream, 0);
            title = feed.getTitle();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DataFormatException e) {
            e.printStackTrace();
        }

//        try {
//            Podcast podcast = new Podcast(new URL(url));
//            title = podcast.getTitle();
//        } catch (InvalidFeedException e) {
//            e.printStackTrace();
//        } catch (MalformedFeedException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        try {
//            RSSFeed feed = reader.load(url);
//
//            if (feed != null) {
//                title = feed.getTitle();
//
//            }
//        } catch (RSSReaderException e) {
//            e.printStackTrace();
//        }
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


    private String parseXMLWithPull(String result) {
        String title = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(result));




            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                Log.d("ggg", "nodeName: "+nodeName);
                switch (eventType) {
                    case XmlPullParser.START_TAG://开始解析
                        if ("title".equals(nodeName)) {
                            title = parser.getText();
                        }
                        break;

                    case XmlPullParser.END_TAG://完成解析
                        if ("title".equals(nodeName)) {

                        }
                        break;
                    default:
                        break;
                }
                eventType=parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return title;
    }

}
