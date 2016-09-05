package com.ligen.wellwatcher.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * 管理员界面中显示各工种device
 * Created by ligen on 2016/5/18.
 */
public class AdminDeviceFragmentAdapter extends FragmentStatePagerAdapter {

    List<String> mTitles; 
    List<Fragment> mFragments;

    public AdminDeviceFragmentAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
        super(fm);
        mTitles = titles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public Fragment getFragment(int position) {
        return mFragments.get(position);
    }
}
