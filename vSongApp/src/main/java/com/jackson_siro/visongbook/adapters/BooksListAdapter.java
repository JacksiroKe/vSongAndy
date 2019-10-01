package com.jackson_siro.visongbook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.SelectableBook;
import com.jackson_siro.visongbook.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class BooksListAdapter extends RecyclerView.Adapter implements SelectableViewHolder.OnItemSelectedListener {

    private final List<SelectableBook> mValues;
    private boolean isMultiSelectionEnabled = false;
    SelectableViewHolder.OnItemSelectedListener listener;


    public BooksListAdapter(SelectableViewHolder.OnItemSelectedListener listener,
                            List<CategoryModel> sliderModels, boolean isMultiSelectionEnabled) {
        this.listener = listener;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<>();
        for (CategoryModel sliderModel : sliderModels) {
            mValues.add(new SelectableBook(sliderModel, false));
        }
    }

    @Override
    public SelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);

        return new SelectableViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SelectableViewHolder holder = (SelectableViewHolder) viewHolder;
        SelectableBook selectableItem = mValues.get(position);
        holder.title.setText(selectableItem.getTitle());
        holder.details.setText(selectableItem.getQcount() + " " + selectableItem.getBackpath() + " songs");

        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<CategoryModel> getSelectedItems() {

        List<CategoryModel> selectedSliderModels = new ArrayList<>();
        for (SelectableBook item : mValues) {
            if (item.isSelected()) {
                selectedSliderModels.add(item);
            }
        }
        return selectedSliderModels;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableViewHolder.MULTI_SELECTION;
        }
        else{
            return SelectableViewHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(SelectableBook item) {
        if (!isMultiSelectionEnabled) {
            for (SelectableBook selectableItem : mValues) {
                if (!selectableItem.equals(item) && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(item) && item.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        listener.onItemSelected(item);
    }
}