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
 * ??????????????????view
 *
 * @author huang
 * @date 2019
 */
public class HomeDataViewFragment extends Fragment {

    private static final String AJAX = "2";
    private static final String POSITIVE = "???";
    private static final String NEGATIVE = "???";


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
     * ???????????????  ?????????????????????
     */
    private HomeEntity homeEntity;
    /**
     * ??????page
     */
    private int page = 1;
    /**
     * ?????????????????????
     */
    private Vector<DataEntity> mDataEntity;
    /**
     * ?????????RSS???
     */
    private boolean isRss = false;
    /**
     * ?????????url
     */
    private String nextPage = "";


    /**
     * ?????????????????????
     */
    private String qzSourceName;
    /**
     * ??????????????????????????????
     */
    private String qzStep;
    /**
     * ????????????url
     */
    private String qzUrl;
    /**
     * ??????????????????
     */
    private String qzSourcesType;
    /**
     * ???????????????
     */
    private String qzQuery;
    /**
     * ??????????????????
     */
    private int qzSpinnerSel;
    private int readIndex;
    /**
     * ????????????
     */
    private String qzGroupName;
    /**
     * ????????????
     */
    private String qzTitle;
    /**
     * ???????????????????????????????????????
     */
    NowRuleBean nowRuleBean;
    /**
     * ????????????
     */
    private Unbinder bind;
    /**
     * ??????view
     */
    private View mRootView;
    /**
     * XpathModel ??? RssModel
     */
    MainViewModel XpathModel;
    RssViewModel RssModel;

    /**
     * ???????????????????????????UI
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
                                    dataEntity.setColor("??????");
                                    mDataEntity.remove(readIndex);
                                    mDataEntity.add(readIndex, dataEntity);
                                } catch (ArrayIndexOutOfBoundsException e) {

                                }
                            }
                        }
                        if (nowRuleBean != null) {
                            if (!nowRuleBean.getReadNextPage().isEmpty()) {
                                if (nowRuleBean.getReadNextPage().indexOf("???")!=-1) {

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
                    //???????????????
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
                //???????????????
                if(mHomeAdapter!=null) {
                    View view = View.inflate(getContext(), R.layout.empty_layout, null);
                    mHomeAdapter.setEmptyView(view);
                }
            }
        });
    }


    /**
     * @param qzGroupName   ?????????
     * @param qzStep        ?????????
     * @param qzUrl         ??????
     * @param qzSourcesType ??????  ?????? ??? ??????
     * @param qzQuery       ???????????????
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
        Log.d(TAG, "??????-->onCreateView");
        if (mRootView == null) {
            Log.d(TAG, "??????-->?????????view");
            mRootView = inflater.inflate(R.layout.fragment_home_data_view, container, false);
            bind = ButterKnife.bind(this, mRootView);
            bindData();
        } else {
            Log.d(TAG, "??????-->?????????view");
        }
        return mRootView;
    }


    private void bindData() {
        //???????????????????????????
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
        //??????????????????
        SharedPreferencesUtils.writeDataSort("???", requireContext());
        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(() -> {
            List<HomeEntity> homeEntities = SQLModel.getInstance().getXpathbyTitle(qzSourceName, qzSourcesType);
            getActivity().runOnUiThread(() -> updateView2(homeEntities));
        });
    }


    //??????????????????????????????ui
    private void updateView2(List<HomeEntity> homeEntities) {
        setHasOptionsMenu(true);
        //??????????????????
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorLogo1, R.color.colorLogo2, R.color.colorLogo3, R.color.colorLogo4);

        }
        if(homeEntities!=null) {
            homeEntity = homeEntities.get(0);
            nowRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntity, qzStep, qzQuery, qzUrl);
            nextPage = nowRuleBean.getNextPageXpath();
            //?????????????????? ?????? ??? ??????
            if (mRecyclerView != null) {
                viewType();

                //RecyclerView???????????????
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                //??????????????????????????????
                //loadAnimation();
            }
            //???htmlunit?????? ?????? ???????????????
            loadMode();
            //??????xpath???json?????????viewmode ??????rss????????????viewmodel
            loadModel();
            //????????????
            bindEvent();
        }
    }

    //????????????
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

    //????????????
    private void bindEvent() {
        firstLoadData();
        swipeRefresh();
        itemClick();
        loadMore();
    }

    //??????????????????
    private void firstLoadData() {
        //??????????????????????????????
        mSwipeRefreshLayout.post(() -> {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(true);
                if (isRss) {
                    getRss();
                } else {
                    //?????????viewModel
                    getData();
                }
            }
        });
    }

    //????????????
    private void swipeRefresh() {
        //????????????
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.postDelayed(() -> {
            page = 1;
            //??????????????????
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
        //??????????????????
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
        //????????????????????????
        mHomeAdapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemClick: ");
            final DataEntity dataEntity = (DataEntity) adapter.getData().get(position);
            EventBus.getDefault().postSticky(new MsgEvent(111, mDataEntity, position));   //????????????

            boolean finaltype = isFinalType();
            boolean result = MainViewClick.OnClick(getContext(), nowRuleBean, dataEntity, qzSpinnerSel, qzTitle, readIndex, finaltype,position);
            if (!result) {
                SnackbarUtil.ShortSnackbar(getView(), "????????????????????????????????????????????????", SnackbarUtil.Warning).show();

            }

        });
    }

    /**
     * ***********************************************************???????????????
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
                //??????????????????
                showBottomSheetDialog();
                return true;
            case R.id.action_sort:
                //??????????????????????????????
                if (mDataEntity != null) {
                    //????????????
//                    Collections.reverse(mHomeAdapter.getData());
//                    mHomeAdapter.notifyDataSetChanged();
//                    mRecyclerView.scrollToPosition(0);
                    //?????????????????????
                    if (POSITIVE.equals(SharedPreferencesUtils.readDataSort(requireContext()))) {
                        SharedPreferencesUtils.writeDataSort("???", getContext());
                        mRecyclerView.scrollToPosition(mHomeAdapter.getData().size());
                        Log.d(TAG, "onOptionsItemSelected: " + mHomeAdapter.getData().size());
                        if (item.getTitle().equals("???????????????")) {
                            item.setTitle("????????????");
                        }

                    } else if (NEGATIVE.equals(SharedPreferencesUtils.readDataSort(getContext()))) {
                        SharedPreferencesUtils.writeDataSort("???", getContext());
                        mRecyclerView.scrollToPosition(0);
                        if (item.getTitle().equals("????????????")) {
                            item.setTitle("???????????????");
                        }

                    }
                }
                return true;
            default:
                return false;
        }
    }
    //***********************************************************???????????????


    /**
     * ??????xpath??????
     */
    private void getData() {

        XpathModel.getProjects(homeEntity, qzStep, qzUrl, qzQuery, page).observe(getViewLifecycleOwner(), this::updateView);
        xpathReload();
    }

    //xpath?????????????????????????????????
    private void xpathReload() {
        XpathModel.reload();
    }

    //xpath?????????????????? ???page>1???
    private void xpathLoadMore(int page) {
        XpathModel.loadMore(page);
    }

    //RSS??????
    private void getRss() throws IllegalStateException {

        if ("1".equals(qzStep)) {
            RssModel.getArticleList().observe(getActivity(), this::updateView);
            rssReload();
        }
    }

    //rss?????????????????????????????????
    private void rssReload() {
        RssModel.fetchFeed(homeEntity.getFirsturl());
    }


    @Override
    public void onStart() {    //??????
        super.onStart();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {    //?????????
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "??????-->onDestroy");
        super.onDestroy();
        if (bind != null) {
            //????????????
            bind.unbind();
        }

        //????????????
        if (threadPoolExecutor != null) {
            LogUtil.info("TAG??????", "????????????");
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
        Log.d(TAG, "??????-->onDestroyView");
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
     * ??????????????????????????????
     */
//    private void loadAnimation() {
//        String loadAnimation = SharedPreferencesUtils.listLoadAnimationRead(getContext());
//        switch (loadAnimation) {
//            case "?????????":
//                break;
//            case "??????":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
//                break;
//            case "??????":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//                break;
//            case "????????????":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//                break;
//            case "????????????":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//                break;
//            case "????????????":
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
//                break;
//            case "????????????":
//                randomLoadAnimation();
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * ??????????????????
     */
//    private void randomLoadAnimation() {
//        int Animation = (int) (1 + Math.random() * (6 - 1 + 1));
//        switch (Animation) {
//            case 1:
//                //?????????
//                break;
//            case 2:
//                //??????
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
//                break;
//            case 3:
//                //??????
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
//                break;
//            case 4:
//                //????????????
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//                break;
//            case 5:
//                //????????????
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//                break;
//            case 6:
//                //????????????
//                mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
//                break;
//            default:
//                break;
//        }
//    }

    //?????????????????????????????????
    private void viewType() {
        //????????????
        MyLinearLayoutManager myLinearLayoutManager;
        //????????????????????????  1??????????????? 2?????????????????????
        switch (Objects.requireNonNull(nowRuleBean.getViewMode())) {
            case "1":
                //?????? ??????
                myLinearLayoutManager = new MyLinearLayoutManager(getContext());
                if (myLinearLayoutManager != null && mRecyclerView != null) {
                    mRecyclerView.setLayoutManager(myLinearLayoutManager);
                }
                mRecyclerView.setAdapter(mHomeAdapter = new HomeAdapter(R.layout.maindata_vertical_item));
                break;
            case "2":
                //?????? ?????? 2
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                mRecyclerView.setAdapter(mHomeAdapter = new HomeAdapter(R.layout.maindata_horizontal_item));
                break;
            case "3":
                //?????? ?????? 3
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

    //????????????  ?????????
    private void loadMode() {
        //?????????htmlunit???????????? ???????????????????????????????????????
        if(tvTishi!=null) {
            if (AJAX.equals(Objects.requireNonNull(nowRuleBean.getIsAjax()))) {
                //??????????????????????????????
                tvTishi.setVisibility(View.VISIBLE);
                tvTishi.setText("????????????????????????????????????????????????????????????...");
            } else {
                tvTishi.setVisibility(View.GONE);
            }
        }
    }

    //*************************************************????????????************************
    SearchAdapter mSearchAdapter;
    RecyclerView mSearchRecyclerView;
    private List<String> sourcesList;
    /**
     * ?????????????????????
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
        //??????????????????
        dialog.create(getActivity().getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        // // You can do any of the necessary the operation with the view
                        mSearchRefresh = v.findViewById(R.id.searchswipeRefreshLayout);
                        //??????????????????
                        if (mSearchRefresh != null) {
                            mSearchRefresh.setColorSchemeResources(R.color.colorLogo1, R.color.colorLogo2, R.color.colorLogo3, R.color.colorLogo4);

                        }
                        textView = v.findViewById(R.id.searchtextview);
                        if (textView != null) {
                            textView.setText("??????'" + qzTitle + "'");
                        }
                        mSearchRefresh.setEnabled(false);
                        mSearchRecyclerView = v.findViewById(R.id.results_list);
                        MyLinearLayoutManager myLinearLayoutManager = new MyLinearLayoutManager(getContext());
                        if (mSearchRecyclerView != null) {
                            mSearchRecyclerView.setLayoutManager(myLinearLayoutManager);
                            // RecyclerView???????????????
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

    //??????????????????
    private void loadData() {
        if (sourcesList != null) {
            //LogUtil.info(TAG, "loadData: ????????????????????????????????????" + sourcesList.size());
            //?????????????????????
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
                            Log.d(TAG, "run: ???????????????" + qzQuery + "--" + qzTitle);
                            //  LogUtil.info("TAG", "run : " + finali + "???????????????" + sourcesList.get(finali).toString() + "  ???????????????" + Thread.currentThread().getName());
                            if (dataEntities != null) {
                                if (getActivity() != null) {
                                    // LogUtil.info("TAG??????","??????fraf");
                                    updateViewChangeSource(dataEntities, sourcesList.get(finali));
                                } else {
                                    // LogUtil.info("TAG??????","?????????fraf");

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
     * ????????????UI
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

                            Log.d(TAG, "updateViewChangeSource: ??????" + qzSourceName);
                            Log.d(TAG, "updateViewChangeSource: ?????????" + fromSource);

                            newdataEntity.setDate("??????: " + fromSource);
                            newdataEntity.setFromSource(fromSource);

                            if (newdataEntity.getTitle().equals(qzTitle)) {
                                // Log.d(TAG, "updateViewChangeSource: ??????????????????");
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
                                textView.setText("??????'" + qzTitle + "'" + "(" + mSearchAdapter.getData().size() + ")");
                            }
                        }

                    } else {
                        Vector<DataEntity> newDataEntity = new Vector<>();
                        for (int i = 0; i < mSearchData.size(); i++) {
                            DataEntity newdataEntity = mSearchData.get(i);

                            //Log.d(TAG, "updateViewChangeSource: ??????"+qzTitle);
                            //Log.d(TAG, "updateViewChangeSource: ?????????"+newdataEntity.getTitle());

                            newdataEntity.setDate("??????: " + fromSource);
                            newdataEntity.setFromSource(fromSource);
                            if (!newdataEntity.getTitle().equals(qzTitle)) {
                                // Log.d(TAG, "updateViewChangeSource: ??????????????????");
                                newDataEntity.add(newdataEntity);
                            }
                        }
                        if (mSearchRefresh != null) {
                            mSearchRefresh.setRefreshing(false);
                        }
                        if (mSearchAdapter != null) {
                            mSearchAdapter.addData(newDataEntity);
                            if (textView != null) {
                                textView.setText("??????'" + qzTitle + "'" + "(" + mSearchAdapter.getData().size() + ")");
                            }
                        }

                    }
                } else {
                    if (mSearchRefresh != null) {
                        mSearchRefresh.setRefreshing(false);
                    }
                    //???????????????
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


    //??????????????????
    private void searchItemClick() {
        //????????????????????????
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
                            //????????????????????? ???????????????
                        } else {
                            EventBus.getDefault().postSticky(new MsgEvent(111, mDataEntity, position));   //????????????
                            boolean finaltype = isFinalTypebySearch();
                            boolean result = MainViewClick.OnClick(getContext(), nowRuleBean, dataEntity, qzSpinnerSel, dataEntity.getTitle(), readIndex, finaltype,position);
                            if (!result) {
                                SnackbarUtil.ShortSnackbar(getView(), "????????????????????????????????????????????????", SnackbarUtil.Warning).show();
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
        LogUtil.info("????????????","?????????"+nextLinkType);
        if (!nextLinkType.equals("1")&&!nextLinkType.equals("8")) {
            //???????????????????????? ??????????????????????????? ?????? ??????????????????activity
            finaltype = true;
        } else {
            finaltype = false;
        }
        return finaltype;
    }
    //???????????????????????????????????????
    private boolean isFinalType() {
        boolean finaltype = false;
        String nextStep = "";
        LogUtil.info("????????????","?????????"+nowRuleBean.getLinkType());
        if (nowRuleBean.getLinkType().equals("1")) {
            //?????????????????????

            switch (qzStep) {
                case "1":
                    nextStep = "2";
                    break;
                case "2":
                    nextStep = "3";
                    break;
            }

            LogUtil.info("????????????","?????????"+nextStep);
            NowRuleBean nextRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntity, nextStep, qzQuery, qzUrl);

            String nextLinkType = nextRuleBean.getLinkType();
            LogUtil.info("????????????","?????????"+nextLinkType);
            if (!nextLinkType.equals("1")&&!nextLinkType.equals("8")) {
                //???????????????????????? ??????????????????????????? ?????? ??????????????????activity
                finaltype = true;
            } else {
                finaltype = false;
            }
        } else {
            finaltype = false;
        }
        return finaltype;
    }


    //????????????????????????
    private void destroySearch() {
        if (threadPoolExecutor != null) {
            LogUtil.info("TAG??????", "????????????");
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
