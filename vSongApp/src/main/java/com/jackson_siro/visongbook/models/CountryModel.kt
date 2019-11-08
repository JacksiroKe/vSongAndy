package com.jackson_siro.visongbook.models

import com.jackson_siro.visongbook.core.Searchable

class CountryModel(country: String, countryCode: String, isoCode: String) : Searchable {
    var country: String? = null
        private set
    var ccode: String? = null
        private set
    var icode: String? = null
        private set

    init {
        this.country = country
        ccode = countryCode
        icode = isoCode
    }

    override fun getTitle(): String {
        return "$ccode | $country - $icode"
    }

    fun setName(country: String): CountryModel {
        this.country = country
        return this
    }

    fun setShort(countryCode: String): CountryModel {
        ccode = countryCode
        return this
    }

    fun setCode(isoCode: String): CountryModel {
        icode = isoCode
        return this
    }
}
