package com.yellowriver.skiff;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;

import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.View.Fragment.About.AboutFragment;
import com.yellowriver.skiff.View.Fragment.Favorite.FavoriteFragment;
import com.yellowriver.skiff.View.Fragment.Home.HomeViewFragment;
import com.yellowriver.skiff.View.Fragment.Sources.SourceFragment;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import skin.support.SkinCompatManager;
import skin.support.design.widget.SkinMaterialBottomNavigationView;


/**
 * 主界面 管理四个fragment.
 *
 * @author huang
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.drawer_layout)
    CoordinatorLayout drawerLayout;
    @BindView(R.id.nav_view)
    SkinMaterialBottomNavigationView navView;

    //两次返回退出
    private long mExitTime;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (firstRun()) {
            SkinCompatManager.getInstance().loadSkin("zhihulan.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
            SharedPreferencesUtils.themeWrite(getApplicationContext(), "默认蓝");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //控制首页是否重新加载  源更新时
        SharedPreferencesUtils.dataChange(false, getApplicationContext());
        initView();
        //获取管理者
        supportFragmentManager = getSupportFragmentManager();
        //默认显示第一条
        FragmentTransaction fragmentTransaction
                = supportFragmentManager.beginTransaction();
        if (homeViewFragment != null) {
            fragmentTransaction.hide(homeViewFragment);
        }
        if (homeViewFragment == null) {
            homeViewFragment = new HomeViewFragment();
            fragmentTransaction.add(R.id.fragment_container, homeViewFragment);
        } else {
            fragmentTransaction.show(homeViewFragment);
        }
        fragmentTransaction.commit();
    }

    private void initView() {
        //底部导航相关
        navView.setOnNavigationItemSelectedListener(
                mOnNavigationItemSelectedListener);
        navView.setLabelVisibilityMode(
                LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
    }

    /**
     * 首页.
     */
    private HomeViewFragment homeViewFragment;
    /**
     * 收藏.
     */
    private FavoriteFragment favoriteFragment;
    /**
     * 源管理.
     */
    private SourceFragment sourceFragment;
    /**
     * 关于.
     */
    private AboutFragment aboutFragment;
    /**
     * Fragment管理.
     */
    private FragmentManager supportFragmentManager;

    /**
     * 底部导航栏 点击事件.
     */
    private final BottomNavigationView
            .OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(final @NonNull MenuItem item) {
            //分发事件
            FragmentTransaction fragmentTransaction = supportFragmentManager
                    .beginTransaction();
            //如果当前对象不等于null的话，那就隐藏他，不然会造成页面叠加现象
            hide(fragmentTransaction);
            switch (item.getItemId()) {
                case R.id.nav_home:
                    //首页
                    if (homeViewFragment == null) {
                        homeViewFragment = new HomeViewFragment();
                        fragmentTransaction
                                .add(R.id.fragment_container, homeViewFragment);
                    } else {
                        if (SharedPreferencesUtils
                                .readdataChange(getApplicationContext())) {
                            fragmentTransaction.remove(homeViewFragment);
                            homeViewFragment = new HomeViewFragment();
                            fragmentTransaction.add(
                                    R.id.fragment_container, homeViewFragment);

                            //控制首页是否重新加载  源更新时
                            SharedPreferencesUtils.dataChange(
                                    false, getApplicationContext());
                        } else {
                            fragmentTransaction.show(homeViewFragment);
                        }
                    }
                    //提交事件
                    fragmentTransaction.commit();
                    return true;
                case R.id.nav_favorite:
                    //收藏
                    if (favoriteFragment == null) {
                        favoriteFragment = new FavoriteFragment();
                        fragmentTransaction.add(
                                R.id.fragment_container, favoriteFragment);
                    } else {
                        if (SharedPreferencesUtils
                                .readFavoriteChange(getApplicationContext())) {
                            fragmentTransaction.remove(favoriteFragment);
                            favoriteFragment = new FavoriteFragment();
                            fragmentTransaction.add(
                                    R.id.fragment_container, favoriteFragment);
                            //控制首页是否重新加载  源更新时
                            SharedPreferencesUtils.FavoriteChange(
                                    false, getApplicationContext());
                        } else {
                            fragmentTransaction.show(favoriteFragment);
                        }
                    }
                    //提交事件
                    fragmentTransaction.commit();
                    return true;
                case R.id.nav_yuansetting:
                    //源管理
                    if (sourceFragment == null) {
                        sourceFragment = new SourceFragment();
                        fragmentTransaction.add(
                                R.id.fragment_container, sourceFragment);
                    } else {
                        if (SharedPreferencesUtils
                                .readSourceReload(getApplicationContext())) {
                            fragmentTransaction.remove(sourceFragment);
                            sourceFragment = new SourceFragment();
                            fragmentTransaction.add(
                                    R.id.fragment_container, sourceFragment);
                            //控制收藏是否重新加载  源更新时
                            SharedPreferencesUtils.writeSourceReload(
                                    false, getApplicationContext());
                        } else {
                            fragmentTransaction.show(sourceFragment);
                        }
                        fragmentTransaction.show(sourceFragment);
                    }
                    //提交事件
                    fragmentTransaction.commit();
                    return true;
                case R.id.nav_about:
                    //关于
                    if (aboutFragment == null) {
                        aboutFragment = new AboutFragment();
                        fragmentTransaction.add(
                                R.id.fragment_container, aboutFragment);
                    } else {
                        fragmentTransaction.show(aboutFragment);
                    }
                    //提交事件
                    fragmentTransaction.commit();
                    return true;
                default:
                    break;
            }
            return true;
        }
    };


    private void hide(FragmentTransaction fragmentTransaction) {
        if (homeViewFragment != null) {
            fragmentTransaction.hide(homeViewFragment);
        }
        if (favoriteFragment != null) {
            fragmentTransaction.hide(favoriteFragment);
        }
        if (sourceFragment != null) {
            fragmentTransaction.hide(sourceFragment);
        }
        if (aboutFragment != null) {
            fragmentTransaction.hide(aboutFragment);
        }
    }


    private boolean firstRun() {
        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun",0);
        Boolean first_run = sharedPreferences.getBoolean("First",true);
        if (first_run){
            sharedPreferences.edit().putBoolean("First",false).commit();
        }
        return first_run;
    }


    /*
     *两次返回退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;

                Toast.makeText(this, "再按一次退出轻舟", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
