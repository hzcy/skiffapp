package com.yellowriver.skiff.View.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.yellowriver.skiff.DataUtils.LocalUtils.MyImportSharedPreferences;
import com.yellowriver.skiff.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import skin.support.widget.SkinCompatButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavMyImpFragment extends Fragment {


    @BindView(R.id.titlexpath)
    EditText titlexpath;
    @BindView(R.id.linkxpath)
    EditText linkxpath;
    @BindView(R.id.coverxpath)
    EditText coverxpath;
    @BindView(R.id.sumamryxpath)
    EditText sumamryxpath;
    @BindView(R.id.datexpath)
    EditText datexpath;
    @BindView(R.id.nextpagexpath)
    EditText nextpagexpath;
    @BindView(R.id.titletext)
    TextView titletext;
    @BindView(R.id.linktext)
    TextView linktext;
    @BindView(R.id.covertext)
    TextView covertext;
    @BindView(R.id.summarytext)
    TextView summarytext;
    @BindView(R.id.datetext)
    TextView datetext;
    @BindView(R.id.nextpagetext)
    TextView nextpagetext;
    @BindView(R.id.textrun)
    AppCompatButton textrun;
    @BindView(R.id.buildsource)
    SkinCompatButton buildsource;
    @BindView(R.id.runbu)
    LinearLayout runbu;

    private String step;


    public static NavMyImpFragment getInstance(String step) {
        NavMyImpFragment navMyImpFragment = new NavMyImpFragment();
        Bundle args = new Bundle();
        args.putString("step", step);
        navMyImpFragment.setArguments(args);
        return navMyImpFragment;
    }


    public NavMyImpFragment() {
        // Required empty public constructor
    }

    /**
     * 绑定控件
     */
    private Unbinder bind;
    /**
     * 缓存view
     */
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mRootView == null) {

            mRootView = inflater.inflate(R.layout.fragment_nav_my_imp, container, false);
            bind = ButterKnife.bind(this, mRootView);
            initData();
        } else {

        }
        return mRootView;


    }

    private void initData() {
        step = getArguments().getString("step");
        String titlexpathStr = "";
        String linkxpathStr = "";
        String coverxpathStr = "";
        String summaryxpathStr = "";
        String datexpathStr = "";
        String nextpagexpathStr = "";
        try {
            titlexpathStr = MyImportSharedPreferences.readTitle(step, getContext());
            linkxpathStr = MyImportSharedPreferences.readLink(step, getContext());
            coverxpathStr = MyImportSharedPreferences.readCover(step, getContext());
            summaryxpathStr = MyImportSharedPreferences.readSummary(step, getContext());
            datexpathStr = MyImportSharedPreferences.readDate(step, getContext());
            nextpagexpathStr = MyImportSharedPreferences.readNextPage(step, getContext());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (titlexpathStr != null) {
            if (titlexpathStr.isEmpty()) {
                if (step.equals("第一步")) {
                    titlexpath.setText("标题不能为空");
                    titlexpath.setTextColor(Color.RED);
                } else {
                    titlexpath.setVisibility(View.GONE);
                    titletext.setVisibility(View.GONE);
                }
            } else {
                titlexpath.setVisibility(View.VISIBLE);
                titletext.setVisibility(View.VISIBLE);
                titlexpath.setText(titlexpathStr);
            }
        }

        if (linkxpathStr != null) {
            if (linkxpathStr.isEmpty()) {
                if (step.equals("第一步")) {
                    linkxpath.setText("链接不能为空");
                    linkxpath.setTextColor(Color.RED);
                } else {
                    linkxpath.setVisibility(View.GONE);
                    linktext.setVisibility(View.GONE);
                }
            } else {
                linkxpath.setVisibility(View.VISIBLE);
                linktext.setVisibility(View.VISIBLE);
                linkxpath.setText(linkxpathStr);
            }
        }



        if (coverxpathStr != null) {
            if (coverxpathStr.isEmpty()) {
                coverxpath.setVisibility(View.GONE);
                covertext.setVisibility(View.GONE);
            } else {
                coverxpath.setVisibility(View.VISIBLE);
                covertext.setVisibility(View.VISIBLE);
                coverxpath.setText(coverxpathStr);
            }
        }

        if (summaryxpathStr != null) {
            if (summaryxpathStr.isEmpty()) {
                sumamryxpath.setVisibility(View.GONE);
                summarytext.setVisibility(View.GONE);
            } else {
                sumamryxpath.setVisibility(View.VISIBLE);
                summarytext.setVisibility(View.VISIBLE);
                sumamryxpath.setText(summaryxpathStr);
            }
        }

        if (datexpathStr != null) {
            if (datexpathStr.isEmpty()) {
                datexpath.setVisibility(View.GONE);
                datetext.setVisibility(View.GONE);
            } else {
                datexpath.setVisibility(View.VISIBLE);
                datetext.setVisibility(View.VISIBLE);
                datexpath.setText(datexpathStr);
            }
        }

        if (nextpagexpathStr != null) {
            if (nextpagexpathStr.isEmpty()) {
                nextpagexpath.setVisibility(View.GONE);
                nextpagetext.setVisibility(View.GONE);
            } else {
                nextpagexpath.setVisibility(View.VISIBLE);
                nextpagetext.setVisibility(View.VISIBLE);
                nextpagexpath.setText(nextpagexpathStr);
            }
        }

        if(step.equals("第一步")){
            runbu.setVisibility(View.VISIBLE);
            textrun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //测试
                }
            });
            buildsource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //生成源
                }
            });
        }else{
            runbu.setVisibility(View.GONE);
        }

    }

    /**
     * 销毁.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除绑定
        if (bind != null) {
            //解除绑定
            bind.unbind();
        }
        if (null != mRootView && mRootView.getParent() != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);

        }
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();

        if (null != mRootView && mRootView.getParent() != null) {
            ((ViewGroup) mRootView.getParent()).removeView(mRootView);

        }
        mRootView = null;
    }

    @OnClick({R.id.textrun, R.id.buildsource})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textrun:
                break;
            case R.id.buildsource:
                break;
        }
    }
}
