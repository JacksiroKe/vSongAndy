package com.jackson_siro.visongbook.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchWrapper implements Parcelable {

    @SerializedName("hex")
    @Expose
    private String hex;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rgb")
    @Expose
    private String rgb;

    private SearchWrapper(Parcel in) {
        hex = in.readString();
        name = in.readString();
        rgb = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hex);
        dest.writeString(name);
        dest.writeString(rgb);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // @return The hex
    public String getHex() {
        return hex;
    }

    // @param hex The hex
    public void setHex(String hex) {
        this.hex = hex;
    }

    // @return The name
    public String getName() {
        return name;
    }

    // @param name The name
    public void setName(String name) {
        this.name = name;
    }

    // @return The rgb
    public String getRgb() {
        return rgb;
    }

    // @param rgb  The rgb
    public void setRgb(String rgb) {
        this.rgb = rgb;
    }

    public static final Creator<SearchWrapper> CREATOR = new Creator<SearchWrapper>() {
        @Override
        public SearchWrapper createFromParcel(Parcel in) {
            return new SearchWrapper(in);
        }

        @Override
        public SearchWrapper[] newArray(int size) {
            return new SearchWrapper[size];
        }
    };
}