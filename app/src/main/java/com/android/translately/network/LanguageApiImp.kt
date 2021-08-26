package com.android.translately.network

import com.android.translately.model.requestModel.LanguageReq
import com.android.translately.model.responseModel.LanguageRes
import dagger.Provides
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class LanguageApiImp @Inject constructor(private val api: LanguageApi) {

    @Inject
    @Named("Key")
    lateinit var KEY: String

    @Inject
    @Named("Host")
    lateinit var HOST: String


    suspend fun getLanguageData(
        query: String,
        source: String,
        target: String
    ): Response<LanguageRes> =
        api.getData(
            "application/x-www-form-urlencoded",
            "application/gzip",
            KEY,
            HOST,
            query,
            target,
            source
        )
}