package com.example.warehouse_accounting.ui.buyers

import androidx.lifecycle.*
import com.example.warehouse_accounting.models.Buyers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BuyersViewModel(state: SavedStateHandle) : ViewModel() {
    private val _allBuyers = mutableListOf<Buyers>()
    private val _buyers = MutableLiveData<MutableList<Buyers>>(mutableListOf())
    val buyers: LiveData<MutableList<Buyers>> = _buyers

    private val savedStateHandle = state
    val searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData("searchQuery", "")

    private val _notificationEvent = MutableLiveData<Pair<String, String>>()
    val notificationEvent: LiveData<Pair<String, String>> = _notificationEvent

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    init {
        startUpdatingProducts()
    }

    private fun startUpdatingProducts() {
        viewModelScope.launch {
            while (isActive) {
                loadUpdatedBuyers()
                delay(5000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun addBuyers(buyers: Buyers) {
        _allBuyers.add(buyers)
        filterBuyers(searchQuery.value ?: "")
        _notificationEvent.value = "Добавлен покупатель" to "Покупатель \"${buyers.name}\" успешно добавлен."
    }

    fun updateBuyers(updatedBuyers: Buyers) {
        _allBuyers.replaceAll { if (it.tin == updatedBuyers.tin) updatedBuyers else it }
        filterBuyers(searchQuery.value ?: "")
        _notificationEvent.value = "Покупатель обновлён" to "Покупатель \"${updatedBuyers.name}\" успешно обновлён."
    }

    fun loadUpdatedBuyers() {
        //_buyers.value = fetchSuppliersFromDatabase()
    }

    fun getId() : Int {
        //_warehouses.value = fetchWarehousesFromDatabase()
        return 0
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
