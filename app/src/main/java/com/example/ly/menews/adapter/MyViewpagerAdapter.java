package com.example.ly.menews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by v_fanlulin on 2018/10/22.
 */

public class MyViewpagerAdapter extends FragmentPagerAdapter {
    //标题list
    private List<String> titleLists;
    //碎片list
    private List<Fragment> fragments;

    public MyViewpagerAdapter(FragmentManager fm, List<String> titleLists, List<Fragment> fragments) {
        super(fm);
        this.titleLists = titleLists;
        this.fragments = fragments;
    }

    public MyViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //得到当前fragment位置
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleLists.get(position);
    }
}
