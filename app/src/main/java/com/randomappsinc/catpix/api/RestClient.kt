package com.randomappsinc.catpix.api

import android.os.Handler
import android.os.HandlerThread
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient(private var listener: Listener) {

    private var catService: CatService
    private val handler: Handler

    interface Listener {
        fun onPicturesFetched(pictures: List<CatPicture>)

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

        val backgroundThread = HandlerThread("")
        backgroundThread.start()
        handler = Handler(backgroundThread.looper)
    }

    fun fetchPictures(page: Int) {
        catService.fetchCatPictures(page, ApiConstants.SMALL).enqueue(object : Callback<List<CatPicture>> {
            override fun onResponse(call: Call<List<CatPicture>>, response: Response<List<CatPicture>>) {
                if (response.isSuccessful) {
                    val catPictures = response.body()
                    if (catPictures != null) {
                        listener.onPicturesFetched(catPictures)
                    } else {
                        listener.onPictureFetchFail()
                    }
                } else {
                    listener.onPictureFetchFail()
                }
            }

            override fun onFailure(call: Call<List<CatPicture>>, t: Throwable) {
                listener.onPictureFetchFail()
            }
        })
    }
}
