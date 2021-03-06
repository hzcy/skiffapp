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

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.HomeAdapter;
import com.yellowriver.skiff.Adapter.TreeAdapter.NodeAdapter;
import com.yellowriver.skiff.Adapter.ViewPageAdapter.ContentPagerAdapter;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.SourcesBean.GroupEntity;
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
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * ????????? ?????????????????????
 *
 * @author huang
 * @date 2019
 */
public class RemoteSourceFragment extends Fragment {
    String baseurl = "https://skiff-d3a.pages.dev/api/sources";
    private static String TAG = "RemoteSourceFragment";

    @BindView(R.id.results_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isPrepared = false;
    /**
     * ?????????????????????
     */
    private String title;
    //private GroupAdapter mGroupAdapter;

    private Vector<DataEntity> mDataEntity;
    private int page = 1;
    private int sourcesCount = 0;
    private HomeAdapter mHomeAdapter;
    private static Gson gson = new Gson();

    private String qzGroupName;

    RemoteSourceModel remoteSourceModel;

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    /**
     * ????????????
     */
    private Unbinder bind;


    private void updateView( Vector<DataEntity> list) {

        mSwipeRefreshLayout.setRefreshing(false);
        //?????????????????????????????????cash
        sourcesCount = 0;

        if (page == 1) {
            if (list != null) {
                if (list.size() != 0) {
                    Log.d(TAG, "updateView: "+list.size());

                    if (mHomeAdapter != null) {
                        int i = 0;
                        //mAdapter.setPageTitle(i, title + "???" + sourcesCount + "???");
                    }
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mHomeAdapter.setNewData(list);

                } else {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }
        }

    }


    public static RemoteSourceFragment getInstance(String qzGroupName) {
        RemoteSourceFragment sourceDataViewFragment = new RemoteSourceFragment();
        Bundle args = new Bundle();
        args.putString("qzGroupName", qzGroupName);
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
        //??????view
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
        //RecyclerView??????
        MyLinearLayoutManager myLinearLayoutManager;
        myLinearLayoutManager = new MyLinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(myLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mHomeAdapter = new HomeAdapter(R.layout.maindata_vertical_item));
       // mGroupAdapter.expandAll();
    }

    private void bindData() {
        qzGroupName = requireArguments().getString("qzGroupName");


        title = requireArguments().getString("title");

        new Thread(new Runnable() {
            @Override
            public void run() {

                String url = baseurl + "/" +qzGroupName+"/list.json";
                Log.d(TAG, "run: "+url);
                String json = NetUtils.getInstance().getRequest(url, "1");

                mDataEntity = new Vector<>();

                Log.d(TAG, "run: "+json);

                List<SourcesEntity> sourceBean = null;
                if (JsonUtils.isJSONValid(json)) {
                    // Log.d(TAG, "getSourceAllGroup: ???json");

                    Type type = new TypeToken<List<SourcesEntity>>() {
                    }.getType();
                    sourceBean = gson.fromJson(json, type);
                    if (sourceBean!=null) {
                        for (int i = 0; i < sourceBean.size(); i++) {
                            sourceBean.get(i).getName();
                            DataEntity dataEntity = new DataEntity();
                            dataEntity.setTitle(sourceBean.get(i).getName());
                            dataEntity.setLink(sourceBean.get(i).getLink());
                            mDataEntity.add(dataEntity);
                        }
                        getActivity().runOnUiThread(() -> updateView(mDataEntity));
                    }

                }
            }
        }).start();
    }

    private void getData() {


    }


    private void bindEvent() {
        firstLoadData();
        swipeRefresh();
//        groupAddAndDelete();
//        sourceAddAndDelete();
        SharedPreferencesUtils.dataChangeSource(
                false, getContext());
    }

    //????????????
    private void swipeRefresh()
    {
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.postDelayed(() -> {
            page = 1;
            //??????????????????
            mSwipeRefreshLayout.setRefreshing(true);

            getData();
        }, 1000));
    }

    //??????????????????????????????
    private void firstLoadData()
    {
        mSwipeRefreshLayout.post(() -> {
            page = 1;
            //??????????????????
            mSwipeRefreshLayout.setRefreshing(true);
            getData();
        });
    }

    //?????? ?????????????????? ????????? ???????????? ????????? ????????????
    private void groupAddAndDelete()
    {
        mHomeAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (adapter.getItemViewType(position)) {
                    case TYPE_LEVEL_0:
                        final group group = (group) adapter.getData().get(position);
                        String groupname = group.getGroupName();
                        String grouplink = group.getGroupLink();
                        if (group!=null) {
                            if (group.getChildNode()!=null) {
                                int size = group.getChildNode().size();

                                if ("1".equals(group.getGroupIshave())) {
                                    SnackbarUtil.ShortSnackbar(getView(), "????????????????????????", SnackbarUtil.Warning).show();
                                } else {
                                    addAlert(groupname, size, TYPE_LEVEL_0, baseurl + grouplink, position);
                                }
                            }

                        }
                        break;
                    case TYPE_LEVEL_1:
                    default:
                        break;
                }
            }
        });
    }

    //?????? ??????????????? ????????? ????????? ????????? ?????????
    private void sourceAddAndDelete()
    {
        mHomeAdapter.setOnItemClickListener(new OnItemClickListener() {
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
                            SnackbarUtil.ShortSnackbar(getView(),"???????????????",SnackbarUtil.Warning).show();
                        } else {
                            //???????????????
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
                new AlertDialog.Builder(requireContext());
        if (TYPE_LEVEL == 0) {
            normalDialog.setTitle("????????????");
            normalDialog.setMessage("??????????????????" + title + "?????????????????????????" + size + "??????");
        } else {
            normalDialog.setTitle("?????????");
            normalDialog.setMessage("??????????????????" + title + "???????");
        }
        normalDialog.setPositiveButton("??????", (dialogInterface, i) -> {

            ProgressDialog waitingDialog =
                    new ProgressDialog(getContext());
            waitingDialog.setTitle("??????");
            waitingDialog.setMessage("?????????...");
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
                        // ??????????????????
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
                                    //??????
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
                                SnackbarUtil.ShortSnackbar(getView(),"??????????????????",SnackbarUtil.Confirm).show();
                                SharedPreferencesUtils.dataChange(true, getContext());
                                SharedPreferencesUtils.dataChangeSource(true, getContext());
                                try {
//                                    int positionAtAll = mGroupAdapter.getParentPositionInAll(position);
//                                    mGroupAdapter.notifyItemChanged(position, "addok");
//                                    if (positionAtAll != -1) {
//                                        IExpandable multiItemEntity = (IExpandable) mGroupAdapter.getData().get(positionAtAll);
//                                        if (!mGroupAdapter.hasSubItems(multiItemEntity)) {
//                                            mGroupAdapter.notifyItemChanged(positionAtAll, "addok");
//                                        }
//                                    }
                                }catch (IndexOutOfBoundsException e)
                                {

                                }
                            }else{
                                SnackbarUtil.ShortSnackbar(getView(),"??????????????????",SnackbarUtil.Alert).show();
                            }
                        }
                    });
                }
            };
            singleThreadPool.execute(runnable);

        }).setNegativeButton("??????", null).show();

    }

    /**
     * ??????.
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "??????-->onDestroy");
        super.onDestroy();
        if (bind!=null) {
            //????????????
            bind.unbind();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepared && isVisibleToUser) {
            //????????????
            if(SharedPreferencesUtils.readdataChangeSource(getContext()))
            {
                getData();
                SharedPreferencesUtils.dataChangeSource(
                        false, getContext());
            }
        }
    }

}
