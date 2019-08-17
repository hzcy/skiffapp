package com.yellowriver.skiff.DataUtils.RemoteUtils;

import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Model.SQLModel;

import org.mcsoxford.rss.RSSFeed;
import org.mcsoxford.rss.RSSReader;
import org.mcsoxford.rss.RSSReaderException;

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
        RSSReader reader = new RSSReader();
        String title = "";
        try {
            RSSFeed feed = reader.load(url);

            if (feed != null) {
                title = feed.getTitle();

            }
        } catch (RSSReaderException e) {
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
