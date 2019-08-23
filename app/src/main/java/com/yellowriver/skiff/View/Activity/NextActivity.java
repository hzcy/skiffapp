package com.yellowriver.skiff.View.Activity;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntity;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Fragment.Home.HomeDataViewFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.yellowriver.skiff.R.drawable.ic_more_vert_black_24dp;

/**
 * 继续解析Activity 使用HomeDataView.Fragment加载数据
 *
 * @author huang
 * @date 2019
 */
public class NextActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.drawer_layout)
    CoordinatorLayout drawerLayout;
    /**
     * 源分组
     */
    private String qzGroupName;
    /**
     * 标题
     */
    private String qzTitle;
    /**
     * 源名称
     */
    private String qzSourceName;
    /**
     * 链接
     */
    private String qzLink;
    private String qzStep;
    /**
     * 首页还是搜索
     */
    private String qzSoucesType;
    /**
     * 搜索关键字
     */
    private String qzQuery;
    /**
     * 当前所在分组下标
     */
    private int qzindex;




    /**
     * 用于收藏
     */
    private String qzCover;
    private String qzSummary;
    private String qzDate;
    private int readIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        bindData();
        bindView();
    }


    private void bindData() {
        //源分组
        qzGroupName = getIntent().getStringExtra("qzGroupName");
        //标题
        qzTitle = getIntent().getStringExtra("qzTitle");
        //源名称
        qzSourceName = getIntent().getStringExtra("qzSourceName");
        //链接
        qzLink = getIntent().getStringExtra("qzLink");
        qzStep = getIntent().getStringExtra("qzStep");
        //搜索还是首页
        qzSoucesType = getIntent().getStringExtra("qzSoucesType");
        //搜索关键字  第二步一般不需要
        qzQuery = getIntent().getStringExtra("qzQuery");
        //当前所在分组下标
        qzindex = getIntent().getIntExtra("qzindex", 0);
        qzCover = getIntent().getStringExtra("qzCover");
        qzSummary = getIntent().getStringExtra("qzSummary");
        qzDate = getIntent().getStringExtra("qzDate");
        readIndex = getIntent().getIntExtra("readIndex",0);
    }

    private void bindView() {
        ButterKnife.bind(this);
        //标题
        mToolbar.setTitle(qzTitle);
        setSupportActionBar(mToolbar);
        mToolbar.setOverflowIcon(getResources().getDrawable(ic_more_vert_black_24dp));
        //这样共用一个fragment 和同一个activity
        switch (qzStep) {

            case "1":
                showFragment(HomeDataViewFragment.getInstance(qzGroupName, qzSourceName, "2", qzLink, qzSoucesType, qzQuery, qzindex, qzTitle,readIndex));
                break;
            case "2":
                showFragment(HomeDataViewFragment.getInstance(qzGroupName, qzSourceName, "3", qzLink, qzSoucesType, qzQuery, qzindex, qzTitle,readIndex));

                break;
            case "3":

                break;
            default:
                break;
        }

    }

    //这里要改成替换 先删掉旧在添加新的
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }


    private static final int MENU_CONFIRM = 17;
    MenuItem item;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        item = menu.add(Menu.NONE, MENU_CONFIRM, 0, "收藏");
        //主要是这句话
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        //添加监听事件
        item.setOnMenuItemClickListener(listener);
        if (isAdd()) {
            item.setIcon(R.drawable.ic_star_black_24dp);
        } else {
            item.setIcon(R.drawable.ic_star_border_black_24dp);
        }
        return true;
    }

    MenuItem.OnMenuItemClickListener listener = item -> {
        switch (item.getItemId()) {
            case MENU_CONFIRM:
                addFavorite();
                break;
            default:
                break;
        }
        return false;
    };


    /**
     * 添加收藏
     */
    private void addFavorite() {
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setGrouping(qzGroupName);
        favoriteEntity.setSourcesName(qzSourceName);
        favoriteEntity.setType(qzSoucesType);
        favoriteEntity.setStep(qzStep);
        favoriteEntity.setTitle(qzTitle);
        favoriteEntity.setSummary(qzSummary);
        favoriteEntity.setCover(qzCover);
        favoriteEntity.setDate(qzDate);
        favoriteEntity.setLink(qzLink);
        favoriteEntity.setReadIndex(0);
        favoriteEntity.setSpinnerSel(qzindex);
        boolean isAdd = SQLModel.getInstance().addFavorite(favoriteEntity);
        if (isAdd) {
            SnackbarUtil.ShortSnackbar(drawerLayout,"收藏成功！",SnackbarUtil.Confirm).show();

            SharedPreferencesUtils.FavoriteChange(true, getApplicationContext());
            item.setIcon(R.drawable.ic_star_black_24dp);
        } else {
            SnackbarUtil.ShortSnackbar(drawerLayout,"已经收藏过了！",SnackbarUtil.Warning).show();




        }
    }


    private boolean isAdd() {
        List<FavoriteEntity> favoriteEntityList = SQLModel.getInstance().getFavoritebyTitle(qzTitle, qzSourceName);
        if (favoriteEntityList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }



}
