package com.yellowriver.skiff.View.Fragment.Favorite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yellowriver.skiff.Adapter.RecyclerViewAdapter.FavoriteAdapter;
import com.yellowriver.skiff.Bean.DataBaseBean.FavoriteEntity;
import com.yellowriver.skiff.Bean.DataBaseBean.HomeEntity;
import com.yellowriver.skiff.Bean.HomeBean.DataEntity;
import com.yellowriver.skiff.Bean.HomeBean.NowRuleBean;
import com.yellowriver.skiff.DataUtils.LocalUtils.SQLiteUtils;
import com.yellowriver.skiff.DataUtils.RemoteUtils.AnalysisUtils;
import com.yellowriver.skiff.Help.MyLinearLayoutManager;
import com.yellowriver.skiff.Help.SnackbarUtil;
import com.yellowriver.skiff.Model.SQLModel;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.ViewUtils.MainViewClick;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 收藏页面
 *
 * @author huang
 */
public class FavoriteDataFragment extends Fragment {

    private static final String TAG = "HomeDataViewFragment";

    @BindView(R.id.results_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * FavoriteAdapter
     */
    private FavoriteAdapter mFavoriteAdapter;


    /**
     * 绑定控件
     */
    private Unbinder bind;

    /**
     * 获取的收藏数据
     */
    private List<FavoriteEntity> mFavoriteEntity;

    private String qzGroupName;

    public static FavoriteDataFragment newInstance(String qzGroupName) {
        Bundle args = new Bundle();


        FavoriteDataFragment favoriteDataFragment = new FavoriteDataFragment();

        args.putString("qzGroupName", qzGroupName);

        favoriteDataFragment.setArguments(args);
        return favoriteDataFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favorite_data_fragment, container, false);
        bindView(v);
        bindEvent();
        return v;
    }


    private void bindView(View v) {
        bind = ButterKnife.bind(this, v);
        qzGroupName = Objects.requireNonNull(getArguments()).getString("qzGroupName");
        //下拉刷新颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorLogo1, R.color.colorLogo2, R.color.colorLogo3,R.color.colorLogo4);
        //垂直显示
        MyLinearLayoutManager myLinearLayoutManager;
        //标题 垂直
        myLinearLayoutManager = new MyLinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(myLinearLayoutManager);
        //RecyclerView的基本设置
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFavoriteAdapter = new FavoriteAdapter(R.layout.maindata_vertical_item));
    }


    private void bindEvent()
    {
        //进入界面开始加载数据
        mSwipeRefreshLayout.post(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getData(qzGroupName);
        });
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.postDelayed(() -> {
            mSwipeRefreshLayout.setRefreshing(true);
            getData(qzGroupName);
        }, 1000));
        mFavoriteAdapter.setOnItemClickListener((adapter, view, position) -> {
            final FavoriteEntity dataEntity = (FavoriteEntity) adapter.getData().get(position);
            DataEntity dataEntity1 = new DataEntity(dataEntity.getTitle(), dataEntity.getSummary(), dataEntity.getCover(), dataEntity.getLink(), dataEntity.getDate(), "1", "1");
            SQLiteUtils.getInstance().getDaoSession().startAsyncSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    List<HomeEntity> homeEntities = SQLModel.getInstance().getXpathbyTitle(dataEntity.getSourcesName(), dataEntity.getType());
                    NowRuleBean nowRuleBean = AnalysisUtils.getInstance().getValueByStep(homeEntities.get(0), dataEntity.getStep(), "", dataEntity.getLink());
                    getActivity().runOnUiThread(() -> {
                        boolean result = MainViewClick.OnClick(getContext(), nowRuleBean, dataEntity1, dataEntity.getSpinnerSel(), dataEntity.getTitle(),dataEntity.getReadIndex());
                        if (!result) {

                            SnackbarUtil.ShortSnackbar(getView(),"该源配置不正确，无法进行下一步。",SnackbarUtil.Warning).show();
                        }
                    });
                }
            });
        });
        mFavoriteAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            final FavoriteEntity dataEntity = (FavoriteEntity) adapter.getData().get(position);
            SQLModel.getInstance().delFavorite(dataEntity);
            List<FavoriteEntity> favoriteEntities = SQLModel.getInstance().getFavoritebyTitle(dataEntity.getTitle(), dataEntity.getSourcesName());
            if (favoriteEntities.isEmpty()) {
                SnackbarUtil.ShortSnackbar(getView(),"删除成功！",SnackbarUtil.Confirm).show();

                mSwipeRefreshLayout.post(() -> {
                    mSwipeRefreshLayout.setRefreshing(true);
                    getData(qzGroupName);
                });
            } else {
                SnackbarUtil.ShortSnackbar(getView(),"删除失败！",SnackbarUtil.Warning).show();


            }
        });
    }


    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    private void getData(String group) {
        mFavoriteEntity = SQLModel.getInstance().getFavoritebyGroup(group);
        if (mFavoriteEntity != null) {
            if (mFavoriteEntity.size() != 0) {
                mSwipeRefreshLayout.setRefreshing(false);
                mRecyclerView.setVisibility(View.VISIBLE);
                mFavoriteAdapter.setNewData(mFavoriteEntity);
            } else {
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "测试-->onDestroy");
        super.onDestroy();
        //解除绑定
        bind.unbind();

    }

}
