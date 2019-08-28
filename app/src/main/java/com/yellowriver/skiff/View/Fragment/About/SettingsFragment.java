package com.yellowriver.skiff.View.Fragment.About;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.Help.LocalBackup;

import com.yellowriver.skiff.R;

import org.jetbrains.annotations.NotNull;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import skin.support.SkinCompatManager;



/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private static final String TAG = "SettingsFragment";
    @BindView(R.id.theme)
    LinearLayout theme;
    @BindView(R.id.loadsetting)
    LinearLayout loadsetting;
    @BindView(R.id.backup)
    LinearLayout backup;
    @BindView(R.id.recover)
    LinearLayout recover;
    @BindView(R.id.themeText)
    TextView themeText;
    @BindView(R.id.loadText)
    TextView loadText;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    public SettingsFragment() {
        // Required empty public constructor
    }

    LocalBackup localBackup;

    View mRootView;
    /**
     * 绑定控件
     */
    private Unbinder bind;

    private String sourcesjson;
    private String fovitejson;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "测试-->onCreateView: ");

        if (mRootView == null) {

            Log.d(TAG, "测试-->新加载view");
            mRootView = inflater.inflate(R.layout.fragment_settings, container, false);
            bind = ButterKnife.bind(this, mRootView);

            //加载视图
            bindView(mRootView);

        } else {
            Log.d(TAG, "测试-->使用旧view");
        }

        return mRootView;
    }

    AlertDialog.Builder builder;

    private void bindView(View mRootView) {

        themeText.setText(SharedPreferencesUtils.themeRead(getContext()));
        loadText.setText(SharedPreferencesUtils.listLoadAnimationRead(getContext()));
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


    @OnClick({R.id.theme, R.id.loadsetting, R.id.backup, R.id.recover})
    public void onViewClicked(View view) {
        LocalBackup localBackup = new LocalBackup(getContext());
        switch (view.getId()) {
            case R.id.theme:

                themeSel();
                break;
            case R.id.loadsetting:
                loadSel();

                break;
            case R.id.backup:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                    } else {
                        sourcesjson = SQLiteUtils.getInstance().getAllHome();
                        fovitejson = SQLiteUtils.getInstance().getAllFavorite();
                        localBackup.backup(sourcesjson, fovitejson);
                    }
                } else {
                    sourcesjson = SQLiteUtils.getInstance().getAllHome();
                    fovitejson = SQLiteUtils.getInstance().getAllFavorite();
                    localBackup.backup(sourcesjson, fovitejson);
                }


                break;
            case R.id.recover:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                    } else {

                        localBackup.resume();
                    }
                } else {
                    localBackup.resume();
                }

                SharedPreferencesUtils.dataChange(true, getContext());

                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);

            }
        }
    }

    private void themeSel() {
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择颜色");
        builder.setItems(new String[]{"默认蓝", "绿色", "红色", "粉色", "青色", "蓝色", "橙色", "紫色", "棕色", "灰色", "黑色"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        //Log.d(TAG, "onClick: lvse");
                        SkinCompatManager.getInstance().loadSkin("zhihulan.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "默认蓝");
                        dialogInterface.dismiss();
                        break;
                    case 1:
                        //Log.d(TAG, "onClick: red");
                        SkinCompatManager.getInstance().loadSkin("kuanlv.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "绿色");
                        dialogInterface.dismiss();
                        break;
                    case 2:
                        //Log.d(TAG, "onClick: shuiya");
                        SkinCompatManager.getInstance().loadSkin("yimahong.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "红色");
                        dialogInterface.dismiss();
                        break;
                    case 3:
                        //Log.d(TAG, "onClick: lanse");
                        SkinCompatManager.getInstance().loadSkin("bilifen.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "粉色");
                        dialogInterface.dismiss();
                        break;
                    case 4:
                        //Log.d(TAG, "onClick: night");
                        SkinCompatManager.getInstance().loadSkin("shuiyaqing.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "青色");
                        dialogInterface.dismiss();
                        break;
                    case 5:
                        //Log.d(TAG, "onClick: night");
                        SkinCompatManager.getInstance().loadSkin("yitilan.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "蓝色");
                        dialogInterface.dismiss();
                        break;
                    case 6:
                        Log.d(TAG, "onClick: night");
                        SkinCompatManager.getInstance().loadSkin("yitencheng.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "橙色");
                        dialogInterface.dismiss();
                        break;
                    case 7:
                        //Log.d(TAG, "onClick: night");
                        SkinCompatManager.getInstance().loadSkin("jilaozi.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "紫色");
                        dialogInterface.dismiss();
                        break;
                    case 8:
                        //Log.d(TAG, "onClick: night");
                        SkinCompatManager.getInstance().loadSkin("gutongzhong.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "棕色");
                        dialogInterface.dismiss();
                        break;
                    case 9:
                        //Log.d(TAG, "onClick: night");
                        SkinCompatManager.getInstance().loadSkin("didiaohui.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "灰色");
                        dialogInterface.dismiss();
                        break;
                    case 10:
                        //Log.d(TAG, "onClick: night");
                        SkinCompatManager.getInstance().loadSkin("gaoduanhei.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                        SharedPreferencesUtils.themeWrite(getContext(), "黑色");
                        dialogInterface.dismiss();
                        break;
                    default:
                        break;
                }
                themeText.setText(SharedPreferencesUtils.themeRead(getContext()));
            }
        });
        builder.show();
    }


    private void loadSel() {
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("选择动画");
        builder.setItems(new String[]{"默认无", "渐显", "缩放", "从下到上", "从左到右", "从右到左", "每次随机"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        //无
                        SharedPreferencesUtils.listLoadAnimationWrite(getContext(), "默认无");
                        dialogInterface.dismiss();
                        break;
                    case 1:
                        //渐显
                        SharedPreferencesUtils.listLoadAnimationWrite(getContext(), "渐显");

                        dialogInterface.dismiss();
                        break;
                    case 2:
                        //缩放
                        SharedPreferencesUtils.listLoadAnimationWrite(getContext(), "缩放");

                        dialogInterface.dismiss();
                        break;
                    case 3:
                        //从下到上
                        SharedPreferencesUtils.listLoadAnimationWrite(getContext(), "从下到上");

                        dialogInterface.dismiss();
                        break;
                    case 4:
                        //从左到右
                        SharedPreferencesUtils.listLoadAnimationWrite(getContext(), "从左到右");

                        dialogInterface.dismiss();
                        break;
                    case 5:
                        //从右到左
                        SharedPreferencesUtils.listLoadAnimationWrite(getContext(), "从右到左");

                        dialogInterface.dismiss();
                        break;
                    case 6:
                        //每次随机
                        SharedPreferencesUtils.listLoadAnimationWrite(getContext(), "每次随机");

                        dialogInterface.dismiss();
                        break;

                    default:
                        break;
                }
                loadText.setText(SharedPreferencesUtils.listLoadAnimationRead(getContext()));
            }
        });
        builder.show();
    }


}
