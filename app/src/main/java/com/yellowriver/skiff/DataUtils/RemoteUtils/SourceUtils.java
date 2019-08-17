package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yellowriver.skiff.Bean.SourcesBean.GroupEntity;
import com.yellowriver.skiff.Bean.SourcesBean.SourcesEntity;
import com.yellowriver.skiff.Bean.SourcesBean.group;
import com.yellowriver.skiff.Bean.SourcesBean.sources;
import com.yellowriver.skiff.Model.SQLModel;


import java.lang.reflect.Type;
import java.util.ArrayList;

import java.util.List;

/**
 * 源管理
 * @author huang
 */
public class SourceUtils {


    private static Gson gson = new Gson();
    private static String baseURL = "https://hege.gitee.io/api";
    private static final String TAG = "SourceUtils";

    private static final SourceUtils INSTANCE = new SourceUtils();

    private SourceUtils() {
    }

    public static SourceUtils getInstance() {
        return INSTANCE;
    }

    //源市场 所有分组
    public static List<MultiItemEntity> getSourceAllGroup() {

        List<MultiItemEntity> list = new ArrayList<>();
        String grouplisturl = baseURL + "/sources/groupList.json";

        // 获取文档内容
        String json = NetUtils.getInstance().getRequest(grouplisturl, "1");

        Log.d(TAG, "getSourceAllGroup: "+json);
        List<GroupEntity> sourceBean = null;
        if (JsonUtils.isJSONValid(json)) {
            Log.d(TAG, "getSourceAllGroup: 是json");
            Type type = new TypeToken<List<GroupEntity>>() {
            }.getType();
            sourceBean = gson.fromJson(json, type);
        }

        if (sourceBean != null) {
            Log.d(TAG, "getSourceAllGroup: "+sourceBean.size());

            for (int i = 0; i < sourceBean.size(); i++) {

                group groupBean = new group();
                groupBean.setGroupName(sourceBean.get(i).getName());
                groupBean.setGroupLink(sourceBean.get(i).getLink());
                List<sources> sourcesList = getSourceByGroup(sourceBean.get(i).getLink());
                groupBean.setSourcess(sourcesList);
                Log.d(TAG, "getSourceAllGroup: "+sourcesList.size());
                for (sources sources : sourcesList)
                {
                    groupBean.addSubItem(sources);
                }
                list.add(groupBean);


            }
        }

        return list;
    }

    //源市场 根据分组获取源
    private static List<sources> getSourceByGroup(String grouplink) {

        List<sources> sourcesBeanList = new ArrayList<>();
        // 获取文档内容
        String json = NetUtils.getInstance().getRequest(baseURL + "/sources" + grouplink, "1");

        List<SourcesEntity> sourcesEntities = null;
        if (JsonUtils.isJSONValid(json)) {
            Type type = new TypeToken<List<SourcesEntity>>() {
            }.getType();
            sourcesEntities = gson.fromJson(json, type);
        }

        Log.d(TAG, "getSourceByGroup: "+sourcesEntities.size());
        if (sourcesEntities != null) {


            for (int i = 0; i < sourcesEntities.size(); i++) {

                String ishave;
                if (!SQLModel.getInstance().getXpathbyTitle(sourcesEntities.get(i).getName()).isEmpty()) {
                    ishave = "1";
                } else {
                    ishave = "0";
                }
                sources sourcesBean = new sources();
                sourcesBean.setSourcesName(sourcesEntities.get(i).getName());
                sourcesBean.setSourcesLink(sourcesEntities.get(i).getLink());
                sourcesBean.setSourcesDate(sourcesEntities.get(i).getType());
                sourcesBean.setSourcesIshave(ishave);
                sourcesBeanList.add(sourcesBean);


            }
        }

        return sourcesBeanList;
    }
}
