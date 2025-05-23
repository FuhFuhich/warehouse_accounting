package com.example.warehouse_accounting.ui.warehouses

import androidx.lifecycle.*
import com.example.warehouse_accounting.ServerController.Service.nya
import com.example.warehouse_accounting.models.Warehouses
import kotlinx.coroutines.*

class WarehousesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val nyaService: nya
) : ViewModel() {
    private val _allWarehouses = mutableListOf<Warehouses>()
    private val _warehouses = MutableLiveData<MutableList<Warehouses>>(mutableListOf())
    val warehouses: LiveData<MutableList<Warehouses>> = _warehouses

    val searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData("searchQuery", "")

    private val _notificationEvent = MutableLiveData<Pair<String, String>>()
    val notificationEvent: LiveData<Pair<String, String>> = _notificationEvent

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    private val warehousesObserver = Observer<MutableList<Warehouses>> { list ->
        _allWarehouses.clear()
        _allWarehouses.addAll(list)
        filterWarehouses(searchQuery.value ?: "")
    }

    init {
        nyaService.getWarehousesLiveData().observeForever(warehousesObserver)
        startUpdatingWarehouses()
    }

    private fun startUpdatingWarehouses() {
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
        nyaService.getWarehousesLiveData().removeObserver(warehousesObserver)
    }

    fun addWarehouses(warehouses: Warehouses) {
        _allWarehouses.add(warehouses)
        nyaService.addNewWarehouse(warehouses)
        filterWarehouses(searchQuery.value ?: "")
        _notificationEvent.value = "Добавлен склад" to "Склад \"${warehouses.warehousesName}\" успешно добавлен."
    }

    fun updateWarehouses(updatedWarehouses: Warehouses) {
        _allWarehouses.replaceAll { if (it.id == updatedWarehouses.id) updatedWarehouses else it }
        filterWarehouses(searchQuery.value ?: "")
        _notificationEvent.value = "Склад обновлён" to "Склад \"${updatedWarehouses.warehousesName}\" успешно обновлён."
    }

    fun loadUpdatedWarehouses() {
        nyaService.requestAllWarehouses()
    }

    fun getId(): Int = 0

    fun filterWarehouses(query: String) {
        searchQuery.value = query
        _warehouses.value = if (query.isEmpty()) {
            _allWarehouses.toMutableList()
        } else {
            _allWarehouses.filter { it.warehousesName.contains(query, ignoreCase = true) }.toMutableList()
        }
    }
}
