package com.yellowriver.skiff.View.Fragment.Home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.HomeAdapter;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.HomeBean.MsgEvent;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.AnalysisUtils;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.Help.CustomLoadMoreView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


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
     * 更新UI
     */
    private void updateView(Vector<DataEntity> data) {
        mDataEntity = data;
        mSwipeRefreshLayout.setRefreshing(false);

        getActivity().runOnUiThread(() -> {
            if (mDataEntity != null) {
                if (mDataEntity.size() != 0) {
                    Log.d(TAG, "updateView: "+mDataEntity.size());
                    if (page == 1) {
                        mHomeAdapter.setNewData(mDataEntity);
                        if ("".equals(nextPage)) {
                            mHomeAdapter.setFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.footer_loadover, mRecyclerView, false));
                        }
                    } else {

                        mHomeAdapter.addData(mDataEntity);
                        mHomeAdapter.loadMoreComplete();
                    }
                } else {
                    //加载空视图
                    if (page!=1){
                        mHomeAdapter.loadMoreEnd(true);
                        if (!mHomeAdapter.isLoading()) {

                            mHomeAdapter.setFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.footer_loadover, mRecyclerView, false));
                        }
                    }else {
                        mHomeAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_layout, mRecyclerView, false));
                    }
                }
            } else {
                //加载空视图
                mHomeAdapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_layout, mRecyclerView, false));
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
    public static HomeDataViewFragment getInstance(String qzGroupName, String qzSourceName, String qzStep, String qzUrl, String qzSourcesType, String qzQuery, int qzSpinnerSel, String qzTitle) {
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
        qzGroupName = Objects.requireNonNull(getArguments()).getString("qzGroupName");
        qzSourceName = Objects.requireNonNull(getArguments()).getString("qzSourceName");
        qzStep = getArguments().getString("qzStep");
        qzUrl = getArguments().getString("qzUrl");
        qzSourcesType = getArguments().getString("qzSourcesType");
        qzQuery = getArguments().getString("qzQuery");
        qzSpinnerSel = getArguments().getInt("qzSpinnerSel");
        qzTitle = getArguments().getString("qzTitle");
        //默认正常顺序
        SharedPreferencesUtils.writeDataSort("正", Objects.requireNonNull(getContext()));
        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(() -> {
            List<HomeEntity> homeEntities = SQLModel.getInstance().getXpathbyTitle(qzSourceName, qzSourcesType);
            getActivity().runOnUiThread(() -> updateView(homeEntities));
        });
    }


    private void updateView(List<HomeEntity> homeEntities) {
        setHasOptionsMenu(true);
        //下拉刷新颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorLogo1, R.color.colorLogo2, R.color.colorLogo3, R.color.colorLogo4);
        homeEntity = homeEntities.get(0);
        nowRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntity, qzStep, qzQuery, qzUrl);
        nextPage = nowRuleBean.getNextPageXpath();
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
        //RecyclerView的基本设置
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loadAnimation();
        //是否用htmlunit加载网页 是的话提示用户加载比较耗时
        if (AJAX.equals(Objects.requireNonNull(nowRuleBean.getIsAjax()))) {
            //提示需要耗时加载网页
            tvTishi.setVisibility(View.VISIBLE);
            tvTishi.setText("该源使用模拟浏览器加载，等待页面完全加载...");
        } else {
            tvTishi.setVisibility(View.GONE);
        }
        //如果是RSS
        String rssType = "{QZRSS}";
        if (homeEntity.getFirstListXpath().contains(rssType)) {
            isRss = true;
        }
        if (isRss) {
            RssModel = ViewModelProviders.of(this).get(RssViewModel.class);
        } else {
            XpathModel = ViewModelProviders.of(this).get(MainViewModel.class);
        }
        bindEvent();
    }


    private void bindEvent() {
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
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.postDelayed(() -> {
            page = 1;
            mHomeAdapter.removeAllFooterView();
            //开启下拉刷新
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
            if (isRss) {
                getRss();
            } else {

                getData();
            }
        }, 1000));

        //主适配器点击事件
        mHomeAdapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemClick: ");
            final DataEntity dataEntity = (DataEntity) adapter.getData().get(position);
            EventBus.getDefault().postSticky(new MsgEvent(111, mDataEntity, position));   //发送时间
            boolean result = MainViewClick.OnClick(getContext(), nowRuleBean, dataEntity, qzSpinnerSel, qzTitle);
            if (!result) {
                SnackbarUtil.ShortSnackbar(getView(),"该源配置不正确，无法进行下一步。",SnackbarUtil.Warning).show();

            }
        });

        //下拉加载更多
        if (!"".equals(nextPage)) {
            mHomeAdapter.setOnLoadMoreListener(() -> {


                    page += 1;
                    if (isRss) {
                        getRss();
                    } else {
                        xpathLoadMore(page);
                    }

            }, mRecyclerView);
        }
        mHomeAdapter.setLoadMoreView(new CustomLoadMoreView());
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
            case R.id.action_sort:
                //如果获取的数据不为空
                if (mDataEntity != null) {
                    //反转数据
                    Collections.reverse(mHomeAdapter.getData());
                    mHomeAdapter.notifyDataSetChanged();
                    mRecyclerView.scrollToPosition(0);
                    //保存数据的状态
                    if (POSITIVE.equals(SharedPreferencesUtils.readDataSort(Objects.requireNonNull(getContext())))) {
                        SharedPreferencesUtils.writeDataSort("反", getContext());

                    } else if (NEGATIVE.equals(SharedPreferencesUtils.readDataSort(getContext()))) {
                        SharedPreferencesUtils.writeDataSort("正", getContext());
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

        XpathModel.getProjects(homeEntity, qzStep, qzUrl, qzQuery, page).observe(this, this::updateView);
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
            RssModel.getArticleList().observe(this, this::updateView);
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
        //解除绑定
        bind.unbind();
        super.onDestroy();


    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "测试-->onDestroyView");
        super.onDestroyView();
        if (null != mRootView) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);

        }

    }

    /**
     * 根据设置加载不同动画
     */
    private void loadAnimation() {
        String loadAnimation = SharedPreferencesUtils.listLoadAnimationRead(getContext());
        switch (loadAnimation) {
            case "默认无":
                break;
            case "渐显":
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                break;
            case "缩放":
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                break;
            case "从下到上":
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
                break;
            case "从左到右":
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
                break;
            case "从右到左":
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
                break;
            case "每次随机":
                randomLoadAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * 随机加载动画
     */
    private void randomLoadAnimation() {
        int Animation = (int) (1 + Math.random() * (6 - 1 + 1));
        switch (Animation) {
            case 1:
                //默认无
                break;
            case 2:
                //渐显
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
                break;
            case 3:
                //缩放
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                break;
            case 4:
                //从下到上
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
                break;
            case 5:
                //从左到右
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
                break;
            case 6:
                //从右到左
                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
                break;
            default:
                break;
        }
    }


}