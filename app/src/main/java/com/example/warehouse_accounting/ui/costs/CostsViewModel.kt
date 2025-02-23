package com.example.warehouse_accounting.ui.costs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CostsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Costs Fragment"
    }
    val text: LiveData<String> = _text
}