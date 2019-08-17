package com.yellowriver.skiff.Interface;

import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作接口
 * @author huang
 */
public interface SQLInterface {
    /**
     * 源 表操作  获取所有分组
     * @return 分组list
     */
    List<String> getGroup();

    /**
     * 根据分组名和类型获取分组下的数据 首页表  2019/6/9 区分首页和搜索
     * @param groupName  分组名
     * @param type  源类型（首页或搜索）
     * @return 根据分组名和类型获取的源标题list
     */
    ArrayList<String> getTitleByGroup(String groupName, String type);

    /**
     *获取分组下的数据 首页表  2019/6/9 区分首页和搜索
     */
   ArrayList<String> getTitleByGroup(String groupName);

    /**
     * 根据分组获取实体
     * @param groupName
     * @return
     */
    List<HomeEntity> gethomeEntitiesByGroup(String groupName);

    /**
     *根据title和类型获取数据  2019/6/9 区分首页和搜索
     */
   List<HomeEntity> getXpathbyTitle(String title,String type);
    /**
     *根据title获取数据  2019/6/9 区分首页和搜索
     */
    List<HomeEntity> getXpathbyTitle(String title);

    /**
     *根据分组 删除分组下的所有源
     */
    void delbyTitle(String title);

    /**
     *添加源
     */
    long addSouce(HomeEntity homeEntity);


    /**
     *获取收藏表所有分组
     */
    List<String> getGroupfromFavorite();

    /**
     *根据分组获取分组下的收藏数据
     */
    List<FavoriteEntity> getFavoritebyGroup(String group);

    /**
     *插入收藏
     */
    boolean addFavorite(FavoriteEntity favoriteEntity);

    /*
     * 根据分组标题获取收藏
     */
    List<FavoriteEntity> getFavoritebyTitle(String title);
    /*
     * 根据分组和源名字获取收藏
     */
    List<FavoriteEntity> getFavoritebyTitle(String title,String sourcesName);

    /**
     *删除指定标题的收藏
     */
    void delFavoritebyTitle(String title);
    /**
     * 删除指定标题的收藏
     */
    void delFavorite(FavoriteEntity favoriteEntity);


}
