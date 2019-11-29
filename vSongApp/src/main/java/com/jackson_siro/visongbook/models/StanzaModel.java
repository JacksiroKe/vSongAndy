package com.jackson_siro.visongbook.models;

import java.io.Serializable;

public class StanzaModel implements Serializable{
    public String stanza;
    public String lyrics;

    public StanzaModel(String stanza, String lyrics) {
        this.stanza = stanza;
        this.lyrics = lyrics;
    }

    public void setStanza(String stanza) {
        this.stanza = stanza;
    }
    public String getStanza() {
        return stanza;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
    public String getLyrics() {
        return lyrics;
    }

}
