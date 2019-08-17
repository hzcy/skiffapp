package com.yellowriver.skiff.View.Fragment.Sources;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yellowriver.skiff.Adapter.TreeAdapter.GroupAdapter;
import com.yellowriver.skiff.Adapter.ViewPageAdapter.ContentPagerAdapter;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.SourcesBean.group;
import com.yellowriver.skiff.Bean.SourcesBean.sources;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.SourceUtils;
import com.yellowriver.skiff.Help.MyLinearLayoutManager;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.Model.SourceDataSource;
import com.yellowriver.skiff.R;

import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 源管理 本地源和源市场
 *
 * @author huang
 * @date 2019
 */
public class SourceDataViewFragment extends Fragment {
    private static String TAG = "SourceDataViewFragment";
    private static String LOCAL = "本地源";
    private static String MARKET = "源市场";
    @BindView(R.id.results_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_tishi)
    TextView tvTishi;
    @BindView(R.id.ll_pbar)
    LinearLayout mLoading;
    @BindView(R.id.main)
    FrameLayout main;
    /**
     * 用着数据库查询
     */
    private String title;
    private GroupAdapter mGroupAdapter;
    private List<MultiItemEntity> list = new ArrayList<>();
    private int page = 1;
    private int sourcesCount = 0;
    private ContentPagerAdapter mAdapter;
    /**
     * 绑定控件
     */
    private Unbinder bind;
    public void setPageAdapter(ContentPagerAdapter adapter) {
        this.mAdapter = adapter;
    }

    private final Handler mHandler2 = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(@NotNull Message msg) {
            if (msg.what == 123) {
                Log.d(TAG, "handleMessage: " + list.size());
                mSwipeRefreshLayout.setRefreshing(false);
                //解决刷新时快速滑动导致cash
                if (page == 1) {
                    if (list != null) {
                        if (list.size() != 0) {

                            if (mAdapter != null) {
                                int i = 0;
                                if (LOCAL.equals(title)) {
                                    i = 0;
                                } else if (MARKET.equals(title)) {
                                    i = 1;
                                }
                                mAdapter.setPageTitle(i, title + "（" + sourcesCount + "）");
                            }
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mGroupAdapter.setNewData(list);
                        } else {
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    }
                }
            }
            return false;
        }
    });


    public static SourceDataViewFragment getInstance(String title, String link) {
        SourceDataViewFragment sourceDataViewFragment = new SourceDataViewFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("link", link);
        sourceDataViewFragment.setArguments(args);
        return sourceDataViewFragment;
    }

    public SourceDataViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_source_data_view, container, false);
        //加载view
        bindView(v);
        bindData();
        bindEvent();
        return v;
    }


    @SuppressLint("ResourceAsColor")
    private void bindView(View v) {
        bind = ButterKnife.bind(this, v);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorLogo1, R.color.colorLogo2, R.color.colorLogo3, R.color.colorLogo4);
        //RecyclerView相关
        MyLinearLayoutManager myLinearLayoutManager;
        myLinearLayoutManager = new MyLinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(myLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mGroupAdapter = new GroupAdapter(list));
        mGroupAdapter.expandAll();
    }

    private void bindData() {
        title = Objects.requireNonNull(getArguments()).getString("title");
    }

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    private void bindEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.postDelayed(() -> {
            page = 1;
            //开启下拉刷新
            mSwipeRefreshLayout.setRefreshing(true);
            //开启线程加载数据
            new Thread(runnable).start();
        }, 1000));
        mGroupAdapter.setOnItemClickListener(new GroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (adapter.getItemViewType(position)) {
                    case TYPE_LEVEL_0:
                        final group group = (group) adapter.getData().get(position);
                        break;
                    case TYPE_LEVEL_1:
                        final sources sources = (sources) adapter.getData().get(position);
                        String sourcesname = sources.getSourcesName();
                        String sourceslink = sources.getSourcesLink();
                        if ("1".equals(sources.getSourcesIshave())) {
                            SnackbarUtil.ShortSnackbar(getView(),"该源已存在",SnackbarUtil.Warning).show();


                        } else {
                            //子项目 源 的点击
                            if (LOCAL.equals(title)) {
                                //点击 删除对话框
                                delAlert(sourcesname, 0, TYPE_LEVEL_1, position);
                            } else {
                                //添加对话框
                                String baseurl = "https://hege.gitee.io/api/sources";
                                addAlert(sourcesname, 0, TYPE_LEVEL_1, baseurl + sourceslink, position);
                            }
                        }
                        break;
                    default:
                        break;
                }


            }
        });

        mGroupAdapter.setOnItemChildClickListener(new GroupAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (adapter.getItemViewType(position)) {
                    case TYPE_LEVEL_0:
                        final group group = (group) adapter.getData().get(position);
                        String groupname = group.getGroupName();
                        String grouplink = group.getGroupLink();
                        int size = group.getSourcess().size();
                        if (LOCAL.equals(title)) {
                            delAlert(groupname, size, TYPE_LEVEL_0, position);
                        } else {
                            addAlert(groupname, size, TYPE_LEVEL_0, grouplink, position);
                        }
                        break;
                    case TYPE_LEVEL_1:
                    default:
                        break;
                }
            }
        });

        //进入界面开始加载数据
        mSwipeRefreshLayout.post(() -> {
            page = 1;
            //开启下拉刷新
            mSwipeRefreshLayout.setRefreshing(true);
            //开启线程加载数据
            new Thread(runnable).start();
        });
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //加载数据
            if (LOCAL.equals(title)) {
                //根据分组获取源的名称
                if (list.isEmpty() || list.size() > 0) {
                    list.clear();
                }
                List<String> titleslist;
                List<HomeEntity> homeEntities;
                titleslist = SQLModel.getInstance().getGroup();
                for (int i = 0; i < titleslist.size(); i++) {
                    group groupBean = new group();
                    groupBean.setGroupName(titleslist.get(i));
                    homeEntities = SQLModel.getInstance().gethomeEntitiesByGroup(titleslist.get(i));

                    //可展开数据相关
                    List<sources> sourcesBeans = new ArrayList<>();
                    for (int j = 0; j < homeEntities.size(); j++) {
                        sources sourcesBean = new sources();
                        sourcesBean.setSourcesName(homeEntities.get(j).getTitle());
                        if (homeEntities.get(j).getType() != null) {
                            if (!"".equals(homeEntities.get(j).getType())) {
                                sourcesBean.setSourcesDate(homeEntities.get(j).getType());
                            }
                        }
                        sourcesBeans.add(sourcesBean);
                        groupBean.addSubItem(sourcesBean);
                    }
                    groupBean.setSourcess(sourcesBeans);
                    list.add(groupBean);
                }

            } else {
                //加载源市场
                list = SourceUtils.getSourceAllGroup();

            }
            sourcesCount = 0;
            for (int j = 0; j < list.size(); j++) {
                group group = (com.yellowriver.skiff.Bean.SourcesBean.group) list.get(j);
                sourcesCount += group.getSourcess().size();
            }
            Message msg = new Message();
            msg.what = 123;
            mHandler2.sendMessage(msg);
        }
    };


    private void delAlert(String t, int size, int TYPE_LEVEL, int position) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(Objects.requireNonNull(getContext()));

        if (TYPE_LEVEL == 0) {
            normalDialog.setTitle("删除分组");
            normalDialog.setMessage("确定要删除“" + t + "”吗?该分组下共有" + size + "个源");
        } else {
            normalDialog.setTitle("删除源");
            normalDialog.setMessage("确定要删除“" + t + "”吗?");
        }
        normalDialog.setPositiveButton("确定",
                (dialog, which) -> {
                    //...To-do

                        SQLModel.getInstance().delbyTitle(t);
                        List<HomeEntity> homeEntityList = SQLModel.getInstance().getXpathbyTitle(t);

                        if (!homeEntityList.isEmpty()) {
                            SnackbarUtil.ShortSnackbar(getView(),"删除失败！",SnackbarUtil.Warning).show();


                        } else {
                            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                                SnackbarUtil.ShortSnackbar(getView(),"删除成功！",SnackbarUtil.Warning).show();


                                SharedPreferencesUtils.dataChange(true, getContext());
                                int positionAtAll = mGroupAdapter.getParentPositionInAll(position);
                                mGroupAdapter.remove(position);
                                if (positionAtAll != -1) {
                                    IExpandable multiItemEntity = (IExpandable) mGroupAdapter.getData().get(positionAtAll);
                                    if (!mGroupAdapter.hasSubItems(multiItemEntity)) {
                                        mGroupAdapter.remove(positionAtAll);
                                    }
                                }
                            });
                        }

                });
        normalDialog.setNegativeButton("取消",
                (dialog, which) -> {
                    //...To-do
                });
        // 显示
        normalDialog.show();
    }


    private void addAlert(String title, int size, int TYPE_LEVEL, String url, int position) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        if (TYPE_LEVEL == 0) {
            normalDialog.setTitle("导入分组");
            normalDialog.setMessage("确定要导入“" + title + "”吗?该分组下共有" + size + "个源");
        } else {
            normalDialog.setTitle("导入源");
            normalDialog.setMessage("确定要导入“" + title + "”吗?");
        }
        normalDialog.setPositiveButton("确定", (dialogInterface, i) -> {

            ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024));
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    boolean isAdd =  SourceDataSource.getInstance().addSource(url);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isAdd)
                            {
                                SnackbarUtil.ShortSnackbar(getView(),"源添加成功！",SnackbarUtil.Confirm).show();


                                SharedPreferencesUtils.dataChange(true, getContext());
                                int positionAtAll = mGroupAdapter.getParentPositionInAll(position);
                                mGroupAdapter.notifyItemChanged(position,"addok");
                                if (positionAtAll != -1) {
                                    IExpandable multiItemEntity = (IExpandable) mGroupAdapter.getData().get(positionAtAll);
                                    if (!mGroupAdapter.hasSubItems(multiItemEntity)) {
                                        mGroupAdapter.notifyItemChanged(positionAtAll,"addok");
                                    }
                                }
                            }else{
                                SnackbarUtil.ShortSnackbar(getView(),"源添加失败！",SnackbarUtil.Warning).show();


                            }
                        }
                    });
                }
            };
            singleThreadPool.execute(runnable);

        }).setNegativeButton("取消", null).show();

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


}
