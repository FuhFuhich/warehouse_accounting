package com.example.warehouse_accounting.ui.buyers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BuyersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is buyers Fragment"
    }
    val text: LiveData<String> = _text
}