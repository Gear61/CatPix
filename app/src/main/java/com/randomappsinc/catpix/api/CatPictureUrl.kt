package com.randomappsinc.catpix.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CatPictureUrl {

    @SerializedName("id")
    @Expose
    val id: String? = null

    @SerializedName("url")
    @Expose
    val url: String? = null
}
