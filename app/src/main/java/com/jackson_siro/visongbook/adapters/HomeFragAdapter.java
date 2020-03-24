package com.jackson_siro.visongbook.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.jackson_siro.visongbook.fragments.*;

public class HomeFragAdapter extends FragmentPagerAdapter {

    private static final String LOG_TAG = HomeFragAdapter.class.getSimpleName();

    private Fragment mSearchFragment, mOnlineFragment, mCollectionFragment, mFavouritesFragment, mNotesFragment;

    public HomeFragAdapter(FragmentManager fm) {
        super(fm);
        mSearchFragment = new DdHomeFrag1();
        mOnlineFragment = new DdHomeFrag2();
        mCollectionFragment = new DdHomeFrag3();
        mFavouritesFragment = new DdHomeFrag4();
        mNotesFragment = new DdHomeFrag5();
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
                return mFavouritesFragment;
            case 4:
                return mNotesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
