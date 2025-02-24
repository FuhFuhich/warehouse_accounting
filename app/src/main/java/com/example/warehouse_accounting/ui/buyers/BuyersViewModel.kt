package com.example.warehouse_accounting.ui.buyers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Buyers

class BuyersViewModel : ViewModel() {
    private val _buyers = MutableLiveData<MutableList<Buyers>>(mutableListOf())
    val buyers: LiveData<MutableList<Buyers>> = _buyers

    fun addBuyers(buyers: Buyers) {
        _buyers.value = (_buyers.value ?: mutableListOf()).apply { add(buyers) }
    }

    fun updateBuyers(updatedBuyers: Buyers) {
        _buyers.value = _buyers.value?.map {
            if (it.tin == updatedBuyers.tin) updatedBuyers else it
        }?.toMutableList()
    }
}
