package com.yellowriver.skiff.View.Fragment.Home;


import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.chad.library.adapter.base.BaseQuickAdapter;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.HomeAdapter;
import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.SearchAdapter;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.HomeBean.MsgEvent;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.AnalysisUtils;
import com.yellowriver.skiff.Help.CustomLoadMoreView2;
import com.yellowriver.skiff.Help.LogUtil;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.Help.MyLinearLayoutManager;
import com.yellowriver.skiff.ViewModel.MainViewModel;
import com.yellowriver.skiff.ViewModel.RssViewModel;
import com.yellowriver.skiff.ViewUtils.MainViewClick;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.shaohui.bottomdialog.BottomDialog;


/**
 * 通用数据显示view
 *
 * @author huang
 * @date 2019
 */
public class HomeDataViewFragment extends Fragment {

    private static final String AJAX = "2";
    private static final String POSITIVE = "正";
    private static final String NEGATIVE = "反";


    private static final String TAG = "HomeDataViewFragment";

    @BindView(R.id.results_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_tishi)
    TextView tvTishi;
    @BindView(R.id.ll_pbar)
    LinearLayout llPbar;
    @BindView(R.id.main)
    FrameLayout main;


    /**
     * HomeAdapter
     */
    private HomeAdapter mHomeAdapter;
    /**
     * 源数据实体  数据库规则实体
     */
    private HomeEntity homeEntity;
    /**
     * 初始page
     */
    private int page = 1;
    /**
     * 获取的数据列表
     */
    private Vector<DataEntity> mDataEntity;
    /**
     * 是否是RSS源
     */
    private boolean isRss = false;
    /**
     * 下一页url
     */
    private String nextPage = "";


    /**
     * 用着数据库查询
     */
    private String qzSourceName;
    /**
     * 用着数据库查询第几步
     */
    private String qzStep;
    /**
     * 用着解析url
     */
    private String qzUrl;
    /**
     * 首页还是搜索
     */
    private String qzSourcesType;
    /**
     * 搜索关键字
     */
    private String qzQuery;
    /**
     * 搜索分组下标
     */
    private int qzSpinnerSel;
    private int readIndex;
    /**
     * 分组名称
     */
    private String qzGroupName;
    /**
     * 上级标题
     */
    private String qzTitle;
    /**
     * 当前步骤的所需要的数据封装
     */
    NowRuleBean nowRuleBean;
    /**
     * 绑定控件
     */
    private Unbinder bind;
    /**
     * 缓存view
     */
    private View mRootView;
    /**
     * XpathModel 和 RssModel
     */
    MainViewModel XpathModel;
    RssViewModel RssModel;

    /**
     * 网络加载数据后更新UI
     */
    private void updateView(Vector<DataEntity> data) {
        mDataEntity = data;


        getActivity().runOnUiThread(() -> {
            mSwipeRefreshLayout.setRefreshing(false);
            if (mDataEntity != null) {
                if (mDataEntity.size() != 0) {
                    Log.d(TAG, "updateView: " + mDataEntity.size());
                    if (page == 1) {
                        if (readIndex != -1) {
                            if (readIndex < mDataEntity.size()) {
                                try {
                                    DataEntity dataEntity = mDataEntity.get(readIndex);
                                    //dataEntity.setTitle(">>>"+mDataEntity.get(readIndex).getTitle()+"<<<");
                                    dataEntity.setColor("红色");
                                    mDataEntity.remove(readIndex);
                                    mDataEntity.add(readIndex, dataEntity);
                                } catch (ArrayIndexOutOfBoundsException e) {

                                }
                            }
                        }
                        if (nowRuleBean != null) {
                            if (!nowRuleBean.getReadNextPage().isEmpty()) {
                                if (nowRuleBean.getReadNextPage().indexOf("反")!=-1) {

                                    Collections.reverse(mDataEntity);
                                }
                            }
                        }
                        if(mHomeAdapter!=null) {
                            mHomeAdapter.setNewData(mDataEntity);
                        }
                        if(mRecyclerView!=null) {
                            mRecyclerView.scrollToPosition(readIndex);
                        }
                        if(mHomeAdapter!=null) {
                            if ("".equals(nextPage)) {
                                mHomeAdapter.setFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.footer_loadover, mRecyclerView, false));
                            }
                        }
                    } else {

                        if(mHomeAdapter!=null) {
                            mHomeAdapter.addData(mDataEntity);
                            mHomeAdapter.getLoadMoreModule().loadMoreComplete();
                        }
                    }
                } else {
                    //加载空视图
                    if(mHomeAdapter!=null) {
                        if (page != 1) {

                            mHomeAdapter.getLoadMoreModule().loadMoreComplete();
                            if (!mHomeAdapter.getLoadMoreModule().isLoading()) {

                                mHomeAdapter.setFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.footer_loadover, mRecyclerView, false));
                            }
                        } else {
                            View view = View.inflate(getContext(), R.layout.empty_layout, null);
                            mHomeAdapter.setEmptyView(view);
                        }
                    }
                }
            } else {
                //加载空视图
                if(mHomeAdapter!=null) {
                    View view = View.inflate(getContext(), R.layout.empty_layout, null);
                    mHomeAdapter.setEmptyView(view);
                }
            }
        });
    }


    /**
     * @param qzGroupName   原名称
     * @param qzStep        第九步
     * @param qzUrl         链接
     * @param qzSourcesType 类型  首页 或 搜索
     * @param qzQuery       搜索关键字
     * @return
     */
    public static HomeDataViewFragment getInstance(String qzGroupName, String qzSourceName, String qzStep, String qzUrl, String qzSourcesType, String qzQuery, int qzSpinnerSel, String qzTitle, int readIndex) {
        Bundle args = new Bundle();
        HomeDataViewFragment homeDataViewFragment = new HomeDataViewFragment();
        args.putString("qzGroupName", qzGroupName);
        args.putString("qzSourceName", qzSourceName);
        args.putString("qzStep", qzStep);
        args.putString("qzUrl", qzUrl);
        args.putString("qzSourcesType", qzSourcesType);
        args.putString("qzQuery", qzQuery);
        args.putString("qzTitle", qzTitle);
        args.putInt("qzSpinnerSel", qzSpinnerSel);
        args.putInt("readIndex", readIndex);
        homeDataViewFragment.setArguments(args);
        return homeDataViewFragment;
    }

    public static HomeDataViewFragment newInstance(String qzGroupName, String qzSourceName, String qzStep, String qzUrl, String qzSourcesType, String qzQuery, int qzSpinnerSel, String qzTitle, int readIndex) {
        HomeDataViewFragment homeDataViewFragment = new HomeDataViewFragment();
        Bundle args = new Bundle();
        args.putString("qzGroupName", qzGroupName);
        args.putString("qzSourceName", qzSourceName);
        args.putString("qzStep", qzStep);
        args.putString("qzUrl", qzUrl);
        args.putString("qzSourcesType", qzSourcesType);
        args.putString("qzQuery", qzQuery);
        args.putString("qzTitle", qzTitle);
        args.putInt("qzSpinnerSel", qzSpinnerSel);
        args.putInt("readIndex", readIndex);
        homeDataViewFragment.setArguments(args);
        return homeDataViewFragment;
    }

    public HomeDataViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "测试-->onCreateView");
        if (mRootView == null) {
            Log.d(TAG, "测试-->新加载view");
            mRootView = inflater.inflate(R.layout.fragment_home_data_view, container, false);
            bind = ButterKnife.bind(this, mRootView);
            bindData();
        } else {
            Log.d(TAG, "测试-->使用旧view");
        }
        return mRootView;
    }


    private void bindData() {
        //获取传递过来的数据
        qzGroupName = requireArguments().getString("qzGroupName");
        Log.d(TAG, "bindData: " + qzGroupName);
        qzSourceName = requireArguments().getString("qzSourceName");
        qzStep = getArguments().getString("qzStep");
        qzUrl = getArguments().getString("qzUrl");
        qzSourcesType = getArguments().getString("qzSourcesType");
        qzQuery = getArguments().getString("qzQuery");
        qzSpinnerSel = getArguments().getInt("qzSpinnerSel");
        qzTitle = getArguments().getString("qzTitle");
        readIndex = getArguments().getInt("readIndex");
        //默认正常顺序
        SharedPreferencesUtils.writeDataSort("正", requireContext());
        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(() -> {
            List<HomeEntity> homeEntities = SQLModel.getInstance().getXpathbyTitle(qzSourceName, qzSourcesType);
            getActivity().runOnUiThread(() -> updateView2(homeEntities));
        });
    }


    //数据库加载数据后更新ui
    private void updateView2(List<HomeEntity> homeEntities) {
        setHasOptionsMenu(true);
        //下拉刷新颜色
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorLogo1, R.color.colorLogo2, R.color.colorLogo3, R.color.colorLogo4);

        }
        if(homeEntities!=null) {
            homeEntity = homeEntities.get(0);
            nowRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntity, qzStep, qzQuery, qzUrl);
            nextPage = nowRuleBean.getNextPageXpath();
            //显示不同界面 垂直 或 水平
            if (mRecyclerView != null) {
                viewType();

                //RecyclerView的基本设置
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                //根据设置加载不同动画
                //loadAnimation();
            }
            //是htmlunit加载 提示 需等待较久
            loadMode();
            //解析xpath或json为一个viewmode 解析rss为另一个viewmodel
            loadModel();
            //绑定事件
            bindEvent();
        }
    }

    //加载方式
    private void loadModel() {
        String rssType = "{QZRSS}";
        if (homeEntity.getFirstListXpath().contains(rssType)) {
            isRss = true;
        }
        if (isRss) {
            RssModel = ViewModelProviders.of(this).get(RssViewModel.class);
        } else {
            XpathModel = ViewModelProviders.of(this).get(MainViewModel.class);
        }
    }

    //绑定事件
    private void bindEvent() {
        firstLoadData();
        swipeRefresh();
        itemClick();
        loadMore();
    }

    //首次进入加载
    private void firstLoadData() {
        //进入界面开始加载数据
        mSwipeRefreshLayout.post(() -> {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(true);
                if (isRss) {
                    getRss();
                } else {
                    //初始化viewModel
                    getData();
                }
            }
        });
    }

    //下拉刷新
    private void swipeRefresh() {
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.postDelayed(() -> {
            page = 1;
            //开启下拉刷新
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
            if (isRss) {
                getRss();
            } else {

                xpathReload();
            }
        }, 1000));
    }

    private void loadMore() {
        //下拉加载更多
        mHomeAdapter.getLoadMoreModule();
        if (!"".equals(nextPage)) {
            mHomeAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    page += 1;
                    if (isRss) {
                        getRss();
                    } else {
                        xpathLoadMore(page);
                    }
                }
            });
        }
        mHomeAdapter.getLoadMoreModule().setLoadMoreView(new CustomLoadMoreView2());
        mRecyclerView.scrollToPosition(readIndex);

    }

    private void itemClick() {
        //主适配器点击事件
        mHomeAdapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemClick: ");
            final DataEntity dataEntity = (DataEntity) adapter.getData().get(position);
            EventBus.getDefault().postSticky(new MsgEvent(111, mDataEntity, position));   //发送时间

            boolean finaltype = isFinalType();
            boolean result = MainViewClick.OnClick(getContext(), nowRuleBean, dataEntity, qzSpinnerSel, qzTitle, readIndex, finaltype,position);
            if (!result) {
                SnackbarUtil.ShortSnackbar(getView(), "该源配置不正确，无法进行下一步。", SnackbarUtil.Warning).show();

            }

        });
    }

    /**
     * ***********************************************************右上角菜单
     */
    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.nextsetting, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_changeSource:
                //显示换源列表
                showBottomSheetDialog();
                return true;
            case R.id.action_sort:
                //如果获取的数据不为空
                if (mDataEntity != null) {
                    //反转数据
//                    Collections.reverse(mHomeAdapter.getData());
//                    mHomeAdapter.notifyDataSetChanged();
//                    mRecyclerView.scrollToPosition(0);
                    //保存数据的状态
                    if (POSITIVE.equals(SharedPreferencesUtils.readDataSort(requireContext()))) {
                        SharedPreferencesUtils.writeDataSort("反", getContext());
                        mRecyclerView.scrollToPosition(mHomeAdapter.getData().size());
                        Log.d(TAG, "onOptionsItemSelected: " + mHomeAdapter.getData().size());
                        if (item.getTitle().equals("滑动到底部")) {
                            item.setTitle("回到顶部");
                        }

                    } else if (NEGATIVE.equals(SharedPreferencesUtils.readDataSort(getContext()))) {
                        SharedPreferencesUtils.writeDataSort("正", getContext());
                        mRecyclerView.scrollToPosition(0);
                        if (item.getTitle().equals("回到顶部")) {
                            item.setTitle("滑动到底部");
                        }

                    }
                }
                return true;
            default:
                return false;
        }
    }
    //***********************************************************右上角菜单


    /**
     * 获取xpath相关
     */
    private void getData() {

        XpathModel.getProjects(homeEntity, qzStep, qzUrl, qzQuery, page).observe(getViewLifecycleOwner(), this::updateView);
        xpathReload();
    }

    //xpath重新加载（加载第一页）
    private void xpathReload() {
        XpathModel.reload();
    }

    //xpath加载更多数据 （page>1）
    private void xpathLoadMore(int page) {
        XpathModel.loadMore(page);
    }

    //RSS相关
    private void getRss() throws IllegalStateException {

        if ("1".equals(qzStep)) {
            RssModel.getArticleList().observe(getActivity(), this::updateView);
            rssReload();
        }
    }

    //rss重新加载（加载第一页）
    private void rssReload() {
        RssModel.fetchFeed(homeEntity.getFirsturl());
    }


    @Override
    public void onStart() {    //注册
        super.onStart();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {    //反注册
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "测试-->onDestroy");
        super.onDestroy();
        if (bind != null) {
            //解除绑定
            bind.unbind();
        }

        //销毁线程
        if (threadPoolExecutor != null) {
            LogUtil.info("TAG销毁", "销毁线程");
            threadPoolExecutor.shutdown();
        }
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (fragmentTransaction.isEmpty()) {

        } else {
            List<Fragment> fragmentList = getActivity().getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragmentList) {
                fragmentTransaction.remove(fragment);
            }
        }
        if (null != mRootView && mRootView.getParent() != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);

        }

    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "测试-->onDestroyView");
        super.onDestroyView();
        mSwipeRefreshLayout.setRefreshing(false);

        mSwipeRefreshLayout.destroyDrawingCache();

        mSwipeRefreshLayout.clearAnimation();
        if (null != mRootView && mRootView.getParent() != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);

        }
        mRootView = null;
        mHomeAdapter = null;
        mSearchAdapter = null;
        mSearchRecyclerView = null;
        mSearchRefresh = null;
        textView = null;
    }

    /**
     * 根据设置加载不同动画
     */
//    private void loadAnimation() {
//        String loadAnimation = SharedPreferencesUtils.listLoadAnimationRead(getContext());
//        switch (loadAnimation) {
//            case "默认无":
//                break;
//            case "渐显":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
//                break;
//            case "缩放":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//                break;
//            case "从下到上":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//                break;
//            case "从左到右":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//                break;
//            case "从右到左":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
//                break;
//            case "每次随机":
//                randomLoadAnimation();
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * 随机加载动画
     */
//    private void randomLoadAnimation() {
//        int Animation = (int) (1 + Math.random() * (6 - 1 + 1));
//        switch (Animation) {
//            case 1:
//                //默认无
//                break;
//            case 2:
//                //渐显
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
//                break;
//            case 3:
//                //缩放
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//                break;
//            case 4:
//                //从下到上
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//                break;
//            case 5:
//                //从左到右
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//                break;
//            case 6:
//                //从右到左
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
//                break;
//            default:
//                break;
//        }
//    }

    //根据源控制列表还是表格
    private void viewType() {
        //垂直显示
        MyLinearLayoutManager myLinearLayoutManager;
        //根据源的显示模式  1为垂直显示 2为网格水平显示
        switch (Objects.requireNonNull(nowRuleBean.getViewMode())) {
            case "1":
                //标题 垂直
                myLinearLayoutManager = new MyLinearLayoutManager(getContext());
                if (myLinearLayoutManager != null && mRecyclerView != null) {
                    mRecyclerView.setLayoutManager(myLinearLayoutManager);
                }
                mRecyclerView.setAdapter(mHomeAdapter = new HomeAdapter(R.layout.maindata_vertical_item));
                break;
            case "2":
                //标题 水平 2
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mRecyclerView.setAdapter(mHomeAdapter = new HomeAdapter(R.layout.maindata_horizontal_item));
                break;
            case "3":
                //标题 水平 3
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                mRecyclerView.setAdapter(mHomeAdapter = new HomeAdapter(R.layout.maindata_horizontal_item));
                break;
            default:
                myLinearLayoutManager = new MyLinearLayoutManager(getContext());
                if (myLinearLayoutManager != null && mRecyclerView != null) {
                    mRecyclerView.setLayoutManager(myLinearLayoutManager);
                }
                mRecyclerView.setAdapter(mHomeAdapter = new HomeAdapter(R.layout.maindata_vertical_item));
                break;

        }
    }

    //加载模式  未显示
    private void loadMode() {
        //是否用htmlunit加载网页 是的话提示用户加载比较耗时
        if(tvTishi!=null) {
            if (AJAX.equals(Objects.requireNonNull(nowRuleBean.getIsAjax()))) {
                //提示需要耗时加载网页
                tvTishi.setVisibility(View.VISIBLE);
                tvTishi.setText("该源使用模拟浏览器加载，等待页面完全加载...");
            } else {
                tvTishi.setVisibility(View.GONE);
            }
        }
    }

    //*************************************************换源相关************************
    SearchAdapter mSearchAdapter;
    RecyclerView mSearchRecyclerView;
    private List<String> sourcesList;
    /**
     * 获取的数据列表
     */
    private Vector<DataEntity> mSearchData;


    TextView textView;
    BottomDialog dialog;
    SwipeRefreshLayout mSearchRefresh;

    private void showBottomSheetDialog() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight() / 2;
        //mSearchAdapter = new SearchAdapter(R.layout.searchdata_vertical_item);
        dialog = new BottomDialog();
        //显示换源列表
        dialog.create(getActivity().getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        // // You can do any of the necessary the operation with the view
                        mSearchRefresh = v.findViewById(R.id.searchswipeRefreshLayout);
                        //下拉刷新颜色
                        if (mSearchRefresh != null) {
                            mSearchRefresh.setColorSchemeResources(R.color.colorLogo1, R.color.colorLogo2, R.color.colorLogo3, R.color.colorLogo4);

                        }
                        textView = v.findViewById(R.id.searchtextview);
                        if (textView != null) {
                            textView.setText("搜索'" + qzTitle + "'");
                        }
                        mSearchRefresh.setEnabled(false);
                        mSearchRecyclerView = v.findViewById(R.id.results_list);
                        MyLinearLayoutManager myLinearLayoutManager = new MyLinearLayoutManager(getContext());
                        if (mSearchRecyclerView != null) {
                            mSearchRecyclerView.setLayoutManager(myLinearLayoutManager);
                            // RecyclerView的基本设置
                            //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            ((SimpleItemAnimator) mSearchRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

                            mSearchRecyclerView.setAdapter(mSearchAdapter = new SearchAdapter(R.layout.changlesource_vertical_item));

                        }
                        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(() -> {
                            sourcesList = SQLModel.getInstance().getTitleByGroup(qzGroupName, "search");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (sourcesList != null) {
                                        if (sourcesList.size() != 0) {
                                            mSearchRefresh.post(() -> {
                                                if (mSearchRefresh != null) {
                                                    mSearchRefresh.setRefreshing(true);
                                                    loadData();
                                                }
                                            });
                                        } else {
                                            View view = View.inflate(getContext(), R.layout.empty_layout, null);
                                            mSearchAdapter.setEmptyView(view);
                                        }
                                    } else {
                                        View view = View.inflate(getContext(), R.layout.empty_layout, null);
                                        mSearchAdapter.setEmptyView(view);
                                    }
                                }
                            });

                        });
                        searchItemClick();

                    }
                })
                .setHeight(height)
                .setLayoutRes(R.layout.changesource_dialog)      // dialog layout
                .setTag("BottomDialog")
                .show();


    }


    ThreadPoolExecutor threadPoolExecutor = null;

    //搜索加载数据
    private void loadData() {
        if (sourcesList != null) {
            //LogUtil.info(TAG, "loadData: 分组下的所有搜索源大小：" + sourcesList.size());
            //创建基本线程池
            int corepollsize = SharedPreferencesUtils.searchSumRead(getContext());
            threadPoolExecutor = new ThreadPoolExecutor(corepollsize, Integer.MAX_VALUE, 1, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(50));

            for (int i = 0; i < sourcesList.size(); i++) {
                final int finali = i;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        List<HomeEntity> homeEntities = SQLModel.getInstance().getXpathbyTitle(sourcesList.get(finali), "search");
                        if (homeEntities != null) {
                            homeEntity = homeEntities.get(0);

                            if (qzQuery != null) {
                                if (!qzQuery.isEmpty()) {

                                } else {
                                    qzQuery = qzTitle;
                                }
                            } else {
                                qzQuery = qzTitle;
                            }
                            Vector<DataEntity> dataEntities = AnalysisUtils.getInstance().MainAnalysis(homeEntity, "1", "", qzTitle, page);
                            Log.d(TAG, "run: 搜索关键字" + qzQuery + "--" + qzTitle);
                            //  LogUtil.info("TAG", "run : " + finali + "当前加载源" + sourcesList.get(finali).toString() + "  当前线程：" + Thread.currentThread().getName());
                            if (dataEntities != null) {
                                if (getActivity() != null) {
                                    // LogUtil.info("TAG销毁","存在fraf");
                                    updateViewChangeSource(dataEntities, sourcesList.get(finali));
                                } else {
                                    // LogUtil.info("TAG销毁","不存在fraf");

                                }
                            }
                        }
                    }
                };
                if (!threadPoolExecutor.isShutdown()) {
                    threadPoolExecutor.execute(runnable);
                }

            }

        } else {
            if (mSearchRefresh != null) {
                mSearchRefresh.setRefreshing(false);
                if (mSearchAdapter != null) {
                    View view = View.inflate(getContext(), R.layout.empty_layout, null);
                    mSearchAdapter.setEmptyView(view);
                }
            }
        }


    }

    /**
     * 搜索更新UI
     */
    private void updateViewChangeSource(Vector<DataEntity> data, String fromSource) {
        mSearchData = data;


        getActivity().runOnUiThread(() -> {
            if (mSearchData != null) {
                if (mSearchData.size() != 0) {
                    Log.d(TAG, "updateView: " + mSearchData.size());
                    if (page == 1) {

                        Vector<DataEntity> newDataEntity = new Vector<>();
                        for (int i = 0; i < mSearchData.size(); i++) {
                            DataEntity newdataEntity = mSearchData.get(i);

                            Log.d(TAG, "updateViewChangeSource: 标题" + qzSourceName);
                            Log.d(TAG, "updateViewChangeSource: 新标题" + fromSource);

                            newdataEntity.setDate("来源: " + fromSource);
                            newdataEntity.setFromSource(fromSource);

                            if (newdataEntity.getTitle().equals(qzTitle)) {
                                // Log.d(TAG, "updateViewChangeSource: 不是完全一样");
                                if (fromSource.equals(qzSourceName)) {
                                    newdataEntity.setState(99);
                                }
                                newDataEntity.add(newdataEntity);
                            }
                        }
                        if (mSearchRefresh != null) {
                            mSearchRefresh.setRefreshing(false);
                        }
                        if (mSearchAdapter != null) {
                            mSearchAdapter.addData(newDataEntity);
                            if (textView != null) {
                                textView.setText("搜索'" + qzTitle + "'" + "(" + mSearchAdapter.getData().size() + ")");
                            }
                        }

                    } else {
                        Vector<DataEntity> newDataEntity = new Vector<>();
                        for (int i = 0; i < mSearchData.size(); i++) {
                            DataEntity newdataEntity = mSearchData.get(i);

                            //Log.d(TAG, "updateViewChangeSource: 标题"+qzTitle);
                            //Log.d(TAG, "updateViewChangeSource: 新标题"+newdataEntity.getTitle());

                            newdataEntity.setDate("来源: " + fromSource);
                            newdataEntity.setFromSource(fromSource);
                            if (!newdataEntity.getTitle().equals(qzTitle)) {
                                // Log.d(TAG, "updateViewChangeSource: 不是完全一样");
                                newDataEntity.add(newdataEntity);
                            }
                        }
                        if (mSearchRefresh != null) {
                            mSearchRefresh.setRefreshing(false);
                        }
                        if (mSearchAdapter != null) {
                            mSearchAdapter.addData(newDataEntity);
                            if (textView != null) {
                                textView.setText("搜索'" + qzTitle + "'" + "(" + mSearchAdapter.getData().size() + ")");
                            }
                        }

                    }
                } else {
                    if (mSearchRefresh != null) {
                        mSearchRefresh.setRefreshing(false);
                    }
                    //加载空视图
                    if (page != 1) {

                    } else {

                    }
                }
            } else {
                if (mSearchRefresh != null) {
                    mSearchRefresh.setRefreshing(false);
                }
            }
        });
    }


    //搜索点击事件
    private void searchItemClick() {
        //主适配器点击事件
        mSearchAdapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemClick: ");
            final DataEntity dataEntity = (DataEntity) adapter.getData().get(position);
            SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(() -> {
                List<HomeEntity> homeEntities = SQLModel.getInstance().getXpathbyTitle(dataEntity.getFromSource(), "search");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeEntity = homeEntities.get(0);
                        NowRuleBean nowRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntity, "1", qzQuery, dataEntity.getLink());
                        String nowSourceName = qzSourceName;
                        qzStep = "2";
                        qzUrl = dataEntity.getLink();
                        qzSourceName = dataEntity.getFromSource();
                        qzQuery = dataEntity.getTitle();
                        qzTitle = dataEntity.getTitle();

                        if (nowSourceName.equals(qzSourceName)) {
                            //当前就是这个源 不执行操作
                        } else {
                            EventBus.getDefault().postSticky(new MsgEvent(111, mDataEntity, position));   //发送时间
                            boolean finaltype = isFinalTypebySearch();
                            boolean result = MainViewClick.OnClick(getContext(), nowRuleBean, dataEntity, qzSpinnerSel, dataEntity.getTitle(), readIndex, finaltype,position);
                            if (!result) {
                                SnackbarUtil.ShortSnackbar(getView(), "该源配置不正确，无法进行下一步。", SnackbarUtil.Warning).show();
                            } else {
                                destroySearch();
                            }
                        }
                    }
                });
            });

        });


    }
    private boolean isFinalTypebySearch() {
        boolean finaltype = false;
        String nextStep = "2";
        NowRuleBean nextRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntity, nextStep, qzQuery, qzUrl);
        String nextLinkType = nextRuleBean.getLinkType();
        LogUtil.info("继续解析","下一步"+nextLinkType);
        if (!nextLinkType.equals("1")&&!nextLinkType.equals("8")) {
            //表示最后一个列表 显示一个上面有图片 介绍 下面是列表的activity
            finaltype = true;
        } else {
            finaltype = false;
        }
        return finaltype;
    }
    //判断是不是最后一个继续解析
    private boolean isFinalType() {
        boolean finaltype = false;
        String nextStep = "";
        LogUtil.info("继续解析","下一步"+nowRuleBean.getLinkType());
        if (nowRuleBean.getLinkType().equals("1")) {
            //当前未继续解析

            switch (qzStep) {
                case "1":
                    nextStep = "2";
                    break;
                case "2":
                    nextStep = "3";
                    break;
            }

            LogUtil.info("继续解析","下一步"+nextStep);
            NowRuleBean nextRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntity, nextStep, qzQuery, qzUrl);

            String nextLinkType = nextRuleBean.getLinkType();
            LogUtil.info("继续解析","下一步"+nextLinkType);
            if (!nextLinkType.equals("1")&&!nextLinkType.equals("8")) {
                //表示最后一个列表 显示一个上面有图片 介绍 下面是列表的activity
                finaltype = true;
            } else {
                finaltype = false;
            }
        } else {
            finaltype = false;
        }
        return finaltype;
    }


    //销毁搜索用的资源
    private void destroySearch() {
        if (threadPoolExecutor != null) {
            LogUtil.info("TAG销毁", "销毁线程");
            threadPoolExecutor.shutdown();
        }

        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("BottomDialog ");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
        getActivity().finish();
        mRootView = null;
        mHomeAdapter = null;
        mSearchAdapter = null;
        mSearchRecyclerView = null;
        mSearchRefresh = null;
        textView = null;
    }
}
