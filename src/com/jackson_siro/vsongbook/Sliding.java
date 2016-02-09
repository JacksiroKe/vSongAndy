package com.jackson_siro.vsongbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackson_siro.vsongbook.adaptor.TabsPager;
import com.jackson_siro.vsongbook.common.view.TabLayout;


public class Sliding extends Fragment {   

    private TabLayout mTabLayout;   
    private ViewPager mViewPager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_sample, container, false);
    }

  
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	 setUpPager(view);
         setUpTabColor();
    }
    void setUpPager(View view){
    	 mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
         mViewPager.setAdapter(new TabsPager(getActivity()));
         mTabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
         mTabLayout.setViewPager(mViewPager); 
    }
    void setUpTabColor(){
    	 mTabLayout.setCustomTabColorizer(new TabLayout.TabColorizer() {
 			@Override
 			public int getIndicatorColor(int position) {
 				// TODO Auto-generated method stub
 				return Sliding.this.getResources().getColor(R.color.red);
 			}
 			@Override
 			public int getDividerColor(int position) {
 				// TODO Auto-generated method stub
 				return Sliding.this.getResources().getColor(R.color.red);
 			}
         });
    }
    
   
}
