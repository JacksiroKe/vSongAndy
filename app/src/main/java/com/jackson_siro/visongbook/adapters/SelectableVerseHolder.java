package com.jackson_siro.visongbook.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jackson_siro.visongbook.R;
import com.jackson_siro.visongbook.models.SelectableStanza;

public class SelectableVerseHolder extends RecyclerView.ViewHolder {

    public static final int MULTI_SELECTION = 2;
    public static final int SINGLE_SELECTION = 1;
    LinearLayout cardView;
    SelectableStanza mItem;
    TextView stanza, lyrics;
    OnItemSelectedListener itemSelectedListener;

    public SelectableVerseHolder(View view, OnItemSelectedListener listener) {
        super(view);
        itemSelectedListener = listener;
        cardView = view.findViewById(R.id.cardViewLayout);
        stanza = view.findViewById(R.id.song_stanza);
        lyrics = view.findViewById(R.id.song_lyrics);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItem.isSelected() && getItemViewType() == MULTI_SELECTION) {
                    setChecked(false);
                } else {
                    setChecked(true);
                }
                itemSelectedListener.onItemSelected(mItem);
            }
        });
    }

    public void setChecked(boolean value) {
        if (value) {
            cardView.setBackgroundResource(R.drawable.custom_selected);
            //stanza.setTextColor(cardView.getContext().getResources().getColor(R.color.white_color));
            lyrics.setTextColor(cardView.getContext().getResources().getColor(R.color.white_color));
        } else {
            cardView.setBackgroundResource(R.drawable.custom_edit_text1);
            //stanza.setTextColor(cardView.getContext().getResources().getColor(R.color.black));
            lyrics.setTextColor(cardView.getContext().getResources().getColor(R.color.black));
        }
        mItem.setSelected(value);
    }

    public interface OnItemSelectedListener {
        void onItemSelected(SelectableStanza item);
    }

}