package com.randomappsinc.catpix.models

import android.os.Parcel
import android.os.Parcelable

class CatPicture : Parcelable {

    var id: String? = null
    private var thumbnailUrl: String? = null
    private var fullResUrl: String? = null

    constructor(id: String?, thumbnailUrl: String?, fullResUrl: String?) {
        this.id = id
        this.thumbnailUrl = thumbnailUrl
        this.fullResUrl = fullResUrl
    }

    fun getThumbnailUrlWithFallback(): String? {
        return if (thumbnailUrl == null) fullResUrl else thumbnailUrl
    }

    fun getFullResUrlWithFallback(): String? {
        return if (fullResUrl == null) thumbnailUrl else fullResUrl
    }

    private constructor(`in`: Parcel) {
        id = `in`.readString()
        thumbnailUrl = `in`.readString()
        fullResUrl = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(thumbnailUrl)
        dest?.writeString(fullResUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CatPicture> {
        override fun createFromParcel(`in`: Parcel): CatPicture {
            return CatPicture(`in`)
        }

        override fun newArray(size: Int): Array<CatPicture?> {
            return arrayOfNulls(size)
        }
    }
}
