package com.jackson_siro.visongbook.models;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class SearchModel implements SearchSuggestion {

    private String mResultTitle;
    private int mResultID;
    private boolean mIsHistory = false;

    public SearchModel(String suggestion, int suggestionid) {
        this.mResultTitle = suggestion;
        this.mResultID = suggestionid;
    }

    public SearchModel(Parcel source) {
        this.mResultTitle = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    public int getID() {
        return mResultID;
    }

    @Override
    public String getBody() {
        return mResultTitle;
    }

    public static final Creator<SearchModel> CREATOR = new Creator<SearchModel>() {
        @Override
        public SearchModel createFromParcel(Parcel in) {
            return new SearchModel(in);
        }

        @Override
        public SearchModel[] newArray(int size) {
            return new SearchModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mResultTitle);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}