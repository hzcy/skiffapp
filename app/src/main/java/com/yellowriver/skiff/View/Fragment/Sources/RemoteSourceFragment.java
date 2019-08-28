package com.yellowriver.skiff.View.Fragment.Sources;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yellowriver.skiff.Adapter.TreeAdapter.GroupAdapter;
import com.yellowriver.skiff.Adapter.ViewPageAdapter.ContentPagerAdapter;
import com.yellowriver.skiff.Bean.SourcesBean.SourcesEntity;
import com.yellowriver.skiff.Bean.SourcesBean.group;
import com.yellowriver.skiff.Bean.SourcesBean.sources;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.JsonUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.NetUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.SourceUtils;
import com.yellowriver.skiff.Help.MyLinearLayoutManager;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.Model.SourceDataSource;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.ViewModel.LocalSourceModel;
import com.yellowriver.skiff.ViewModel.RemoteSourceModel;

import org.jetbrains.annotations.NotNull;


import java.lang.reflect.Type;
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
public class RemoteSourceFragment extends Fragment {
    String baseurl = "https://hege.gitee.io/api/sources";
    private static String TAG = "RemoteSourceFragment";

    @BindView(R.id.results_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isPrepared = false;
    /**
     * 用着数据库查询
     */
    private String title;
    private GroupAdapter mGroupAdapter;
    private List<MultiItemEntity> list = new ArrayList<>();
    private int page = 1;
    private int sourcesCount = 0;
    private ContentPagerAdapter mAdapter;
    private static Gson gson = new Gson();

    RemoteSourceModel remoteSourceModel;

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    /**
     * 绑定控件
     */
    private Unbinder bind;
    public void setPageAdapter(ContentPagerAdapter adapter) {
        this.mAdapter = adapter;
    }

    private void updateView(List<MultiItemEntity> list) {

        Log.d(TAG, "handleMessage: " + list.size());
        mSwipeRefreshLayout.setRefreshing(false);
        //解决刷新时快速滑动导致cash
        sourcesCount = 0;

        if (page == 1) {
            if (list != null) {
                if (list.size() != 0) {

                    if (mAdapter != null) {
                        int i = 0;
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


    public static RemoteSourceFragment getInstance() {
        RemoteSourceFragment sourceDataViewFragment = new RemoteSourceFragment();
        Bundle args = new Bundle();
        sourceDataViewFragment.setArguments(args);
        return sourceDataViewFragment;
    }

    public RemoteSourceFragment() {
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
        isPrepared = true;
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
        remoteSourceModel = ViewModelProviders.of(this).get(RemoteSourceModel.class);
        title = Objects.requireNonNull(getArguments()).getString("title");
    }

    private void getData() {

        remoteSourceModel.getRemoteSources().observe(this, this::updateView);
        remoteSourceModel.reload();
    }


    private void bindEvent() {
        firstLoadData();
        swipeRefresh();
        groupAddAndDelete();
        sourceAddAndDelete();
        SharedPreferencesUtils.dataChange(
                false, getContext());
    }

    //下拉刷新
    private void swipeRefresh()
    {
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.postDelayed(() -> {
            page = 1;
            //开启下拉刷新
            mSwipeRefreshLayout.setRefreshing(true);

            getData();
        }, 1000));
    }

    //进入界面开始加载数据
    private void firstLoadData()
    {
        mSwipeRefreshLayout.post(() -> {
            page = 1;
            //开启下拉刷新
            mSwipeRefreshLayout.setRefreshing(true);
            getData();
        });
    }

    //一级 分组点击事件 本地源 删除分组 源市场 添加分组
    private void groupAddAndDelete()
    {
        mGroupAdapter.setOnItemChildClickListener(new GroupAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (adapter.getItemViewType(position)) {
                    case TYPE_LEVEL_0:
                        final group group = (group) adapter.getData().get(position);
                        String groupname = group.getGroupName();
                        String grouplink = group.getGroupLink();
                        int size = group.getSourcess().size();

                            if ("1".equals(group.getGroupIshave())) {
                                SnackbarUtil.ShortSnackbar(getView(),"该分组已全部导入",SnackbarUtil.Warning).show();
                            }else {
                                addAlert(groupname, size, TYPE_LEVEL_0, baseurl + grouplink, position);
                            }

                        break;
                    case TYPE_LEVEL_1:
                    default:
                        break;
                }
            }
        });
    }

    //二级 源点击事件 本地源 删除源 源市场 导入源
    private void sourceAddAndDelete()
    {
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
                            //添加对话框
                            addAlert(sourcesname, 0, TYPE_LEVEL_1, baseurl + sourceslink, position);

                        }
                        break;
                    default:
                        break;
                }
            }
        });
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

            ProgressDialog waitingDialog =
                    new ProgressDialog(getContext());
            waitingDialog.setTitle("提示");
            waitingDialog.setMessage("加载中...");
            waitingDialog.setIndeterminate(true);
            waitingDialog.setCancelable(false);
            waitingDialog.show();
            ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024));
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    boolean isAdd = false;
                    if (TYPE_LEVEL == 0){
                        Log.d(TAG, "run: "+url);
                        // 获取文档内容
                        String json = NetUtils.getInstance().getRequest(url, "1");
                        Log.d(TAG, "getSourceAllGroup: "+json);
                        List<SourcesEntity> sourceBean = null;
                        if (JsonUtils.isJSONValid(json)) {
                            Type type = new TypeToken<List<SourcesEntity>>() {
                            }.getType();
                            sourceBean = gson.fromJson(json, type);
                        }
                        int addSize = 0;
                        if (sourceBean!=null)
                        {
                            for (SourcesEntity sourcesEntity : sourceBean)
                            {
                                if (!SQLModel.getInstance().getXpathbyTitle(sourcesEntity.getName(),sourcesEntity.getType()).isEmpty()) {
                                    //存在
                                } else {
                                    String sourceLink = sourcesEntity.getLink();
                                    boolean add = SourceDataSource.getInstance().addSource(baseurl+sourceLink);
                                    if (add)
                                    {
                                        addSize++;
                                    }
                                }
                            }
                        }else {
                           isAdd = false;
                        }
                        if (addSize>0)
                        {
                            isAdd = true;
                        }

                    }else {
                        isAdd = SourceDataSource.getInstance().addSource(url);
                    }
                    boolean finalIsAdd = isAdd;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            waitingDialog.cancel();
                            if (finalIsAdd)
                            {
                                SnackbarUtil.ShortSnackbar(getView(),"源添加成功！",SnackbarUtil.Confirm).show();
                                SharedPreferencesUtils.dataChange(true, getContext());
                                SharedPreferencesUtils.dataChangeSource(true, getContext());
                                int positionAtAll = mGroupAdapter.getParentPositionInAll(position);
                                mGroupAdapter.notifyItemChanged(position,"addok");
                                if (positionAtAll != -1) {
                                    IExpandable multiItemEntity = (IExpandable) mGroupAdapter.getData().get(positionAtAll);
                                    if (!mGroupAdapter.hasSubItems(multiItemEntity)) {
                                        mGroupAdapter.notifyItemChanged(positionAtAll,"addok");
                                    }
                                }
                            }else{
                                SnackbarUtil.ShortSnackbar(getView(),"源添加失败！",SnackbarUtil.Alert).show();
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepared && isVisibleToUser) {
            //加载数据
            if(SharedPreferencesUtils.readdataChangeSource(getContext()))
            {
                getData();
                SharedPreferencesUtils.dataChangeSource(
                        false, getContext());
            }
        }
    }

}
