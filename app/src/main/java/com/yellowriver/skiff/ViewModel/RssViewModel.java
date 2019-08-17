package com.yellowriver.skiff.ViewModel;

import android.net.ParseException;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.prof.rssparser.Article;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 * RssViewModel
 * @author huang
 * @date 2019
 */
public class RssViewModel extends ViewModel {

    private MutableLiveData<Vector<DataEntity>> mDataEntity = null;

    public MutableLiveData<Vector<DataEntity>> getArticleList() {
        if (mDataEntity == null) {
            mDataEntity = new MutableLiveData<>();
        }
        return mDataEntity;
    }

    private void setArticleList(Vector<DataEntity> mDataEntity) {
        this.mDataEntity.postValue(mDataEntity);
    }


    public void fetchFeed(String urlString) {

        Parser parser = new Parser();
        parser.onFinish(new OnTaskCompleted() {
            //what to do when the parsing is done
            @Override
            public void onTaskCompleted(@NotNull List<Article> list) {
                if (list != null) {
                    Vector<DataEntity> dataEntities = new Vector<>();
                    for (Article article : list) {
                        String pubDateString = null;
                        try {
                            String sourceDateString = article.getPubDate();
                            if (sourceDateString != null) {
                                SimpleDateFormat sourceSdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                                Date date = null;
                                try {
                                    date = sourceSdf.parse(sourceDateString);
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                                if (date != null) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                                    pubDateString = sdf.format(date);
                                } else {
                                    pubDateString = article.getPubDate();
                                }
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                            pubDateString = article.getPubDate();
                        }
                        DataEntity dataEntity = new DataEntity(article.getTitle(), article.getDescription(), article.getImage(), article.getLink(), pubDateString, "1", "3");
                        dataEntities.add(dataEntity);
                    }
                    setArticleList(dataEntities);
                }
            }
            //what to do in case of error
            @Override
            public void onError(@NotNull Exception e) {
                setArticleList(new Vector<>());
                e.printStackTrace();
            }
        });
        parser.execute(urlString);
    }
}
