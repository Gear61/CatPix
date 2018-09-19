package com.randomappsinc.catpix.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CatService {

    @GET("images/search")
    fun fetchCatPictures(@Query("page") page: Int, @Query("size") size: String): Call<List<CatPicture>>
}
