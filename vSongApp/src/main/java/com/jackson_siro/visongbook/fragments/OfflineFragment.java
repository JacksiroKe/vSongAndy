package com.jackson_siro.visongbook.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.adapters.ViewPagerAdapter;
import com.jackson_siro.visongbook.components.CustomViewPager;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.models.CategoryModel;
import com.jackson_siro.visongbook.ui.MyApplication;

import java.util.List;
import java.util.Objects;

public class OfflineFragment extends Fragment {

    private View view;
    private TabLayout subTabLayout;
    private List<CategoryModel> booksItems;

    private SQLiteHelper db = new SQLiteHelper(MyApplication.getAppContext());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        booksItems = db.getBookList();
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

        for (int i = 0; i < booksItems.size(); i ++) {
            Bundle bundle = new Bundle();
            bundle.putString("songbookid", booksItems.get(i).getCategoryid() + "");
            OfflineFragmentTabs tab = new OfflineFragmentTabs();
            tab.setArguments(bundle);
            adapter.addFragment(tab, booksItems.get(i).getCategoryid() + "");
        }

        viewPager.setAdapter(adapter);
    }

    @SuppressLint("NewApi")
    private void setupTabIcons() {
        for (int i = 0; i < booksItems.size(); i ++) {
            TextView tabtitle = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.fragment_subtab, null);
            tabtitle.setText(booksItems.get(i).getBackpath());
            Objects.requireNonNull(subTabLayout.getTabAt(i)).setCustomView(tabtitle);
        }

    }
}
