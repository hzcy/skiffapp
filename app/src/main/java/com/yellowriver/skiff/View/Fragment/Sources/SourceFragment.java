package com.yellowriver.skiff.View.Fragment.Sources;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.yellowriver.skiff.Adapter.ViewPageAdapter.ContentPagerAdapter;
import com.yellowriver.skiff.Adapter.ViewPageAdapter.FragmentAdapter;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.SourcesBean.GroupEntity;
import com.yellowriver.skiff.Bean.SourcesBean.SourcesEntity;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.JsonUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.NetUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.RSSUtils;
import com.yellowriver.skiff.Help.LocalBackup;
import com.yellowriver.skiff.Help.LogUtil;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.Model.SourceDataSource;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Activity.MyImportActivity;
import com.yellowriver.skiff.View.Fragment.Home.HomeDataViewFragment;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import skin.support.flycotablayout.widget.SkinSlidingTabLayout;

import static com.yellowriver.skiff.R.drawable.ic_more_vert_black_24dp;

/**
 * 源管理
 *
 * @author huang
 */
public class SourceFragment extends Fragment {
    String baseurl = "https://skiff-d3a.pages.dev/api/sources";
    private static final String HTTP = "http";
    private static final String TAG = "SourceFragment";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tl_search)
    SkinSlidingTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.main)
    CoordinatorLayout main;
    @BindView(R.id.addsetting)
    LinearLayout addsetting;
    /**
     * 绑定控件
     */
    private Unbinder bind;
    /**
     * tablayout和viewpage相关
     */
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;


    public SourceFragment() {
        // Required empty public constructor
    }

    View mRootView;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_source, null);
            //加载视图
            bind = ButterKnife.bind(this, mRootView);
            mToolbar.setTitle(getString(R.string.sourcesSet));
            bindView();
            bindData();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    FragmentAdapter contentAdapter;

    private static Gson gson = new Gson();

    @SuppressLint("ResourceType")
    private void bindView() {

    }



    private void bindData() {


        tabIndicators = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {

                String json = NetUtils.getInstance().getRequest(baseurl + "/groupList.json", "1");

                List< GroupEntity > sourceBean = null;
                if (JsonUtils.isJSONValid(json)) {
                    // Log.d(TAG, "getSourceAllGroup: 是json");
                    Type type = new TypeToken<List<GroupEntity>>() {
                    }.getType();
                    sourceBean = gson.fromJson(json, type);
                    if(sourceBean!=null) {
                        for (int i = 0; i < sourceBean.size(); i++) {
                            tabIndicators.add(sourceBean.get(i).getName());
                        }
                        getActivity().runOnUiThread(() -> initContent());
                    }
                }
            }
        }).start();



    }

    private void initContent() {
        if (tabFragments != null) {
            tabFragments.clear();
        }
        tabFragments = new ArrayList<>();
        for (String qzGroupName : tabIndicators) {
            //第一步
            tabFragments.add(RemoteSourceFragment.getInstance(qzGroupName));
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



    private void loadSql(String newtitle, String newtype, HomeEntity homeEntity) {
        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(new Runnable() {
            @Override
            public void run() {
                List<HomeEntity> homeEntityList = SQLModel.getInstance().getXpathbyTitle(newtitle, newtype);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: " + homeEntityList.isEmpty());
                        if (!homeEntityList.isEmpty()) {
                            SnackbarUtil.ShortSnackbar(getView(), "源已经存在了！", SnackbarUtil.Warning).show();

                        } else {

                            long addresult = SQLModel.getInstance().addSouce(homeEntity);
                            if (addresult >= 0) {
                                SnackbarUtil.ShortSnackbar(getView(), "源添加成功！", SnackbarUtil.Confirm).show();
                                //重新加载源管理页面
                                bindView();
                                //保存修改了数据的状态 当这里修改成功 首页要重新加载
                                SharedPreferencesUtils.dataChange(true, getContext());


                            } else {
                                SnackbarUtil.ShortSnackbar(getView(), "源添加失败！", SnackbarUtil.Alert).show();

                            }

                        }
                    }
                });
            }
        });
    }

    private void showDialogAddJSON() {
        Gson gson = new Gson();
        View view = View.inflate(getContext(), R.layout.add_setting, null);
        EditText sourcegroup = view.findViewById(R.id.sourcegroup);
        EditText sourceadder = view.findViewById(R.id.sourceadder);
        sourcegroup.setVisibility(View.GONE);
        sourceadder.setHint("Json格式");
        //sourcegroup.setHint("分组");
        sourceadder.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //改变默认的单行模式
        sourceadder.setSingleLine(false);
        //水平滚动设置为False
        sourceadder.setHorizontallyScrolling(false);
        new AlertDialog.Builder(requireContext()).setTitle("剪贴板导入")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    if (TextUtils.isEmpty(sourceadder.getText())) {
                        SnackbarUtil.ShortSnackbar(getView(), "请粘贴JSon格式的源！！", SnackbarUtil.Warning).show();

                    } else {
                        String json = sourceadder.getText().toString();
                        new Thread(() -> {

                            if (JsonUtils.isJSONValid(json)) {
                                //JSON 轻舟源
                                Type type = new TypeToken<HomeEntity>() {
                                }.getType();
                                HomeEntity homeEntity = null;
                                try {
                                    homeEntity = gson.fromJson(json, type);
                                }catch (JsonSyntaxException e)
                                {

                                }catch (IllegalStateException e){

                                }

                                if (homeEntity != null) {
                                    String newtitle = homeEntity.getTitle();
                                    String newtype = homeEntity.getType();
                                    loadSql(newtitle, newtype, homeEntity);
                                } else {
                                    SnackbarUtil.ShortSnackbar(getView(), "源不匹配，无法导入！", SnackbarUtil.Warning).show();

                                }
                            } else {
                                SnackbarUtil.ShortSnackbar(getView(), "不是有效JSon格式！", SnackbarUtil.Warning).show();


                            }
                        }).start();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void showDialogAddRss() {
        View view = View.inflate(getContext(), R.layout.add_setting, null);
        EditText sourcegroup = view.findViewById(R.id.sourcegroup);
        EditText sourceadder = view.findViewById(R.id.sourceadder);
        // sourcegroup.setVisibility(View.GONE);
        sourceadder.setHint("RSS地址");
        sourcegroup.setHint("分组");
        sourceadder.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //改变默认的单行模式
        sourceadder.setSingleLine(false);
        //水平滚动设置为False
        sourceadder.setHorizontallyScrolling(false);
        new AlertDialog.Builder(requireContext()).setTitle("导入RSS源(测试功能)")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    ProgressDialog waitingDialog =
                            new ProgressDialog(getContext());
                    waitingDialog.setTitle("提示");
                    waitingDialog.setMessage("加载中...");
                    waitingDialog.setIndeterminate(true);
                    waitingDialog.setCancelable(false);
                    waitingDialog.show();
                    if (TextUtils.isEmpty(sourceadder.getText()) || TextUtils.isEmpty(sourcegroup.getText())) {
                        SnackbarUtil.ShortSnackbar(getView(), "请输入RSS源地址或给分组取个名！", SnackbarUtil.Warning).show();

                    } else {
                        new Thread(() -> {
                            String json = sourceadder.getText().toString();
                            String group = sourcegroup.getText().toString();
                            Log.d(TAG, "run: RSS");
                            //RSS
                            boolean add = RSSUtils.insertRSS(json, group);


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    waitingDialog.cancel();
                                    if (add) {

                                        SnackbarUtil.ShortSnackbar(getView(), "RSS源添加成功！", SnackbarUtil.Confirm).show();

                                        //重新加载源管理页面
                                        bindView();
                                        SharedPreferencesUtils.dataChange(true, getContext());
                                    } else {
                                        SnackbarUtil.ShortSnackbar(getView(), "RSS源添加失败！", SnackbarUtil.Warning).show();


                                    }
                                }
                            });


                        }).start();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void showDialogAddHttp() {
        View view = View.inflate(getContext(), R.layout.add_setting, null);
        EditText sourcegroup = view.findViewById(R.id.sourcegroup);
        EditText sourceadder = view.findViewById(R.id.sourceadder);
        sourcegroup.setVisibility(View.GONE);
        sourceadder.setHint("http://...");
        //sourcegroup.setHint("分组");
        sourceadder.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //改变默认的单行模式
        sourceadder.setSingleLine(false);
        //水平滚动设置为False
        sourceadder.setHorizontallyScrolling(false);
        new AlertDialog.Builder(requireContext()).setTitle("网络导入")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    if (TextUtils.isEmpty(sourceadder.getText())) {
                        SnackbarUtil.ShortSnackbar(getView(), "请输入源网络地址！", SnackbarUtil.Warning).show();


                    } else if (!sourceadder.getText().toString().startsWith(HTTP)) {
                        SnackbarUtil.ShortSnackbar(getView(), "请输入网络URL！", SnackbarUtil.Warning).show();


                    } else {
                        ProgressDialog waitingDialog =
                                new ProgressDialog(getContext());
                        waitingDialog.setTitle("提示");
                        waitingDialog.setMessage("加载中...");
                        waitingDialog.setIndeterminate(true);
                        waitingDialog.setCancelable(false);
                        waitingDialog.show();
                        String url = sourceadder.getText().toString();
                        new Thread(() -> {
                            boolean isAdd =false;
                            String json = NetUtils.getInstance().getRequest(url,"");
                            int addsum = 0;

                                if (url.indexOf("list") != -1) {
                                    Log.d(TAG, "showDialogAddHttp: 加");
                                    Gson gson = new Gson();
                                    String baseurl = "https://hege.gitee.io/api/sources";
                                    List<SourcesEntity> sourceBean = null;
                                    if (JsonUtils.isJSONValid(json)) {
                                        Type type = new TypeToken<List<SourcesEntity>>() {
                                        }.getType();
                                        sourceBean = gson.fromJson(json, type);
                                    }
                                    int addSize = 0;
                                    if (sourceBean != null) {
                                        Log.d(TAG, "showDialogAddHttp: 加" + sourceBean.size());
                                        for (SourcesEntity sourcesEntity : sourceBean) {
                                            if (!SQLModel.getInstance().getXpathbyTitle(sourcesEntity.getName(), sourcesEntity.getType()).isEmpty()) {
                                                //存在
                                            } else {
                                                String sourceLink = sourcesEntity.getLink();
                                                Log.d(TAG, "showDialogAddHttp: " + baseurl + sourceLink);
                                                if (sourceLink.startsWith("http")) {

                                                } else {
                                                    sourceLink = baseurl + sourceLink;
                                                }
                                                boolean add = SourceDataSource.getInstance().addSource(sourceLink);
                                                if (add) {
                                                    Log.d(TAG, "showDialogAddHttp: 添加成功" + sourceLink);
                                                    addSize++;
                                                } else {
                                                    Log.d(TAG, "showDialogAddHttp: 添加失败 已存在" + sourceLink);
                                                }
                                            }
                                        }
                                    } else {
                                        isAdd = false;
                                    }
                                    if (addSize > 0) {
                                        isAdd = true;
                                    }
                                } else {

                                    if(json!=null) {
                                        if (json.startsWith("[")) {

                                            Gson gson = new Gson();
                                            Type type = new TypeToken<List<HomeEntity>>() {
                                            }.getType();
                                            List<HomeEntity> homeEntityList = gson.fromJson(json, type);
                                            if (homeEntityList != null && homeEntityList.size() > 0) {
                                                //LogUtil.info("获取的json","json"+homeEntityList.size());
                                                for (HomeEntity homeEntity : homeEntityList) {
                                                    boolean a = SQLiteUtils.getInstance().addHome2(homeEntity);
                                                    if (a) {
                                                        isAdd = true;
                                                        addsum++;
                                                    }
                                                }
                                            }
                                        } else {
                                            isAdd = SourceDataSource.getInstance().addSource(url);
                                        }
                                    }else{
                                        isAdd = false;
                                    }

                            }
                            boolean finalIsAdd = isAdd;
                            int finalAddsum = addsum;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    waitingDialog.cancel();
                                    if (finalIsAdd) {
                                        //重新加载源管理页面
                                        bindView();
                                        if(finalAddsum != 0){
                                            SnackbarUtil.ShortSnackbar(getView(), "成功导入"+finalAddsum+"个源！", SnackbarUtil.Confirm).show();

                                        }
                                        SharedPreferencesUtils.dataChange(true, getContext());

                                    } else {
                                        SnackbarUtil.ShortSnackbar(getView(), "源添加失败！", SnackbarUtil.Warning).show();

                                    }
                                }
                            });
                        }).start();
                    }
                }).setNegativeButton("取消", null).show();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "测试-->onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }


    /**
     * 销毁.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "测试-->onDestroy");
        super.onDestroy();
        if (bind != null) {
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
        Log.d(TAG, "测试-->onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }


    @OnClick(R.id.addsetting)
    public void onViewClicked() {
        showBottomSheetDialog();
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = View.inflate(getContext(), R.layout.addsources_dialog, null);
        LinearLayout netImport = view.findViewById(R.id.netImport);
        LinearLayout clipboardimport = view.findViewById(R.id.clipboardimport);
        LinearLayout rssimport = view.findViewById(R.id.rssimport);
        LinearLayout myimport = view.findViewById(R.id.myimport);
        netImport.setOnClickListener(view1 -> {
            showDialogAddHttp();
            dialog.cancel();
        });
        clipboardimport.setOnClickListener(view12 -> {
            showDialogAddJSON();
            dialog.cancel();
        });
        rssimport.setOnClickListener(view13 -> {
            showDialogAddRss();
            dialog.cancel();
        });
        myimport.setOnClickListener(view13 -> {
            Intent intent = new Intent(getContext(), MyImportActivity.class);
            getContext().startActivity(intent);
            dialog.cancel();
        });
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }


}