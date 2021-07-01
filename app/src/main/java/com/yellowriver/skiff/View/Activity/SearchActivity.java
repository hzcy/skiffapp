package com.yellowriver.skiff.View.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchView;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Fragment.Home.HomeTabFragment;
import com.yellowriver.skiff.View.Fragment.SearchOneFragment;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索界面
 *
 * @author huang
 */
public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchHome";
    @BindView(R.id.spinner)
    NiceSpinner mSpinner;
    @BindView(R.id.searchBar)
    SearchView mSearchView;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;



    //搜索关键字
    private String qzQuery;
    private int qzSpinnerSel;

    private String groupName;
    private String type;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initData();
        initView();
        loadSql();
    }

    private void initData()
    {
        qzQuery = getIntent().getStringExtra("qzQuery");
        groupName = getIntent().getStringExtra("qzGroupName");
        type = "search";
    }



    //加载view
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mSearchView.setShape(Search.Shape.CLASSIC);
        mSearchView.setShadow(false);
        mSearchView.setShadowColor(Color.parseColor("#FFFFFF"));
        //开始搜索
        if (qzQuery != null) {
            mSearchView.setQuery(qzQuery, true);
        }

    }

    private void loadSql() {
        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(new Runnable() {
            @Override
            public void run() {

                List<String> list = SQLModel
                        .getInstance().getGroup();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d(TAG, "run: " + list);
                        updateView(list);

                    }
                });
            }
        });
    }

    private void updateView(List<String> list) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.myspinner, list);  //创建一个数组适配器
        //设置下拉列表框的下拉选项样式
        adapter.setDropDownViewResource(R.layout.myspinner);
        mSpinner.setAdapter(adapter);
        qzSpinnerSel = adapter.getPosition(groupName);
        Log.d(TAG, "updateView: "+groupName);
        Log.d(TAG, "updateView: "+qzSpinnerSel);
        //让第一个数据项已经被选中
        mSpinner.setSelectedIndex(qzSpinnerSel);
        //进入界面时获取第一个分组名称
        ArrayList<String> list1 = SQLModel.getInstance().getTitleByGroup(mSpinner.getItemAtPosition(qzSpinnerSel).toString(), "search");

        if (list1.isEmpty()) {
            //显示空提示
            //mAppBarLayout.setElevation(10);
            //showFragment(HomeViewFragment.getInstance("", "void", "", 0));

        } else {
            //加载数据
            Log.d(TAG, "initView: 加载数据");
            showFragment(groupName, type, qzQuery, mSpinner.getSelectedIndex());

        }
        bindEvent();

    }


    private void bindEvent()
    {
        //给Spinner添加事件监听
        mSpinner.setOnSpinnerItemSelectedListener((parent, view, position, id) -> {
            groupName = (String) parent.getItemAtPosition(position);
            //如果是没有搜索关键字 就打开首页 如果有就搜索
            if (TextUtils.isEmpty(mSearchView.getText())) {
                //mAppBarLayout.setElevation(0);

                //添加到tablayout上
                //showFragment(HomeViewFragment.getInstance(name,"home","",mSpinner.getSelectedIndex()));
            } else {
                ArrayList<String> list1 = SQLModel.getInstance().getTitleByGroup(groupName, "search");

                if (list1.isEmpty()) {
                    //显示空提示
                    //mAppBarLayout.setElevation(10);
                    // showFragment(HomeViewFragment.getInstance("", "void", "", 0));


                } else {
                    showFragment(groupName, type, qzQuery, mSpinner.getSelectedIndex());
                }
            }
        });

        mSearchView.setOnQueryTextListener(new Search.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(CharSequence query) {
                showFragment(groupName, type, query.toString(), mSpinner.getSelectedIndex());
                ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(Objects.requireNonNull(SearchActivity.this.getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }

            @Override
            public void onQueryTextChange(CharSequence newText) {

            }
        });
    }


    private void showFragment(String groupName, String type, String query, int index) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //如果切换成聚合搜索
        if("聚合搜索".equals(SharedPreferencesUtils.searchViewTypeRead(getApplicationContext()))){
            SearchOneFragment searchOneFragment = null;
            Log.d(TAG, "showFragment: "+fragmentTransaction.isEmpty());
            if (fragmentTransaction.isEmpty()) {
                searchOneFragment = SearchOneFragment.getInstance(groupName, type, query, index);
                fragmentTransaction.replace(R.id.fragment_container, searchOneFragment);
            } else {
                fragmentTransaction.remove(searchOneFragment);
                searchOneFragment = SearchOneFragment.getInstance(groupName, type, query, index);
                fragmentTransaction.replace(R.id.fragment_container, searchOneFragment);
            }

        }else{
            //分开搜索
            HomeTabFragment homeTabFragment = null;
            Log.d(TAG, "showFragment: "+fragmentTransaction.isEmpty());
            if (fragmentTransaction.isEmpty()) {
                homeTabFragment = HomeTabFragment.getInstance(groupName, type, query, index);
                fragmentTransaction.replace(R.id.fragment_container, homeTabFragment);
            } else {
                fragmentTransaction.remove(homeTabFragment);
                homeTabFragment = HomeTabFragment.getInstance(groupName, type, query, index);
                fragmentTransaction.replace(R.id.fragment_container, homeTabFragment);
            }
        }

        //提交事件
        fragmentTransaction.commit();
    }


}
