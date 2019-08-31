package com.yellowriver.skiff.View.Fragment.About;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Activity.Other.AboutWebViewActivity;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 开源许可
 *
 * @author huang
 */
public class LicenseFragment extends Fragment {
    private static final String TAG = "LicenseFragment";
    @BindView(R.id.greenDAO)
    CardView greenDAO;
    @BindView(R.id.Glide)
    CardView Glide;
    @BindView(R.id.okhttp)
    CardView okhttp;
    @BindView(R.id.AgentWeb)
    CardView AgentWeb;
    @BindView(R.id.Androidskinsupport)
    CardView Androidskinsupport;
    @BindView(R.id.gson)
    CardView gson;
    @BindView(R.id.fastjson)
    CardView fastjson;
    @BindView(R.id.EventBus)
    CardView EventBus;
    @BindView(R.id.jsoup)
    CardView jsoup;
    @BindView(R.id.JsoupXpath)
    CardView JsoupXpath;
    @BindView(R.id.HtmlUnitAndroid)
    CardView HtmlUnitAndroid;
    @BindView(R.id.nicespinner)
    CardView nicespinner;
    @BindView(R.id.MaterialSearchView)
    CardView MaterialSearchView;
    @BindView(R.id.butterknife)
    CardView butterknife;
    @BindView(R.id.Alerter)
    CardView Alerter;
    @BindView(R.id.BaseRecyclerViewAdapterHelper)
    CardView BaseRecyclerViewAdapterHelper;
    @BindView(R.id.ColorPicker)
    CardView ColorPicker;
    @BindView(R.id.AndroidIconics)
    CardView AndroidIconics;
    @BindView(R.id.androidrss)
    CardView androidrss;

    public LicenseFragment() {
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
            mRootView = inflater.inflate(R.layout.fragment_license, container, false);
            bind = ButterKnife.bind(this, mRootView);

            //加载视图
            bindView(mRootView);

        } else {
            Log.d(TAG, "测试-->使用旧view");
        }

        return mRootView;
    }

    private void bindView(View mRootView) {

//        ImageView version = mRootView.findViewById(R.id.version);
//
//        version.setImageResource(FontAwesome.Icon.faw_android);

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


    @OnClick({R.id.greenDAO, R.id.Glide, R.id.okhttp, R.id.AgentWeb, R.id.Androidskinsupport, R.id.gson, R.id.fastjson, R.id.EventBus, R.id.jsoup, R.id.JsoupXpath, R.id.HtmlUnitAndroid, R.id.nicespinner, R.id.MaterialSearchView, R.id.butterknife, R.id.Alerter, R.id.BaseRecyclerViewAdapterHelper, R.id.ColorPicker, R.id.AndroidIconics, R.id.androidrss})
    public void onViewClicked(View view) {
        String qzLink = "";
        String qzTitle = "";
        switch (view.getId()) {
            case R.id.greenDAO:
                qzLink = "https://github.com/greenrobot/greenDAO";
                qzTitle = "greenDAO";
                break;
            case R.id.Glide:
                qzLink = "https://github.com/bumptech/glide";
                qzTitle = "Glide";
                break;
            case R.id.okhttp:
                qzLink = "https://github.com/square/okhttp";
                qzTitle = "okhttp";
                break;
            case R.id.AgentWeb:
                qzLink = "https://github.com/Justson/AgentWeb";
                qzTitle = "AgentWeb";
                break;
            case R.id.Androidskinsupport:
                qzLink = "https://github.com/ximsfei/Android-skin-support";
                qzTitle = "Android-skin-support";
                break;
            case R.id.gson:
                qzLink = "https://github.com/google/gson";
                qzTitle = "gson";
                break;
            case R.id.fastjson:
                qzLink = "https://github.com/alibaba/fastjson";
                qzTitle = "fastjson";
                break;
            case R.id.EventBus:
                qzLink = "https://github.com/greenrobot/EventBus";
                qzTitle = "EventBus";
                break;
            case R.id.jsoup:
                qzLink = "https://github.com/jhy/jsoup";
                qzTitle = "jsoup";
                break;
            case R.id.JsoupXpath:
                qzLink = "https://github.com/zhegexiaohuozi/JsoupXpath";
                qzTitle = "JsoupXpath";
                break;
            case R.id.HtmlUnitAndroid:
                qzLink = "https://github.com/null-dev/HtmlUnit-Android";
                qzTitle = "HtmlUnit-Android";
                break;
            case R.id.nicespinner:
                qzLink = "https://github.com/arcadefire/nice-spinner";
                qzTitle = "nice-spinner";
                break;
            case R.id.MaterialSearchView:
                qzLink = "https://github.com/lapism/MaterialSearchView";
                qzTitle = "MaterialSearchView";
                break;
            case R.id.butterknife:
                qzLink = "https://github.com/JakeWharton/butterknife";
                qzTitle = "butterknife";
                break;
            case R.id.Alerter:
                qzLink = "https://github.com/Tapadoo/Alerter";
                qzTitle = "Alerter";
                break;
            case R.id.BaseRecyclerViewAdapterHelper:
                qzLink = "https://github.com/CymChad/BaseRecyclerViewAdapterHelper";
                qzTitle = "BaseRecyclerViewAdapterHelper";
                break;
            case R.id.ColorPicker:
                qzLink = "https://github.com/jaredrummler/ColorPicker";
                qzTitle = "ColorPicker";
                break;
            case R.id.AndroidIconics:
                qzLink = "https://github.com/mikepenz/Android-Iconics";
                qzTitle = "Android-Iconics";
                break;
            case R.id.androidrss:
                qzLink = "https://github.com/ahorn/android-rss";
                qzTitle = "android-rss";
                break;
            default:
                break;
        }
        if (!"".equals(qzLink) && !"".equals(qzTitle)) {
            Intent intent = new Intent(getActivity(), AboutWebViewActivity.class);
            intent.putExtra("qzLink", qzLink);
            intent.putExtra("qzTitle", qzTitle);
            startActivity(intent);
        }
    }
}
