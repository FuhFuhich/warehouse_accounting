package com.example.warehouse_accounting.ui.buyers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Buyers

class BuyersViewModel : ViewModel() {
    private val _buyers = MutableLiveData<MutableList<Buyers>>(mutableListOf())
    val buyers: LiveData<MutableList<Buyers>> = _buyers

    fun addBuyers(buyers: Buyers) {
        _buyers.value?.add(buyers)
        _buyers.value = _buyers.value
    }

    fun updateBuyers(updatedBuyers: Buyers) {
        val currentList = _buyers.value ?: return
        val index = currentList.indexOfFirst { it.tin == updatedBuyers.tin }
        if (index != -1) {
            currentList[index] = updatedBuyers
            _buyers.value = currentList
        }
    }
}
