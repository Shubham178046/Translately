package com.android.translately.util

import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.translately.model.language.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object Extentions {
    fun ViewModel.onMain(body: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Main)
        {
            body()
        }
    }

    fun ViewModel.onDefault(body: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.Default)
        {
            body()
        }
    }

    fun ViewModel.onIO(body: suspend () -> Unit): Job {
        return viewModelScope.launch(Dispatchers.IO)
        {
            body()
        }
    }


    fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
        val query = MutableStateFlow("")

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query.value = newText
                return true
            }
        })

        return query
    }

    fun AppCompatEditText.getTextChangedListenerStateFlow(): StateFlow<String> {
        val query = MutableStateFlow("")


        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && p0.isNotEmpty()) {
                    query.value = p0.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        return query
    }
}