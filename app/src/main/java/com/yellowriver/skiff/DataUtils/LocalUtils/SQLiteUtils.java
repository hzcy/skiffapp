package com.yellowriver.skiff.DataUtils.LocalUtils;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import com.google.gson.Gson;
import com.yellowriver.skiff.Bean.DataBaseBean.DaoSession;
import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntity;

import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntityDao;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;

import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntityDao;
import com.yellowriver.skiff.MyApplication;

/**
 * @author huang
 */
public class SQLiteUtils {

    private static final SQLiteUtils INSTANCE = new SQLiteUtils();
    private static final String TAG = "SQLiteUtils";

    private SQLiteUtils() {
    }

    public static SQLiteUtils getInstance() {
        return INSTANCE;
    }

    private DaoSession daoSession =  MyApplication.instances.getDaoSession();

    public DaoSession getDaoSession()
    {
        return daoSession;
    }



    //***********************************************************源相关
    //去重查询数据  获取分类
    public List<String> getGroup() {

        String SQL_DISTINCT_ENAME = "SELECT DISTINCT " + HomeEntityDao.Properties.Grouping.columnName + " FROM " + HomeEntityDao.TABLENAME;

        ArrayList<String> result = new ArrayList<>();
        try (Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null)) {
            if (c.moveToFirst()) {
                do {
                    result.add(c.getString(0));
                } while (c.moveToNext());
            }
        }
        return result;
    }

    //根据分组名和类型获取分组下的数据 首页表  2019/6/9 区分首页和搜索
    public  ArrayList<String> getTitleByGroup(String groupName,String type)
    {
        List<HomeEntity> homeEntities = daoSession.queryRaw(HomeEntity.class, " where " + HomeEntityDao.Properties.Grouping.columnName + " = ? and " + HomeEntityDao.Properties.Type.columnName+ " = ?", groupName,type);
        ArrayList<String> titlelists = new ArrayList<>();
        if (homeEntities.size() != 0) {
            for (int i = 0; i < homeEntities.size(); i++) {
                //将获取的标题添加到一起
                titlelists.add(homeEntities.get(i).getTitle());
            }
        }
        return  titlelists;
    }

    //获取分组下的数据 首页表  2019/6/9 区分首页和搜索
    public  ArrayList<String> getTitleByGroup(String groupName)
    {
        List<HomeEntity> homeEntities = daoSession.queryRaw(HomeEntity.class, " where " + HomeEntityDao.Properties.Grouping.columnName + " = ? " , groupName);
        ArrayList<String> titlelists = new ArrayList<>();
        if (homeEntities.size() != 0) {
            for (int i = 0; i < homeEntities.size(); i++) {
                //将获取的标题添加到一起
                titlelists.add(homeEntities.get(i).getTitle());
            }
        }
        return  titlelists;
    }

    public  List<HomeEntity> gethomeEntitiesByGroup(String groupName)
    {

        return daoSession.queryRaw(HomeEntity.class, " where " + HomeEntityDao.Properties.Grouping.columnName + " = ? " , groupName);
    }

    //根据title获取数据  2019/6/9 区分首页和搜索
    public  List<HomeEntity> getXpathbyTitle(String title,String type)
    {
        List<HomeEntity> homeEntityList = null;
        try {
             homeEntityList = daoSession.queryRaw(HomeEntity.class, " where " + HomeEntityDao.Properties.Title.columnName + " = ? and " + HomeEntityDao.Properties.Type.columnName + " = ? ", title, type);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
         return homeEntityList;
    }

    public  List<HomeEntity> getXpathbyTitle(String title)
    {
        return daoSession.queryRaw(HomeEntity.class, " where "+HomeEntityDao.Properties.Title.columnName+" = ? " , title);
    }
    public  List<HomeEntity> getXpathbyGroup(String group)
    {
        return daoSession.queryRaw(HomeEntity.class, " where "+HomeEntityDao.Properties.Grouping.columnName+" = ? " , group);
    }

//    public static List<HomeEntity> getXpathbyTitleandType(String title,String type)
//    {
//        return daoSession.queryRaw(HomeEntity.class, " where "+HomeEntityDao.Properties.Title.columnName+" = ? and " +HomeEntityDao.Properties.Type.columnName+" = ? " , title,type);
//    }

    //根据分组 删除分组下的所有源
    public  void delbyTitle(String title,String type)
    {
        List<HomeEntity> homeEntities = getXpathbyTitle(title,type);

        if(homeEntities!=null) {
            for (HomeEntity homeEntity : homeEntities) {

                daoSession.delete(homeEntity);

            }
        }
    }

    //删除分组
    public  void delbyGroup(String group)
    {
        List<HomeEntity> homeEntities = getXpathbyGroup(group);

        for (HomeEntity homeEntity : homeEntities) {

            daoSession.delete(homeEntity);

        }
    }

    //添加源
    public  long addSouce(HomeEntity homeEntity)
    {
        HomeEntityDao homeEntityDao = daoSession.getHomeEntityDao();

        return homeEntityDao.insert(homeEntity);

    }


    public void addHome(HomeEntity homeEntity) {

        HomeEntityDao homeEntityDao = daoSession.getHomeEntityDao();
        List<HomeEntity> homeEntityList = getXpathbyTitle(homeEntity.getTitle(), homeEntity.getType());
        if (homeEntityList.isEmpty()) {
            long addid = homeEntityDao.insert(homeEntity);
            if (addid >= 0) {
                Log.d(TAG, "addHome: 添加了");
            }
        } else {
            Log.d(TAG, "addHome: 已经存在了");
        }

    }

    public boolean addHome2(HomeEntity homeEntity) {

        boolean a = false;
        HomeEntityDao homeEntityDao = daoSession.getHomeEntityDao();
        List<HomeEntity> homeEntityList = getXpathbyTitle(homeEntity.getTitle(), homeEntity.getType());
        if (homeEntityList.isEmpty()) {
            long addid = homeEntityDao.insert(homeEntity);
            if (addid >= 0) {
                a = true;
                Log.d(TAG, "addHome: 添加了");
            }
        } else {
            Log.d(TAG, "addHome: 已经存在了");
        }
        return a;

    }


    //***********************************************************源相关

    //***********************************************************收藏相关
    //获取收藏表所有分组
    public  List<String> getGroupfromFavorite() {

        String SQL_DISTINCT_ENAME = "SELECT DISTINCT " + FavoriteEntityDao.Properties.Grouping.columnName + " FROM " + FavoriteEntityDao.TABLENAME;

        ArrayList<String> result = new ArrayList<>();
        try (Cursor c = daoSession.getDatabase().rawQuery(SQL_DISTINCT_ENAME, null)) {
            if (c.moveToFirst()) {
                do {
                    result.add(c.getString(0));
                } while (c.moveToNext());
            }
        }
        return result;
    }

    //根据分组获取分组下的收藏数据
    public  List<FavoriteEntity> getFavoritebyGroup(String group)
    {

        Log.d(TAG, "getFavoritebyGroup: "+group);
        return  daoSession.queryRaw(FavoriteEntity.class, " where "+FavoriteEntityDao.Properties.Grouping.columnName+" = ? " , group);


    }

    //插入收藏
    public  boolean addFavorite(FavoriteEntity favoriteEntity)
    {
        boolean isAdd = false;
        FavoriteEntityDao favoriteEntityDao = daoSession.getFavoriteEntityDao();
        List<FavoriteEntity> favoriteEntities = getFavoritebyTitle(favoriteEntity.getTitle(),favoriteEntity.getSourcesName());
        if (favoriteEntities.isEmpty())
        {
           long addid =  favoriteEntityDao.insert(favoriteEntity);
           if (addid>=0)
           {
               isAdd = true;
           }
        }else
        {
            isAdd = false;
        }

        return isAdd;

    }
    public  List<FavoriteEntity> getFavoritebyTitle(String title)
    {
        return daoSession.queryRaw(FavoriteEntity.class, " where "+FavoriteEntityDao.Properties.Title.columnName+" = ? " , title);
    }

    public  List<FavoriteEntity> getFavoritebyTitle(String title,String sourcesName)
    {
        return daoSession.queryRaw(FavoriteEntity.class, " where "+FavoriteEntityDao.Properties.Title.columnName+"= ? and " +FavoriteEntityDao.Properties.SourcesName.columnName+ " = ? ", title,sourcesName);
    }

    //删除指定标题的收藏
    public  void delFavoritebyTitle(String title)
    {
        List<FavoriteEntity> favoriteEntities = getFavoritebyTitle(title);
        for (FavoriteEntity favoriteEntity : favoriteEntities) {
            daoSession.delete(favoriteEntity);

        }
    }

    public  void delFavorite(FavoriteEntity favoriteEntity)
    {
            daoSession.delete(favoriteEntity);
    }


    //备份用 将数据库转为json
    public String getAllHome()
    {

        List<HomeEntity> list =  daoSession.getHomeEntityDao().queryBuilder().list();
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    public String getAllFavorite()
    {
        List<FavoriteEntity> list =  daoSession.getFavoriteEntityDao().queryBuilder().list();
        Gson gson = new Gson();
        return gson.toJson(list);
    }
    //阅读模式 阅读记录
    public boolean updateFavoriteByTitle(String title,String sourcesName,int readIndex)
    {
        boolean ups = false;
        List<FavoriteEntity> favoriteEntities = getFavoritebyTitle(title,sourcesName);
        if (favoriteEntities!=null)
        {
            if (favoriteEntities.size()>0)
            {
                FavoriteEntity favoriteEntity = favoriteEntities.get(0);
                favoriteEntity.setReadIndex(readIndex);
                daoSession.update(favoriteEntity);
                ups = true;
            }
        }
        return ups;
    }
}
