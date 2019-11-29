package com.jackson_siro.visongbook.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.SelectableStanza;
import com.jackson_siro.visongbook.models.StanzaModel;
import com.jackson_siro.visongbook.ui.EePostView;

import java.util.ArrayList;
import java.util.List;

public class StanzaListAdapter extends RecyclerView.Adapter implements SelectableVerseHolder.OnItemSelectedListener {

    private final List<SelectableStanza> mValues;
    private boolean isMultiSelectionEnabled = false;
    private final int myfont;
    EePostView listener;


    public StanzaListAdapter(EePostView listener,
                             List<StanzaModel> stanzaModels, boolean isMultiSelectionEnabled, int myfont) {
        this.listener = listener;
        this.myfont = myfont;
        this.isMultiSelectionEnabled = isMultiSelectionEnabled;

        mValues = new ArrayList<SelectableStanza>();
        for (StanzaModel stanzaModel : stanzaModels) {
            mValues.add(new SelectableStanza(stanzaModel, false));
        }
    }

    @Override
    public SelectableVerseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listing_stanza, parent, false);

        return new SelectableVerseHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        SelectableVerseHolder holder = (SelectableVerseHolder) viewHolder;
        SelectableStanza selectableItem = mValues.get(position);
        holder.stanza.setText(selectableItem.getStanza());
        holder.lyrics.setText(selectableItem.getLyrics());
        holder.lyrics.setTextSize(myfont);

        holder.mItem = selectableItem;
        holder.setChecked(holder.mItem.isSelected());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public List<StanzaModel> getSelectedItems() {

        List<StanzaModel> selectedSliderModels = new ArrayList<StanzaModel>();
        for (SelectableStanza item : mValues) {
            if (item.isSelected()) {
                selectedSliderModels.add(item);
            }
        }
        return selectedSliderModels;
    }

    @Override
    public int getItemViewType(int position) {
        if(isMultiSelectionEnabled){
            return SelectableVerseHolder.MULTI_SELECTION;
        }
        else{
            return SelectableVerseHolder.SINGLE_SELECTION;
        }
    }

    @Override
    public void onItemSelected(SelectableStanza verse) {
        if (!isMultiSelectionEnabled) {
            for (SelectableStanza selectableItem : mValues) {
                if (!selectableItem.equals(verse) && selectableItem.isSelected()) {
                    selectableItem.setSelected(false);
                } else if (selectableItem.equals(verse) && verse.isSelected()) {
                    selectableItem.setSelected(true);
                }
            }
            notifyDataSetChanged();
        }
        //listener.onItemSelected(verse);
    }
}