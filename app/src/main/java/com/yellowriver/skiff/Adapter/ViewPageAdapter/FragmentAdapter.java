package com.yellowriver.skiff.Adapter.ViewPageAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *  后面优化要用
 * @author huang
 */
public class FragmentAdapter  extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> mTitles;
    private FragmentManager fm;

    public FragmentAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragmentList) {
        super(fm);
        this.fm = fm;
        this.mTitles = titles;
        this.fragmentList = fragmentList;

    }

//    @Override
//    public Fragment getItem(int position) {
//        Fragment fragment = fragmentList.get(position);
//        Bundle bundle = new Bundle();
//        bundle.putString("id","" + position);
//        fragment.setArguments(bundle);
//        Log.d("ddd", "getItem: "+fragmentList.size());
//        return fragment;
//    }


    @NotNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.size() != 0 ? fragmentList.get(position) : null;
    }


    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.size() != 0 ? mTitles.get(position) : "";
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 将实例化的fragment进行显示即可。
        Fragment fragment = (Fragment) super.instantiateItem(container, position);

        fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);// 注释父类方法
        Fragment fragment = fragmentList.get(position);// 获取要销毁的fragment
        fm.beginTransaction().hide(fragment).commit();// 将其隐藏即可，并不需要真正销毁，这样fragment状态就得到了保存
    }
}
