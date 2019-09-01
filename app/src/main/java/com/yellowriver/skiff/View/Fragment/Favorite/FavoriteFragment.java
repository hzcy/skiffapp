package com.yellowriver.skiff.View.Fragment.Favorite;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
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
 * 收藏tab
 *
 * @author huang
 */
public class FavoriteFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tl_search)
    SkinSlidingTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.main)
    CoordinatorLayout main;

    /**
     * 绑定控件
     */
    private Unbinder bind;

    /**
     * tablayout和viewpage相关
     */
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;


    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        setHasOptionsMenu(true);
        //加载视图
        bindView(rootView);
        bindData();
        return rootView;
    }

    private void bindView(View v) {
        bind = ButterKnife.bind(this, v);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.favorite));
    }

    private void bindData() {
        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(() -> {
            tabIndicators = SQLModel.getInstance().getGroupfromFavorite();
            getActivity().runOnUiThread(() -> updateView());
        });
    }

    /**
     * 设置tablayout
     */
    private void updateView() {
        if (tabFragments != null) {
            tabFragments.clear();
        }
        tabFragments = new ArrayList<>();
        for (String title : tabIndicators) {
            tabFragments.add(FavoriteDataFragment.newInstance(title));
        }
        FragmentAdapter contentAdapter = new FragmentAdapter(getChildFragmentManager(),tabIndicators,tabFragments);
        mViewPager.setAdapter(contentAdapter);
        mTabLayout.setViewPager(mViewPager);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (bind!=null) {
            //解除绑定
            bind.unbind();
        }
    }

}
