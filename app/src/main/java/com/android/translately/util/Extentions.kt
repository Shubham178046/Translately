package com.android.translately.util

import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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
                }else{
                    query.value = ""
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        return query
    }


    fun <T> Flow<T>.throttle(waitMillis: Int) = flow {
        coroutineScope {
            val context = coroutineContext
            var nextMillis = 0L
            var delayPost: Deferred<Unit>? = null
            collect {
                val current = SystemClock.uptimeMillis()
                if (nextMillis < current) {
                    nextMillis = current + waitMillis
                    emit(it)
                    delayPost?.cancel()
                } else {
                    val delayNext = nextMillis
                    delayPost?.cancel()
                    delayPost = async(Dispatchers.Default) {
                        delay(nextMillis - current)
                        if (delayNext == nextMillis) {
                            nextMillis = SystemClock.uptimeMillis() + waitMillis
                            withContext(context) {
                                emit(it)
                            }
                        }
                    }
                }
            }
        }
    }

/*    fun <T> Flow<T>.debounce(waitMillis: Long) = flow {
        coroutineScope {
            val context = coroutineContext
            var delayPost: Deferred<Unit>? = null
            collect {
                delayPost?.cancel()
                delayPost = async(Dispatchers.Default) {
                    delay(waitMillis)
                    withContext(context) {
                        emit(it)
                    }
                }
            }
        }
    }*/
}