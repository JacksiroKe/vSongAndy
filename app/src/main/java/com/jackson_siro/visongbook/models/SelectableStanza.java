package com.jackson_siro.visongbook.models;

public class SelectableStanza extends StanzaModel {
    private boolean isSelected = false;

    public SelectableStanza(StanzaModel stanza, boolean isSelected) {
        super(stanza.getStanza(), stanza.getLyrics());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}