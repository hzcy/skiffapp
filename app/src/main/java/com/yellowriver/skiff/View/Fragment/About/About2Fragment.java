package com.yellowriver.skiff.View.Fragment.About;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Activity.Other.LicenseActivity;

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
public class About2Fragment extends Fragment {
    private static final String TAG = "About2Fragment";
    @BindView(R.id.update)
    LinearLayout update;
    @BindView(R.id.changelog)
    LinearLayout changelog;
    @BindView(R.id.homepage)
    LinearLayout homepage;
    @BindView(R.id.share)
    LinearLayout share;
    @BindView(R.id.license)
    LinearLayout license;
    @BindView(R.id.qqgroup)
    LinearLayout qqgroup;
    @BindView(R.id.weixingroup)
    LinearLayout weixingroup;
    @BindView(R.id.weixingzh)
    LinearLayout weixingzh;
    @BindView(R.id.mail)
    LinearLayout mail;
    @BindView(R.id.alipayhb)
    LinearLayout alipayhb;
    @BindView(R.id.alipayjz)
    LinearLayout alipayjz;
    @BindView(R.id.qqjz)
    LinearLayout qqjz;
    @BindView(R.id.weixinjz)
    LinearLayout weixinjz;
    @BindView(R.id.disclaimer)
    LinearLayout disclaimer;


    public About2Fragment() {
        // Required empty public constructor
    }

    View mRootView;
    /**
     * 绑定控件
     */
    private Unbinder bind;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "测试-->onCreateView: ");

        if (mRootView == null) {

            Log.d(TAG, "测试-->新加载view");
            mRootView = inflater.inflate(R.layout.fragment_about2, container, false);
            bind = ButterKnife.bind(this, mRootView);

            //加载视图
            bindView(mRootView);

        } else {
            Log.d(TAG, "测试-->使用旧view");
        }

        return mRootView;
    }

    private void bindView(View mRootView) {

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


    @OnClick(R.id.license)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), LicenseActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.update, R.id.changelog, R.id.homepage, R.id.share, R.id.qqgroup, R.id.weixingroup, R.id.weixingzh, R.id.mail, R.id.alipayhb, R.id.alipayjz, R.id.qqjz, R.id.weixinjz, R.id.disclaimer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.update:
                break;
            case R.id.changelog:
                break;
            case R.id.homepage:
                break;
            case R.id.share:
                break;
            case R.id.qqgroup:
                break;
            case R.id.weixingroup:
                break;
            case R.id.weixingzh:
                break;
            case R.id.mail:
                break;
            case R.id.alipayhb:
                break;
            case R.id.alipayjz:
                break;
            case R.id.qqjz:
                break;
            case R.id.weixinjz:
                break;
            case R.id.disclaimer:
                break;
            default:
                break;
        }
    }
}
