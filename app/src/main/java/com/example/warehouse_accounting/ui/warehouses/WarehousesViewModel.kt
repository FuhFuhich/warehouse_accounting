package com.example.warehouse_accounting.ui.warehouses

import androidx.lifecycle.*
import com.example.warehouse_accounting.models.Warehouses
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WarehousesViewModel(state: SavedStateHandle) : ViewModel() {
    private val _allWarehouses = mutableListOf<Warehouses>()
    private val _warehouses = MutableLiveData<MutableList<Warehouses>>(mutableListOf())
    val warehouses: LiveData<MutableList<Warehouses>> = _warehouses

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
                loadUpdatedWarehouses()
                delay(5000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun addWarehouses(warehouses: Warehouses) {
        _allWarehouses.add(warehouses)
        filterWarehouses(searchQuery.value ?: "")
        _notificationEvent.value = "Добавлен склад" to "Склад \"${warehouses.warehousesName}\" успешно добавлен."
    }

    fun updateWarehouses(updatedWarehouses: Warehouses) {
        _allWarehouses.replaceAll { if (it.id == updatedWarehouses.id) updatedWarehouses else it }
        filterWarehouses(searchQuery.value ?: "")
        _notificationEvent.value = "Склад обновлён" to "Склад \"${updatedWarehouses.warehousesName}\" успешно обновлён."
    }

    fun loadUpdatedWarehouses() {
        //_warehouses.value = fetchWarehousesFromDatabase()
    }

    fun getId() : Int {
        //_warehouses.value = fetchWarehousesFromDatabase()
        return 0
    }

    fun filterWarehouses(query: String) {
        searchQuery.value = query
        _warehouses.value = if (query.isEmpty()) {
            _allWarehouses.toMutableList()
        } else {
            _allWarehouses.filter { it.warehousesName.contains(query, ignoreCase = true) }.toMutableList()
        }
    }

    fun generateUniqueId() : Int {
        return 0
    }
}
