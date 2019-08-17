package com.yellowriver.skiff.View.Fragment.Sources;


import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yellowriver.skiff.Adapter.ViewPageAdapter.ContentPagerAdapter;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.JsonUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.RSSUtils;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.Model.SourceDataSource;
import com.yellowriver.skiff.R;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import skin.support.flycotablayout.widget.SkinSlidingTabLayout;

import static com.yellowriver.skiff.R.drawable.ic_more_vert_black_24dp;

/**
 * 源管理
 *
 * @author huang
 */
public class SourceFragment extends Fragment {
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
    /**
     * 绑定控件
     */
    private Unbinder bind;
    /**
     * tablayout和viewpage相关
     */
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sourcessetting, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_netImport:
                showDialogAddHttp();
                return true;
            case R.id.action_clipboardimport:
                showDialogAddJSON();
                return true;
            case R.id.action_rssimport:
                showDialogAddRss();
                return true;
            default:
                return false;
        }
    }

    public SourceFragment() {
        // Required empty public constructor
    }

    View mRootView;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_source, container, false);
            bind = ButterKnife.bind(this, mRootView);
            //加载视图
            bindView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    SourceDataViewFragment sourceDataViewFragment;
    ContentPagerAdapter contentAdapter;

    private void bindView(View v) {
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        mToolbar.setTitle(getString(R.string.sourcesSet));
        mToolbar.setOverflowIcon(getResources().getDrawable(ic_more_vert_black_24dp));

        tabIndicators = new ArrayList<>();
        tabIndicators.add("本地源");
        tabIndicators.add("源市场");
        sourceDataViewFragment = SourceDataViewFragment.getInstance("本地源", "");
        SourceDataViewFragment sourceDataViewFragment2 = SourceDataViewFragment.getInstance("源市场", "");
        tabFragments = new ArrayList<>();
        tabFragments.add(sourceDataViewFragment);
        tabFragments.add(sourceDataViewFragment2);
        contentAdapter = new ContentPagerAdapter(getChildFragmentManager(), tabIndicators, tabFragments);
        mViewPager.setAdapter(contentAdapter);
        sourceDataViewFragment.setPageAdapter(contentAdapter);
        sourceDataViewFragment2.setPageAdapter(contentAdapter);
        mTabLayout.setViewPager(mViewPager);
    }

    /**
     * 添加成功后 重新加载本地源
     */
    private void updateLocalSource() {
        contentAdapter.notifyDataSetChanged();
    }

    private void loadSql(String newtitle, String newtype, HomeEntity homeEntity) {
        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(new Runnable() {
            @Override
            public void run() {
                List<HomeEntity> homeEntityList = SQLModel.getInstance().getXpathbyTitle(newtitle, newtype);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: "+homeEntityList.isEmpty());
                        if (!homeEntityList.isEmpty()) {
                            SnackbarUtil.ShortSnackbar(getView(),"源已经存在了！",SnackbarUtil.Warning).show();

                        }else{

                            long addresult = SQLModel.getInstance().addSouce(homeEntity);
                            if (addresult >= 0) {
                                SnackbarUtil.ShortSnackbar(getView(),"源添加成功！",SnackbarUtil.Confirm).show();

                                //保存修改了数据的状态 当这里修改成功 首页要重新加载
                                SharedPreferencesUtils.dataChange(true, getContext());
                                updateLocalSource();
                            } else {
                                SnackbarUtil.ShortSnackbar(getView(),"源添加失败！",SnackbarUtil.Warning).show();

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
        new AlertDialog.Builder(Objects.requireNonNull(getContext())).setTitle("剪贴板导入")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    if (TextUtils.isEmpty(sourceadder.getText())) {
                        SnackbarUtil.ShortSnackbar(getView(),"请粘贴JSon格式的源！！",SnackbarUtil.Warning).show();

                    } else {
                        String json = sourceadder.getText().toString();
                        new Thread(() -> {
                            //String group = sourcegroup.getText().toString();
                            if (JsonUtils.isJSONValid(json)) {
                                Log.d(TAG, "showDialogAddJSON: 是json");
                                //JSON 轻舟源
                                Type type = new TypeToken<HomeEntity>() {
                                }.getType();
                                HomeEntity homeEntity = gson.fromJson(json, type);
                                if (homeEntity != null) {
                                    Log.d(TAG, "showDialogAddJSON: 不为空");
                                    String newtitle = homeEntity.getTitle();
                                    String newtype = homeEntity.getType();
                                    loadSql(newtitle, newtype, homeEntity);
                                } else {
                                    SnackbarUtil.ShortSnackbar(getView(),"源格式无效，无法导入！",SnackbarUtil.Warning).show();

                                }
                            } else {
                                SnackbarUtil.ShortSnackbar(getView(),"不是有效JSon格式！",SnackbarUtil.Warning).show();


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
        new AlertDialog.Builder(Objects.requireNonNull(getContext())).setTitle("导入RSS源(测试功能)")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    if (TextUtils.isEmpty(sourceadder.getText()) || TextUtils.isEmpty(sourcegroup.getText())) {
                        SnackbarUtil.ShortSnackbar(getView(),"请输入RSS源地址或给分组取个名！",SnackbarUtil.Warning).show();

                    } else {
                        new Thread(() -> {
                            String json = sourceadder.getText().toString();
                            String group = sourcegroup.getText().toString();
                            Log.d(TAG, "run: RSS");
                            //RSS
                            boolean add = RSSUtils.insertRSS(json, group);
                            if (add) {
                                SnackbarUtil.ShortSnackbar(getView(),"RSS源添加成功！",SnackbarUtil.Confirm).show();


                                SharedPreferencesUtils.dataChange(true, getContext());
                            } else {
                                SnackbarUtil.ShortSnackbar(getView(),"RSS源添加失败！",SnackbarUtil.Warning).show();


                            }
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
        new AlertDialog.Builder(Objects.requireNonNull(getContext())).setTitle("网络导入")
                .setView(view)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    if (TextUtils.isEmpty(sourceadder.getText())) {
                        SnackbarUtil.ShortSnackbar(getView(),"请输入源网络地址！",SnackbarUtil.Warning).show();


                    } else if (!sourceadder.getText().toString().startsWith(HTTP)) {
                        SnackbarUtil.ShortSnackbar(getView(),"请输入网络URL！",SnackbarUtil.Warning).show();


                    } else {
                        String url = sourceadder.getText().toString();
                        new Thread(() -> {
                            boolean isAdd = SourceDataSource.getInstance().addSource(url);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (isAdd) {
                                        updateLocalSource();
                                    } else {
                                        SnackbarUtil.ShortSnackbar(getView(),"源添加失败！",SnackbarUtil.Warning).show();

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "测试-->onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        Log.d(TAG, "测试-->onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }


}