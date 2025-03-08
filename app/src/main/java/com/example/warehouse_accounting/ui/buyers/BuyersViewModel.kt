package com.example.warehouse_accounting.ui.buyers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Buyers

class BuyersViewModel : ViewModel() {
    private val _allBuyers = mutableListOf<Buyers>()
    private val _buyers = MutableLiveData<MutableList<Buyers>>(mutableListOf())
    val buyers: LiveData<MutableList<Buyers>> = _buyers

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    fun addBuyers(buyers: Buyers) {
        _allBuyers.add(buyers)
        filterBuyers(_searchQuery.value ?: "")
    }

    fun updateBuyers(updatedBuyers: Buyers) {
        _allBuyers.replaceAll { if (it.tin == updatedBuyers.tin) updatedBuyers else it }
        filterBuyers(_searchQuery.value ?: "")
    }

    fun loadUpdatedBuyers() {
        //_buyers.value = fetchBuyersFromDatabase()
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
