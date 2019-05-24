package com.jackson_siro.visongbook.models;

import com.jackson_siro.visongbook.core.Searchable;

public class CountryModel implements Searchable {
    private String Country;
    private String CountryCode;
    private String IsoCode;

    public CountryModel(String country, String countryCode, String isoCode) {
        Country = country;
        CountryCode = countryCode;
        IsoCode = isoCode;
    }

    @Override
    public String getTitle() {
        return getCcode() + " | " + getCountry() + " - " + getIcode();
    }

    public String getCountry() {
        return Country;
    }

    public CountryModel setName(String country) {
        Country = country;
        return this;
    }

    public String getCcode() {
        return CountryCode;
    }

    public CountryModel setShort(String countryCode) {
        CountryCode = countryCode;
        return this;
    }

    public String getIcode() {
        return IsoCode;
    }

    public CountryModel setCode(String isoCode) {
        IsoCode = isoCode;
        return this;
    }
}
