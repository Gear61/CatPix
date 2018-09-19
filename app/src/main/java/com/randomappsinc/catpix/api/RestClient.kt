package com.randomappsinc.catpix.api

import com.randomappsinc.catpix.models.CatPicture
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient(private var listener: Listener) {

    private var catService: CatService
    private var thumbnailUrls: List<String?>? = null
    private var fullResUrls: List<String?>? = null

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
                        val newThumbnails = ArrayList<String?>()
                        for (urlContainer in urls) {
                            newThumbnails.add(urlContainer.url)
                        }
                        thumbnailUrls = newThumbnails
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
                        val newFullResUrls = ArrayList<String?>()
                        for (urlContainer in urls) {
                            newFullResUrls.add(urlContainer.url)
                        }
                        fullResUrls = newFullResUrls
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
                if (thumbnailUrls!![i] != null || fullResUrls!![i] != null) {
                    catPictures.add(CatPicture(thumbnailUrls!![i], fullResUrls!![i]))
                }
            }
            listener.onPicturesFetched(catPictures)
        }
    }

    fun onPictureFetchFail() {
        thumbnailUrls = null
        fullResUrls = null
        listener.onPictureFetchFail()
    }
}
