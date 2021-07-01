package com.yellowriver.skiff.DataUtils.RemoteUtils;

import android.util.Log;


import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseNode;
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
    public static List<BaseNode> getSourceAllGroup() {

        List<BaseNode> list = new ArrayList<>();
//        String grouplisturl = baseURL + "/sources/groupList.json";
//        String baseurl = "https://hege.gitee.io/api/sources";

        // 获取文档内容
        String json = NetUtils.getInstance().getRequest("https://raw.githubusercontent.com/hzcy/skiffpage/main/api/sources/groupList.json", "1");
        Log.d("新开始", "run: "+json);
        if (json!=null) {

            //Log.d(TAG, "getSourceAllGroup: " + json);
            List<GroupEntity> sourceBean = null;
            if (JsonUtils.isJSONValid(json)) {
               // Log.d(TAG, "getSourceAllGroup: 是json");
                Type type = new TypeToken<List<GroupEntity>>() {
                }.getType();
                sourceBean = gson.fromJson(json, type);
            }

            if (sourceBean != null) {
                //Log.d(TAG, "getSourceAllGroup: " + sourceBean.size());

                for (int i = 0; i < sourceBean.size(); i++) {

//                    group groupBean = new group();
//                    groupBean.setGroupName(sourceBean.get(i).getName());
//                    groupBean.setGroupLink(sourceBean.get(i).getLink());
                    List<BaseNode> sourcesList = new ArrayList<>();
                    sourcesList = getSourceByGroup(sourceBean.get(i).getLink());
                    if (sourcesList!=null) {

                      //  Log.d(TAG, "getSourceAllGroup: " + sourcesList.size());
                        group groupBean = new group(sourceBean.get(i).getName(),sourceBean.get(i).getLink(),sourcesList);
                        list.add(groupBean);

                    }

                }
            }
        }

        return list;
    }

    //源市场 根据分组获取源
    private static List<BaseNode> getSourceByGroup(String grouplink) {

        List<BaseNode> sourcesBeanList = new ArrayList<>();
        // 获取文档内容
        String json = NetUtils.getInstance().getRequest(baseURL + "/sources" + grouplink, "1");

        if (json != null) {
            List<SourcesEntity> sourcesEntities = null;
            if (JsonUtils.isJSONValid(json)) {
                Type type = new TypeToken<List<SourcesEntity>>() {
                }.getType();
                sourcesEntities = gson.fromJson(json, type);
            }


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
                    sourcesBean.setSourcesType(sourcesEntities.get(i).getType());
                    sourcesBean.setSourcesDate(sourcesEntities.get(i).getDate());
                    sourcesBean.setSourcesIshave(ishave);
                    sourcesBeanList.add(sourcesBean);


                }
            }
        }

        return sourcesBeanList;
    }
}
