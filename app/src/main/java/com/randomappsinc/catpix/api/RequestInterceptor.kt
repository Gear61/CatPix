package com.randomappsinc.catpix.api

import com.randomappsinc.catpix.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RequestInterceptor : Interceptor {

    object SearchCallConstants {
        const val X_API_KEY = "x-api-key"
        const val API_KEY = "e1eb78d0-f579-4d2b-8016-e4afa48de4d4"
        const val FORMAT_KEY = "format"
        const val JSON = "json"
        const val ORDER_KEY = "order"
        const val DESC = "DESC"
        const val LIMIT_KEY = "limit"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request
                .url()
                .newBuilder()
                .addQueryParameter(SearchCallConstants.FORMAT_KEY, SearchCallConstants.JSON)
                .addQueryParameter(SearchCallConstants.ORDER_KEY, SearchCallConstants.DESC)
                .addQueryParameter(
                        SearchCallConstants.LIMIT_KEY,
                        Constants.EXPECTED_PAGE_SIZE.toString())
                .build()
        request = request
                .newBuilder()
                .header(SearchCallConstants.X_API_KEY, SearchCallConstants.API_KEY)
                .url(url)
                .build()
        return chain.proceed(request)
    }
}
