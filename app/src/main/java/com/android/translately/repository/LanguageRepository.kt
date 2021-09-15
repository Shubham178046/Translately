package com.android.translately.repository

import com.android.translately.model.requestModel.LanguageReq
import com.android.translately.model.responseModel.LanguageRes
import com.android.translately.network.LanguageApiImp
import com.android.translately.util.Resource
import javax.inject.Inject

open class LanguageRepository @Inject constructor(private val apiImp: LanguageApiImp) {

    suspend fun getLanguageData(
        query: String,
        source: String,
        target: String
    ): Resource<LanguageRes> = try {
        val response = apiImp.getLanguageData(query, source, target)
        val result = response.body()
        if (response.isSuccessful && result != null) {
            Resource.Success(result)
        } else {
            Resource.Error(response.message())
        }
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An error occured")
    }
}