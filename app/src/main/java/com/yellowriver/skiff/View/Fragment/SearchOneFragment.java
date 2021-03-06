package com.yellowriver.skiff.View.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.SearchAdapter;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.HomeBean.MsgEvent;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.DataUtils.LocalUtils.SharedPreferencesUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.AnalysisUtils;
import com.yellowriver.skiff.Help.LogUtil;
import com.yellowriver.skiff.Help.MyLinearLayoutManager;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.ViewUtils.MainViewClick;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchOneFragment extends Fragment {
    private static final String TAG = "SearchOneFragment";
    @BindView(R.id.results_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_havelode)
    TextView tvHavelode;
    @BindView(R.id.tv_loadingsum)
    TextView tvLoadingsum;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.searchswipeRefreshLayout)
    SwipeRefreshLayout mSearchRefresh;


    /**
     * HomeAdapter
     */
    private SearchAdapter mHomeAdapter;
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
     * ???????????????
     */
    private String qzGroupName;
    /**
     * ?????????
     */
    private String qzSourcesType;
    /**
     * ???????????????
     */
    private String qzQuery;
    /**
     * ?????????????????????index
     */
    private int qzSpinnerSel;

    private List<String> sourcesList;
    /**
     * ????????????
     */
    private Unbinder bind;

    int loadsum = 1;

    /**
     * ??????UI
     */
    private void updateView(Vector<DataEntity> data, String fromSource) {
        loadsum++;

        mDataEntity = data;


        getActivity().runOnUiThread(() -> {
            if(mSearchRefresh!=null) {
                mSearchRefresh.setRefreshing(false);
            }
            if(tvHavelode!=null) {
                tvHavelode.setText("?????????(" + (loadsum - 1) + ")");
            }
            if(sourcesList!=null) {
                if (sourcesList.size() - loadsum < 0) {

                } else {
                    if(tvLoadingsum!=null) {
                        tvLoadingsum.setText("?????????(" + (sourcesList.size() - loadsum) + ")");

                    }
                }
            }

            if (mDataEntity != null) {
                if (mDataEntity.size() != 0) {
                    Log.d(TAG, "updateView: " + mDataEntity.size());

                    if (page == 1) {

                        if(getContext()!=null){
                            if (SharedPreferencesUtils.searchTypeRead(getContext()).equals("????????????")) {
                                //???????????????????????????
                                if (mHomeAdapter != null) {
                                    if (mHomeAdapter.getData() != null) {
                                        //?????????????????????
                                        List<DataEntity> haveDataEntities = mHomeAdapter.getData();
                                        for (int i = 0; i < haveDataEntities.size(); i++) {
                                            for (int j = 0; j < mDataEntity.size(); j++) {
                                                //??????????????????????????????
                                                try {
                                                    if (mDataEntity.get(j).getTitle().equals(haveDataEntities.get(i).getTitle())) {
                                                        //????????????
                                                        //????????????????????????????????????xxx???
                                                        try {
                                                            DataEntity havedataEntity = haveDataEntities.get(i);
                                                            String date = havedataEntity.getDate();
                                                            if (date != null) {
                                                                date += "," + fromSource;
//                                                if(haveDataEntities.get(i).getDate().indexOf("??????")!=-1){
//
//                                                }else{
//
//                                                    date += "," + fromSource;
//                                                }
                                                                String[] fromlist = date.split(",");
                                                                if (fromlist != null) {
                                                                    date = date.replace((fromlist.length - 1) + ")", (fromlist.length) + ")");
                                                                }
                                                                havedataEntity.setDate(date);
                                                            }

                                                            if (mHomeAdapter != null) {
                                                                mHomeAdapter.notifyItemChanged(i);
                                                            }
                                                            // mHomeAdapter.notifyItemChanged(i, fromSource);
                                                            mDataEntity.remove(j);
                                                        } catch (IndexOutOfBoundsException e) {
                                                        }
                                                    }
                                                } catch (ArrayIndexOutOfBoundsException e) {

                                                }
                                            }
                                        }
                                    } else {

                                    }
                                }

                            } else {

                            }
                        }
                        Vector<DataEntity> newDataEntity;
                        if(!SharedPreferencesUtils.searchTypeRead(getContext()).equals("????????????"))
                        {
                             newDataEntity = new Vector<>();
                        }else{
                            newDataEntity = null;
                        }
                        for (int i=0;i<mDataEntity.size();i++) {
                            DataEntity newdataEntity = mDataEntity.get(i);
                            String date = "";
                            if(SharedPreferencesUtils.searchTypeRead(getContext()).equals("????????????")) {
                                 date = "??????(" + 1 + "): " + fromSource;
                            }else {
                                date = "??????: " + fromSource;
                            }

                            newdataEntity.setDate(date);
                            newdataEntity.setFromSource(fromSource);
                            if(!SharedPreferencesUtils.searchTypeRead(getContext()).equals("????????????")){
                                if(newdataEntity.getTitle().equals(qzQuery)){
                                    newDataEntity.add(newdataEntity);
                                }else{

                                }
                            }else {

                            }
                        }
                        if(!SharedPreferencesUtils.searchTypeRead(getContext()).equals("????????????")){
                            if (mHomeAdapter != null) {
                                mHomeAdapter.addData(newDataEntity);
                            }
                        }else {
                            if (mHomeAdapter != null) {
                                mHomeAdapter.addData(mDataEntity);
                            }
                        }

                    } else {
                        if(mHomeAdapter!=null) {
                            mHomeAdapter.addData(mDataEntity);
                        }

                    }
                } else {

                    if((sourcesList.size() - loadsum)<=0&&mHomeAdapter.getData().size()==0){
                        if(mHomeAdapter!=null) {
                            View view = View.inflate(getContext(), R.layout.empty_layout, null);
                            mHomeAdapter.setEmptyView(view);
                        }
                    }

                }
            } else {


                if((sourcesList.size() - loadsum)<=0&&mHomeAdapter.getData()==null){
                    if(mHomeAdapter!=null) {
                        View view = View.inflate(getContext(), R.layout.empty_layout, null);
                        mHomeAdapter.setEmptyView(view);
                    }
                }

            }
        });
    }

    public static SearchOneFragment getInstance(String qzGroupName, String qzSourcesType, String qzQuery, int qzSpinnerSel) {
        SearchOneFragment searchOneFragment = new SearchOneFragment();
        Bundle args = new Bundle();
        args.putString("qzGroupName", qzGroupName);
        args.putString("qzSourcesType", qzSourcesType);
        args.putString("qzQuery", qzQuery);
        args.putInt("qzSpinnerSel", qzSpinnerSel);
        searchOneFragment.setArguments(args);
        return searchOneFragment;
    }

    public SearchOneFragment() {
        // Required empty public constructor
    }

    /**
     * ??????Fragment view
     */
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_search_one2, null);
            bindView(rootView);
            bindData();
            bindEvent();

        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @SuppressLint("ResourceType")
    private void bindView(View v) {
        bind = ButterKnife.bind(this, v);
        MyLinearLayoutManager myLinearLayoutManager = new MyLinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(myLinearLayoutManager);
        // RecyclerView???????????????
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setAdapter(mHomeAdapter = new SearchAdapter(R.layout.searchdata_vertical_item));
        //??????????????????
        if (mSearchRefresh != null) {
            mSearchRefresh.setColorSchemeResources(R.color.colorLogo1, R.color.colorLogo2, R.color.colorLogo3, R.color.colorLogo4);

        }
        mSearchRefresh.setEnabled(false);
    }


    private void bindData() {
        qzGroupName = Objects.requireNonNull(getArguments()).getString("qzGroupName");
        qzSourcesType = getArguments().getString("qzSourcesType");
        qzQuery = getArguments().getString("qzQuery");
        qzSpinnerSel = getArguments().getInt("qzSpinnerSel");


    }
    private void bindEvent()
    {
        mSearchRefresh.post(() -> {
            if (mSearchRefresh != null) {
                mSearchRefresh.setRefreshing(true);
                loadSqlData();
            }
        });

        itemClick();
    }

    private void itemClick() {


        //????????????????????????
        mHomeAdapter.setOnItemClickListener((adapter, view, position) -> {
            Log.d(TAG, "onItemClick: ");
            final DataEntity dataEntity = (DataEntity) adapter.getData().get(position);
            SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(() -> {
                List<HomeEntity> homeEntities = SQLModel.getInstance().getXpathbyTitle(dataEntity.getFromSource(), qzSourcesType);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(homeEntities!=null){
                            if(homeEntities.size()!=0){
                                homeEntity = homeEntities.get(0);
                                NowRuleBean nowRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntity, "1", qzQuery, dataEntity.getLink());
                                boolean finaltype = isFinalType(nowRuleBean,"1",homeEntities.get(0),dataEntity.getLink());
                                EventBus.getDefault().postSticky(new MsgEvent(111, mDataEntity, position));   //????????????
                                boolean result = MainViewClick.OnClick(getContext(), nowRuleBean, dataEntity, qzSpinnerSel, dataEntity.getTitle(), -1,finaltype,position);
                                if (!result) {
                                    SnackbarUtil.ShortSnackbar(getView(), "????????????????????????????????????????????????", SnackbarUtil.Warning).show();

                                }
                            }
                        }

                    }
                });
            });

        });
    }

    //???????????????????????????????????????
    private boolean isFinalType(NowRuleBean nowRuleBean,String step,HomeEntity homeEntity,String qzurl) {
        boolean finaltype = false;
        if (nowRuleBean.getLinkType().equals("1")) {
            //?????????????????????
            String nextStep = "";
            switch (step) {
                case "1":
                    nextStep = "2";
                    break;
                case "2":
                    nextStep = "3";
                    break;
            }
            NowRuleBean nextRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntity, nextStep, "", qzurl);
            String nextLinkType = nextRuleBean.getLinkType();
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


    //??????????????????????????????
    private void loadSqlData() {
        SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(() -> {
            sourcesList = SQLModel.getInstance().getTitleByGroup(qzGroupName, qzSourcesType);
            getActivity().runOnUiThread(() -> loadData());
        });
    }

    ThreadPoolExecutor threadPoolExecutor = null;

    //????????????
    private void loadData() {
        if (sourcesList != null) {

            if(sourcesList.size()==0){
                if(mSearchRefresh!=null) {
                    mSearchRefresh.setRefreshing(false);
                }
                if (getActivity() != null) {
                    if (mHomeAdapter != null) {
                        View view = View.inflate(getContext(), R.layout.empty_layout, null);
                        mHomeAdapter.setEmptyView(view);
                    }
                }
            }else {
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
                            List<HomeEntity> homeEntities = SQLModel.getInstance().getXpathbyTitle(sourcesList.get(finali), qzSourcesType);
                            if (homeEntities != null) {
                                homeEntity = homeEntities.get(0);
                                Vector<DataEntity> dataEntities = AnalysisUtils.getInstance().MainAnalysis(homeEntity, "1", "", qzQuery, page);


                                if (getActivity() != null) {

                                    updateView(dataEntities, sourcesList.get(finali));

                                } else {


                                }

                            }
                        }
                    };
                    if (!threadPoolExecutor.isShutdown()) {
                        threadPoolExecutor.execute(runnable);
                    }

                }
            }

        }


    }


    /**
     * ??????.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "??????-->onDestroy");
        super.onDestroy();
        //????????????
        LogUtil.info("TAG??????", "????????????");
        if(threadPoolExecutor!=null) {
            threadPoolExecutor.shutdown();
        }
        if (bind != null) {
            //????????????
            bind.unbind();
        }
        mHomeAdapter = null;
        if (null != rootView&&rootView.getParent()!=null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);

        }
        rootView = null;

    }
}
