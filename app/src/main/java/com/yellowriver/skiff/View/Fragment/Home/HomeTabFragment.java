package com.yellowriver.skiff.View.Fragment.Home;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.yellowriver.skiff.Adapter.ViewPageAdapter.ContentPagerAdapter;
import com.yellowriver.skiff.Adapter.ViewPageAdapter.FragmentAdapter;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import skin.support.flycotablayout.widget.SkinSlidingTabLayout;


/**
 * TabFragment
 *
 * @author huang
 */
public class HomeTabFragment extends Fragment {
    private static final String TAG = "HomeTabFragment";
    private static final String KONG = "void";

    @BindView(R.id.tl_search)
    SkinSlidingTabLayout mTabLayout;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    /**
     * tablayout和viewpage相关
     */
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    /**
     * 源分组名称
     */
    private String qzGroupName;
    /**
     * 源类型
     */
    private String qzSourcesType;
    /**
     * 搜索关键字
     */
    private String qzQuery;
    /**
     * 当前下拉框选择index
     */
    private int qzSpinnerSel;

    /**
     * 绑定控件
     */
    private Unbinder bind;


    public static HomeTabFragment getInstance(String qzGroupName, String qzSourcesType, String qzQuery, int qzSpinnerSel) {
        HomeTabFragment homeTabFragment = new HomeTabFragment();
        Bundle args = new Bundle();
        args.putString("qzGroupName", qzGroupName);
        args.putString("qzSourcesType", qzSourcesType);
        args.putString("qzQuery", qzQuery);
        args.putInt("qzSpinnerSel", qzSpinnerSel);
        homeTabFragment.setArguments(args);
        return homeTabFragment;
    }

    public HomeTabFragment() {
        // Required empty public constructor
    }

    /**
     * 缓存Fragment view
     */
    private View rootView;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_tab, null);
            bindView(rootView);
            bindData();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }


    @SuppressLint("ResourceType")
    private void bindView(View v) {
        bind = ButterKnife.bind(this, v);
    }

    private void bindData() {
        qzGroupName = requireArguments().getString("qzGroupName");
        qzSourcesType = getArguments().getString("qzSourcesType");
        qzQuery = getArguments().getString("qzQuery");
        qzSpinnerSel = getArguments().getInt("qzSpinnerSel");
        //数据为空视图
        if (KONG.equals(qzSourcesType)) {
            mAppBarLayout.setVisibility(View.GONE);
        } else {
            mAppBarLayout.setVisibility(View.VISIBLE);
            loadSqlData();
        }
    }

    private void loadSqlData() {
        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(() -> {
            tabIndicators = SQLModel.getInstance().getTitleByGroup(qzGroupName, qzSourcesType);
            getActivity().runOnUiThread(() -> initContent());
        });
    }
    FragmentAdapter contentAdapter;
    private void initContent() {
        if (tabFragments != null) {
            tabFragments.clear();
        }
        tabFragments = new ArrayList<>();
        for (String qzSourceName : tabIndicators) {
            //第一步
            tabFragments.add(HomeDataViewFragment.newInstance(qzGroupName, qzSourceName, "1", "", qzSourcesType, qzQuery, qzSpinnerSel, "", -1));
        }
        try {
            contentAdapter = new FragmentAdapter(getChildFragmentManager(), tabIndicators, tabFragments);

        }catch (IllegalStateException e){

        }
        if (contentAdapter!=null) {
            mViewPager.setAdapter(contentAdapter);
            //mViewPager.setOffscreenPageLimit(tabFragments.size());
            mTabLayout.setViewPager(mViewPager);
        }
    }


    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /**
     * 销毁.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "测试-->onDestroy");
        super.onDestroy();
        if (bind!=null) {
            //解除绑定
            bind.unbind();
        }
    }

}
