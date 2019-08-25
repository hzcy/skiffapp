package com.yellowriver.skiff.View.Fragment.About;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Activity.Other.SettingsActivity;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 关于
 *
 * @author huang
 */
public class AboutFragment extends Fragment {
    private static final String TAG = "AboutFragment";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.setting)
    LinearLayout setting;

    /**
     * 绑定控件
     */
    private Unbinder bind;


    public AboutFragment() {
        // Required empty public constructor
    }


    View mRootView;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "测试-->onCreateView: ");

        if (mRootView == null) {

            Log.d(TAG, "测试-->新加载view");
            mRootView = inflater.inflate(R.layout.fragment_about, container, false);
            bind = ButterKnife.bind(this, mRootView);

            //加载视图
            bindView(mRootView);

        } else {
            Log.d(TAG, "测试-->使用旧view");
        }

        return mRootView;
    }

    private void bindView(View mRootView) {
        mToolbar.setTitle("关于");
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new About2Fragment())
                .commit();
    }

    /**
     * 销毁.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "测试-->onDestroy");
        super.onDestroy();
        //解除绑定
        bind.unbind();

    }

    @Override
    public void onStop() {
        Log.d(TAG, "测试-->onStop");
        super.onStop();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "测试-->onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "测试-->onResume");
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "测试-->onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "测试-->onDestroyView");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "测试-->onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }


    @OnClick(R.id.setting)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }
}
