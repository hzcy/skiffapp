package com.yellowriver.skiff.View.Fragment.Home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Activity.SearchActivity;
import com.lapism.searchview.Search;
import com.lapism.searchview.widget.SearchView;

import org.angmarch.views.NiceSpinner;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * 首页.
 *
 * @author huang
 */
public class HomeViewFragment extends Fragment {
    /**
     * 日志TAG.
     */
    private static final String TAG = "HomeViewFragment";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner)
    NiceSpinner mSpinner;
    @BindView(R.id.searchBar)
    SearchView mSearchView;
    @BindView(R.id.ll_searchview)
    LinearLayout mllserarchView;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    /**
     * 分组名称.
     */
    private String qzGroupName;
    /**
     * 源类型.
     */
    private String qzSourcesType;
    /**
     * 搜索关键字.
     */
    private String qzQuery;
    /**
     * 下拉框当前index.
     */
    private int qzSpinnerSel;
    /**
     * 绑定控件
     */
    private Unbinder bind;
    /**
     * 默认构造方法.
     */
    public HomeViewFragment() {

    }

    private void updateView(final List<String> groupNameList) {
        //如果数据库为空
        if (groupNameList.size() == 0) {
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setTitle("首页");
            mAppBarLayout.setElevation(10);
            mllserarchView.setVisibility(View.GONE);

        } else {
            mAppBarLayout.setElevation(0);
            qzSourcesType = "home";
            //将数据设置ArrayAdapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    Objects.requireNonNull(
                            getContext()), R.layout.myspinner, groupNameList);
            adapter.setDropDownViewResource(R.layout.myspinner);
            mSpinner.setAdapter(adapter);
            mSpinner.setBackgroundResource(R.color.colorPrimary);
            mSpinner.setSelectedIndex(0);
            qzGroupName = (String) mSpinner.getItemAtPosition(0);
            qzQuery = Objects.requireNonNull(mSearchView.getQuery()).toString();
            //加载数据
            showFragment(HomeTabFragment.getInstance(
                    qzGroupName, qzSourcesType, qzQuery, mSpinner.getSelectedIndex()
            ));
        }
    }

    /**
     * 缓存Fragment view.
     */
    @Override
    public View onCreateView(
            final @NotNull LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.fragment_home_view, null);
        bindView(rootView);
        bindData();
        bindEvent();
        return rootView;
    }


    @SuppressLint("ResourceAsColor")
    private void bindView(final View v) {
        bind = ButterKnife.bind(this, v);
        mSearchView.setShape(Search.Shape.CLASSIC);
        mSearchView.setShadow(false);
        mSearchView.setShadowColor(R.color.colorPrimary);
        mSearchView.setHint(R.string.search_hint);
    }


    private void bindData() {
        SQLiteUtils.getInstance().getDaoSession()
                .startAsyncSession().runInTx(() -> {
            List<String> groupNameList = SQLModel
                    .getInstance().getGroup();
            getActivity().runOnUiThread(() -> updateView(groupNameList));
        });
    }




    private void bindEvent()
    {
        spinnerSelected();
        searchView();
    }

    //下拉栏选择事件
    private void spinnerSelected()
    {
        mSpinner.setOnSpinnerItemSelectedListener(
                (parent, view, position, id) -> {
                    qzGroupName = (String) parent.getItemAtPosition(position);
                    qzSpinnerSel = position;
                    showFragment(HomeTabFragment.
                            getInstance(qzGroupName, qzSourcesType, qzQuery, qzSpinnerSel));
                    //隐藏输入法
                    if (Objects.requireNonNull(getActivity())
                            .getCurrentFocus() != null) {
                        ((InputMethodManager) Objects.requireNonNull(
                                getActivity().getSystemService(
                                        INPUT_METHOD_SERVICE)))
                                .hideSoftInputFromWindow(Objects.requireNonNull(
                                        getActivity().getCurrentFocus()).
                                                getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                });
    }

    //搜索框点击事件
    private void searchView()
    {
        mSearchView.setOnQueryTextListener(
                new Search.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(final CharSequence query) {
                        Intent intent = new Intent(getActivity(),
                                SearchActivity.class);
                        intent.putExtra("qzGroupName", qzGroupName);
                        intent.putExtra("qzQuery", query);
                        startActivity(intent);
                        //隐藏输入法
                        if (Objects.requireNonNull(getActivity())
                                .getCurrentFocus() != null) {
                            ((InputMethodManager) Objects.requireNonNull(
                                    getActivity().getSystemService(
                                            INPUT_METHOD_SERVICE)))
                                    .hideSoftInputFromWindow(Objects.requireNonNull(
                                            getActivity().getCurrentFocus()).
                                                    getWindowToken(),
                                            InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        return false;
                    }

                    @Override
                    public void onQueryTextChange(final CharSequence newText) {
                        //当没有输入任何内容的时候清除结果，看实际需求
                    }
                });
    }

    //切换fragment
    private void showFragment(final Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    /**
     * 保存状态.
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(final @NotNull Bundle outState) {
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
    /**
     * 生命周期.
     *
     * @param savedInstanceState
     */
    @Override
    public void onViewStateRestored(final @Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * 生命周期.
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 生命周期.
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 生命周期.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 生命周期.
     */
    @Override
    public void onResume() {
        super.onResume();
    }


}
