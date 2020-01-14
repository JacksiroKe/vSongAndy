package com.jackson_siro.visongbook.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.tabs.TabLayout;

import com.jackson_siro.visongbook.MyApplication;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.data.SQLiteHelper;
import com.jackson_siro.visongbook.fragments.BooksViewFrag;
import com.jackson_siro.visongbook.models.*;

import java.util.List;

public class EeBooksView extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private List<CategoryModel> booksItems;
    private SQLiteHelper db = new SQLiteHelper(MyApplication.getAppContext());

    BooksPagerAdapter mPagerAdapter;
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ee_books_view);
        toolbarSet();

        booksItems = db.getBookList();

        mPagerAdapter = new BooksPagerAdapter(booksItems, getSupportFragmentManager());
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
    }

    private void toolbarSet() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.songs_per_books);
    }

    public class BooksPagerAdapter extends FragmentPagerAdapter {
        private List<CategoryModel> mTitles;

        public BooksPagerAdapter(List<CategoryModel> books, FragmentManager fm) {
            super(fm);
            mTitles = books;
        }

        public Fragment getItem(int position) {
            BooksViewFrag songbook = new BooksViewFrag();
            Bundle data = new Bundle();
            data.putInt("songbookid", mTitles.get(position).getCategoryid());
            data.putString("booktitle", mTitles.get(position).getTitle());
            songbook.setArguments(data);
            return songbook;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position).getBackpath();
        }

        @Override
        public int getCount() {
            return mTitles.size();
        }
    }
}