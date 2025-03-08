package com.example.warehouse_accounting.ui.buyers

import androidx.lifecycle.*
import com.example.warehouse_accounting.models.Buyers

class BuyersViewModel(state: SavedStateHandle) : ViewModel() {
    private val _allBuyers = mutableListOf<Buyers>()
    private val _buyers = MutableLiveData<MutableList<Buyers>>(mutableListOf())
    val buyers: LiveData<MutableList<Buyers>> = _buyers

    private val savedStateHandle = state
    val searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData("searchQuery", "")

    fun addBuyers(buyers: Buyers) {
        _allBuyers.add(buyers)
        filterBuyers(searchQuery.value ?: "")
    }

    fun updateBuyers(updatedBuyers: Buyers) {
        _allBuyers.replaceAll { if (it.tin == updatedBuyers.tin) updatedBuyers else it }
        filterBuyers(searchQuery.value ?: "")
    }

    fun loadUpdatedBuyers() {
        //_buyers.value = fetchSuppliersFromDatabase()
    }

    fun filterBuyers(query: String) {
        searchQuery.value = query
        _buyers.value = if (query.isEmpty()) {
            _allBuyers.toMutableList()
        } else {
            _allBuyers.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }
    }
}
