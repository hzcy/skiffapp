package com.yellowriver.skiff.Adapter.ViewPageAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author huang
 */
public class ContentPagerAdapter extends FragmentPagerAdapter {
    private List<String> mTitles;
    private List<Fragment> tabFragments;
    public ContentPagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> tabFragments) {
        super(fm);
        this.mTitles = titles;
        this.tabFragments = tabFragments;

    }



        @NotNull
    @Override
    public Fragment getItem(int position) {
        return tabFragments.size() != 0 ? tabFragments.get(position) : null;
    }

    @Override
    public int getCount() {
        return tabFragments == null ? 0 : tabFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.size() != 0 ? mTitles.get(position) : "";
    }

    // 动态设置我们标题的方法
    public void setPageTitle(int position, String title)
    {
        if(position >= 0 && position < mTitles.size())
        {
            mTitles.set(position, title);
            notifyDataSetChanged();
        }
    }
}
