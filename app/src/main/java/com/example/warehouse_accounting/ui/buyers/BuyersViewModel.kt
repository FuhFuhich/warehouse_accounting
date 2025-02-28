package com.example.warehouse_accounting.ui.buyers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Buyers

class BuyersViewModel : ViewModel() {
    private val _allBuyers = mutableListOf<Buyers>()
    private val _buyers = MutableLiveData<MutableList<Buyers>>(mutableListOf())
    val buyers: LiveData<MutableList<Buyers>> = _buyers

    fun addBuyers(supplier: Buyers) {
        _allBuyers.add(supplier)
        _buyers.value = _allBuyers.toMutableList()
    }

    fun updateBuyers(updatedSupplier: Buyers) {
        _allBuyers.replaceAll { if (it.tin == updatedSupplier.tin) updatedSupplier else it }
        _buyers.value = _allBuyers.toMutableList()
    }

    fun filterBuyers(query: String) {
        if (query.isEmpty()) {
            _buyers.value = _allBuyers.toMutableList()
        } else {
            _buyers.value = _allBuyers.filter {
                it.name.contains(query, ignoreCase = true)
            }.toMutableList()
        }
    }
}
