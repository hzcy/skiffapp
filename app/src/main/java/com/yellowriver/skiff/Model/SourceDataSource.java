package com.yellowriver.skiff.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.DataUtils.RemoteUtils.JsonUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.NetUtils;
import com.yellowriver.skiff.Interface.SourceDataInterface;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author huang
 */
public class SourceDataSource implements SourceDataInterface {

    private static final SourceDataSource INSTANCE = new SourceDataSource();

    private SourceDataSource() {
    }

    public static SourceDataSource getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean addSource(String url) {
        boolean isAdd = false;
        Gson gson = new Gson();
        try {
            URL url1 = new URL(url);
            url1.openConnection().connect();
            String data = NetUtils.getInstance().getRequest(url, "");
            if (data != null) {
                if (JsonUtils.isJSONValid(data)) {
                    Type type = new TypeToken<HomeEntity>() {
                    }.getType();
                    HomeEntity homeEntity = gson.fromJson(data, type);
                    if (homeEntity != null) {
                        String newtitle = homeEntity.getTitle();
                        String newtype = homeEntity.getType();
                        List<HomeEntity> homeEntityList = SQLModel.getInstance().getXpathbyTitle(newtitle, newtype);
                        if (!homeEntityList.isEmpty()) {
                            isAdd = false;
                        } else {
                            long addresult = SQLModel.getInstance().addSouce(homeEntity);

                            if (addresult >= 0) {
                                isAdd = true;
                            } else {
                                isAdd = false;
                            }
                        }
                    } else {
                        isAdd = false;
                    }
                } else {
                    isAdd = false;
                }
            } else {
                isAdd = false;
            }
        } catch (MalformedURLException e) {
            isAdd = false;
            e.printStackTrace();
        } catch (IOException e) {
            isAdd = false;
            e.printStackTrace();
        }
        return isAdd;
    }



}
