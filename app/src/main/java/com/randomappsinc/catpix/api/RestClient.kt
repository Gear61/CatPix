package com.randomappsinc.catpix.api

import com.randomappsinc.catpix.models.CatPicture
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient(private var listener: Listener) {

    private var catService: CatService
    private var thumbnailUrls: List<CatPictureUrl>? = null
    private var fullResUrls: List<CatPictureUrl>? = null

    interface Listener {
        fun onPicturesFetched(pictures: ArrayList<CatPicture>)

        fun onPictureFetchFail()
    }

    init {
        val client = OkHttpClient.Builder()
                .addInterceptor(RequestInterceptor())
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        catService = retrofit.create(CatService::class.java)
    }

    fun fetchPictures(page: Int) {
        catService.fetchCatPictures(page, ApiConstants.SMALL).enqueue(object : Callback<List<CatPictureUrl>> {
            override fun onResponse(call: Call<List<CatPictureUrl>>, response: Response<List<CatPictureUrl>>) {
                if (response.isSuccessful) {
                    val urls = response.body()
                    if (urls != null) {
                        thumbnailUrls = response.body()
                        maybeReturnResponse()
                    } else {
                        onPictureFetchFail()
                    }
                } else {
                    onPictureFetchFail()
                }
            }

            override fun onFailure(call: Call<List<CatPictureUrl>>, t: Throwable) {
                onPictureFetchFail()
            }
        })

        catService.fetchCatPictures(page, ApiConstants.FULL).enqueue(object : Callback<List<CatPictureUrl>> {
            override fun onResponse(call: Call<List<CatPictureUrl>>, response: Response<List<CatPictureUrl>>) {
                if (response.isSuccessful) {
                    val urls = response.body()
                    if (urls != null) {
                        fullResUrls = response.body()
                        maybeReturnResponse()
                    } else {
                        onPictureFetchFail()
                    }
                } else {
                    onPictureFetchFail()
                }
            }

            override fun onFailure(call: Call<List<CatPictureUrl>>, t: Throwable) {
                onPictureFetchFail()
            }
        })
    }

    fun maybeReturnResponse() {
        if (thumbnailUrls != null && fullResUrls != null) {
            val catPictures = ArrayList<CatPicture>()
            for (i in 0 until thumbnailUrls!!.size) {
                var id : String? = null
                if (thumbnailUrls!![i].id != null) {
                    id = thumbnailUrls!![i].id
                } else if (fullResUrls!![i].id != null) {
                    id = fullResUrls!![i].id
                }
                val hasPicture = thumbnailUrls!![i].url != null || fullResUrls!![i].url != null
                if (id != null && hasPicture) {
                    catPictures.add(CatPicture(id, thumbnailUrls!![i].url, fullResUrls!![i].url))

                    // Pre-load the high-res image if possible
                    if (fullResUrls!![i].url != null) {
                        Picasso.get().load(fullResUrls!![i].url).fetch()
                    }
                }
            }
            thumbnailUrls = null
            fullResUrls = null
            listener.onPicturesFetched(catPictures)
        }
    }

    fun onPictureFetchFail() {
        thumbnailUrls = null
        fullResUrls = null
        listener.onPictureFetchFail()
    }
}
