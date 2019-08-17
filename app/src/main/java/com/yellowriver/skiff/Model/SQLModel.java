package com.yellowriver.skiff.Model;

import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Interface.SQLInterface;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huang
 */
public class SQLModel implements SQLInterface {


    private static final SQLModel INSTANCE = new SQLModel();

    private SQLModel() {
    }

    public static SQLModel getInstance() {
        return INSTANCE;
    }


    @Override
    public List<String> getGroup() {

        return SQLiteUtils.getInstance().getGroup();
    }

    @Override
    public ArrayList<String> getTitleByGroup(String groupName, String type) {

        return SQLiteUtils.getInstance().getTitleByGroup(groupName, type);


    }

    @Override
    public ArrayList<String> getTitleByGroup(String groupName) {

        return SQLiteUtils.getInstance().getTitleByGroup(groupName);

    }

    @Override
    public List<HomeEntity> gethomeEntitiesByGroup(String groupName) {

        return SQLiteUtils.getInstance().gethomeEntitiesByGroup(groupName);


    }

    @Override
    public List<HomeEntity> getXpathbyTitle(String title, String type) {

        return SQLiteUtils.getInstance().getXpathbyTitle(title, type);
    }

    @Override
    public List<HomeEntity> getXpathbyTitle(String title) {


        return SQLiteUtils.getInstance().getXpathbyTitle(title);

    }

    @Override
    public void delbyTitle(String title) {


        SQLiteUtils.getInstance().delbyTitle(title);

    }

    @Override
    public long addSouce(HomeEntity homeEntity) {

        return SQLiteUtils.getInstance().addSouce(homeEntity);


    }

    @Override
    public List<String> getGroupfromFavorite() {

        return SQLiteUtils.getInstance().getGroupfromFavorite();


    }

    @Override
    public List<FavoriteEntity> getFavoritebyGroup(String group) {

        return SQLiteUtils.getInstance().getFavoritebyGroup(group);


    }

    @Override
    public boolean addFavorite(FavoriteEntity favoriteEntity) {

        return SQLiteUtils.getInstance().addFavorite(favoriteEntity);


    }

    @Override
    public List<FavoriteEntity> getFavoritebyTitle(String title) {

        return SQLiteUtils.getInstance().getFavoritebyTitle(title);


    }

    @Override
    public List<FavoriteEntity> getFavoritebyTitle(String title, String sourcesName) {

        return SQLiteUtils.getInstance().getFavoritebyTitle(title, sourcesName);


    }

    @Override
    public void delFavoritebyTitle(String title) {

        SQLiteUtils.getInstance().delFavoritebyTitle(title);

    }

    @Override
    public void delFavorite(FavoriteEntity favoriteEntity) {

        SQLiteUtils.getInstance().delFavorite(favoriteEntity);

    }


}
