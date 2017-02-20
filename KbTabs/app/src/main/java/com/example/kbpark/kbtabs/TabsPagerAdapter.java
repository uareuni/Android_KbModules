package com.example.kbpark.kbtabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            // 초기 fragment는 이렇게 adapter를 이용해서 붙인다.
            // 추후에는 요 fragment의 container view를 대상으로 거기에다가 계속 add, replace등 하면 되는건가?
            return FirstTabOne.newInstance();
        }
        else
        {
            return SecondTabOne.newInstance();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "TAB " + (position + 1);
    }
}