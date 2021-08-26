package com.android.translately.network

import com.android.translately.model.requestModel.LanguageReq
import com.android.translately.model.responseModel.LanguageRes
import retrofit2.Response
import retrofit2.http.*

interface LanguageApi {

    @FormUrlEncoded
    @POST("translate/v2")
    suspend fun getData(
        @Header("Content-Type") ContentType: String,
        @Header("Accept-Encoding") AcceptEncoding: String,
        @Header("x-rapidapi-key") key: String,
        @Header("x-rapidapi-host") host: String,
        @Field("q") query: String,
        @Field("target") target: String,
        @Field("source") source: String,
    ): Response<LanguageRes>
}