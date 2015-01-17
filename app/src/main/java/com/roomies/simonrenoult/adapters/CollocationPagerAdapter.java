package com.roomies.simonrenoult.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.roomies.simonrenoult.fragments.BoardFragment;
import com.roomies.simonrenoult.fragments.MapFragment;

public class CollocationPagerAdapter extends FragmentPagerAdapter {
    
    private Bundle _bundle;
    
    public CollocationPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        _bundle = bundle;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new BoardFragment();
            case 1:
                MapFragment m = new MapFragment();
                m.setArguments(_bundle);
                return m;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
