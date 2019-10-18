package com.jackson_siro.visongbook.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arlib.floatingsearchview.util.Util;
import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.data.SearchWrapper;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private List<SearchWrapper> mDataSet = new ArrayList<SearchWrapper>();

    private int mLastAnimatedItemPosition = -1;

    public interface OnItemClickListener{
        void onClick(SearchWrapper colorWrapper);
    }

    private OnItemClickListener mItemsOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mColorName;
        public final TextView mColorValue;
        public final View mTextContainer;

        public ViewHolder(View view) {
            super(view);
            mColorName = view.findViewById(R.id.color_name);
            mColorValue = view.findViewById(R.id.color_value);
            mTextContainer = view.findViewById(R.id.text_container);
        }
    }

    public void swapData(List<SearchWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener){
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        SearchWrapper SearchSuggestions = mDataSet.get(position);
        holder.mColorName.setText(SearchSuggestions.getName());
        holder.mColorValue.setText(SearchSuggestions.getHex());

        int color = Color.parseColor(SearchSuggestions.getHex());
        holder.mColorName.setTextColor(color);
        holder.mColorValue.setTextColor(color);

        if(mLastAnimatedItemPosition < position){
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = position;
        }

        if(mItemsOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemsOnClickListener.onClick(mDataSet.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void animateItem(View view) {
        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }
}
