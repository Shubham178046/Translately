package com.android.translately.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.translately.model.requestModel.LanguageReq
import com.android.translately.repository.LanguageRepository
import com.android.translately.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(private val languageRepository: LanguageRepository) :
    ViewModel() {
    private val _conversion = MutableStateFlow<TranslateEvent>(TranslateEvent.Empty)
    val conversion: StateFlow<TranslateEvent> = _conversion

    sealed class TranslateEvent {
        class Success(val convertText: String?) : TranslateEvent()
        class Failure(val errorText: String) : TranslateEvent()
        object Loading : TranslateEvent()
        object Empty : TranslateEvent()
    }

    fun convert(
        query: String,
        source: String,
        target: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = TranslateEvent.Loading
            when (val response = languageRepository.getLanguageData(query, target, source)) {
                is Resource.Error -> {
                    _conversion.value = TranslateEvent.Failure(response.message!!)
                }
                is Resource.Success -> {
                    _conversion.value =
                        TranslateEvent.Success(response.data!!.data!!.translations!!.get(0).translatedText.takeIf { response.data.data!!.translations != null || response.data.data.translations!!.size > 0 })
                }
            }
        }
    }
}