package com.jackson_siro.visongbook.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.jackson_siro.visongbook.fragments.*;

public class HomeFragmentAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = HomeFragmentAdapter.class.getSimpleName();

    private Fragment mSearchFragment;
    private Fragment mOnlineFragment;
    private Fragment mCollectionFragment;
    private Fragment mPersonalFragment;

    public HomeFragmentAdapter(FragmentManager fm) {
        super(fm);
        mSearchFragment = new SearchFragment();
        mOnlineFragment = new OnlineFragment();
        mCollectionFragment = new OfflineFragment();
        mPersonalFragment = new OnlineFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return mSearchFragment;
            case 1:
                return mOnlineFragment;
            case 2:
                return mCollectionFragment;
            case 3:
                return mPersonalFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
