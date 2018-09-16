package com.randomappsinc.catpix.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor : Interceptor {

    object Constants {
        const val X_API_KEY = "x-api-key"
        const val API_KEY = "e1eb78d0-f579-4d2b-8016-e4afa48de4d4"
        const val FORMAT_KEY = "format"
        const val JSON = "json"
        const val ORDER_KEY = "order"
        const val DESC = "DESC"
        const val LIMIT_KEY = "limit"
        const val NUM_RESULTS = "24"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request
                .url()
                .newBuilder()
                .addQueryParameter(Constants.FORMAT_KEY, Constants.JSON)
                .addQueryParameter(Constants.ORDER_KEY, Constants.DESC)
                .addQueryParameter(Constants.LIMIT_KEY, Constants.NUM_RESULTS)
                .build()
        request = request
                .newBuilder()
                .header(Constants.X_API_KEY, Constants.API_KEY)
                .url(url)
                .build()
        return chain.proceed(request)
    }
}
