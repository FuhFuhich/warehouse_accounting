package com.example.warehouse_accounting.ui.mistral

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MistralViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is MistralViewModel Fragment"
    }
    val text: LiveData<String> = _text
}