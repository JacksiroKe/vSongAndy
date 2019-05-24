package com.jackson_siro.visongbook.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.adapters.ViewPagerAdapter;
import com.jackson_siro.visongbook.components.CustomViewPager;

import java.util.Objects;

public class OnlineFragment extends Fragment {

    private View view;
    private TabLayout subTabLayout;

    public OnlineFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.posts_fragment, container, false);

        CustomViewPager viewPager = view.findViewById(R.id.homeViewPager);
        setupViewPager(viewPager);

        subTabLayout = view.findViewById(R.id.sub_tabs);
        subTabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        return view;
    }

    @SuppressLint("NewApi")
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager());

        Bundle bundle1 = new Bundle();
        bundle1.putString("api_request", "recent");
        OnlineFragmentTabs tab1 = new OnlineFragmentTabs();
        tab1.setArguments(bundle1);
        adapter.addFragment(tab1, "recent");

        viewPager.setAdapter(adapter);
    }

    @SuppressLint("NewApi")
    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_sub_tab, null);
        tabOne.setText("Coming Soon!");
        Objects.requireNonNull(subTabLayout.getTabAt(0)).setCustomView(tabOne);

    }
}
