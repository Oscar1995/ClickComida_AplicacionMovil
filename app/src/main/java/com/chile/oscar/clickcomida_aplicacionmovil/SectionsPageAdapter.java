package com.chile.oscar.clickcomida_aplicacionmovil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oscar on 23-06-2017.
 */

class SectionsPageAdapter extends FragmentPagerAdapter
{
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> stringList = new ArrayList<>();

    public void addFragment (Fragment fragment, String title)
    {
        fragmentList.add(fragment);
        stringList.add(title);
    }

    public SectionsPageAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return stringList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return stringList.size();
    }
}
