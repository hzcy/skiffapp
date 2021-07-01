package com.yellowriver.skiff.Help;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.yellowriver.skiff.Adapter.ViewPageAdapter.FragmentAdapter;
import com.yellowriver.skiff.R;
import com.yellowriver.skiff.View.Fragment.ListSaveFragment;

import java.util.ArrayList;
import java.util.List;

import skin.support.flycotablayout.widget.SkinSlidingTabLayout;

public class BuyerLiveGoodsDialog extends DialogFragment {

    private SkinSlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView ivClose;
    private int height;

    FragmentAdapter contentAdapter;
    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private String type;
    private String xpath;
    AlertDialog dlg;


    public BuyerLiveGoodsDialog(String type, String xpath, AlertDialog dlg)
    {
        this.type = type;
        this.xpath = xpath;
        this.dlg = dlg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        height = (int) (display.getHeight() * 0.3);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.savetext_dialog, container, false);
        tabLayout = view.findViewById(R.id.tl_search);
        viewPager = view.findViewById(R.id.view_pager);
//            ivClose = view.findViewById(R.id.iv_cancel);
//            ivClose.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismiss();
//                }
//            });

        tabIndicators = new ArrayList<>();
        tabIndicators.add("第一步");
        tabIndicators.add("第二步");
        tabIndicators.add("第三步");
        tabFragments = new ArrayList<>();
        for (String title : tabIndicators) {
            //第一步
            tabFragments.add(new ListSaveFragment(title,type,xpath,dlg));
        }
        try {
            contentAdapter = new FragmentAdapter(getChildFragmentManager(), tabIndicators, tabFragments);

        }catch (IllegalStateException e){
            e.printStackTrace();

        }
       // tabLayout.setLayoutMode(View);
        //BuyerLiveGoodsPageAdapter pageAdapter = new BuyerLiveGoodsPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(contentAdapter);
        tabLayout.setViewPager(viewPager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        if (window != null) {
            // 一定要设置Background，如果不设置，window属性设置无效
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            DisplayMetrics dm = new DisplayMetrics();
            if (getActivity() != null) {
                WindowManager windowManager = getActivity().getWindowManager();
                if (windowManager != null) {
                    windowManager.getDefaultDisplay().getMetrics(dm);
                    WindowManager.LayoutParams params = window.getAttributes();
                    params.gravity = Gravity.BOTTOM;
                    // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = height;
                    window.setAttributes(params);
                }
            }
        }
    }


    public static void showDialog(FragmentManager fragmentManager,String title,String xpath,AlertDialog dlg) {

        BuyerLiveGoodsDialog dialog = new BuyerLiveGoodsDialog(title,xpath,dlg);
        dialog.show(fragmentManager, "savetag");
    }



}
