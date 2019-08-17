package com.yellowriver.skiff;

import android.database.sqlite.SQLiteDatabase;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;



import com.yellowriver.skiff.Bean.DataBaseBean.DaoMaster;
import com.yellowriver.skiff.Bean.DataBaseBean.DaoSession;
import com.tencent.bugly.crashreport.CrashReport;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * @author huang
 */
public class MyApplication extends MultiDexApplication  {
    private DaoSession mDaoSession;
    public static MyApplication instances;

    @Override
    public void onCreate() {
        super.onCreate();
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .loadSkin();
        MultiDex.install(this);
        CrashReport.initCrashReport(getApplicationContext(), "c46c245326", true);
        instances = this;
        setDatabase();
    }



    /**
     * 单例模式 * *
     */
//    public static MyApplication getInstances() {
//        return instances;
//    }


 /* 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。*/

    private void setDatabase() {
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "skiff-db", null);
        SQLiteDatabase db = mHelper.getWritableDatabase();
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }



    public DaoSession getDaoSession() {
        return mDaoSession;
    }



}
