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
        println("=== WAREHOUSES OBSERVER ===")
        println("Получены склады с сервера: ${list.size}")
        list.forEach { warehouse ->
            println("Warehouse: id=${warehouse.id}, name=${warehouse.warehousesName}")
        }

        _allWarehouses.clear()
        _allWarehouses.addAll(list)
        filterWarehouses(searchQuery.value ?: "")

        println("_allWarehouses обновлен, размер: ${_allWarehouses.size}")
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
        println("=== ДОБАВЛЕНИЕ СКЛАДА ===")
        println("Добавляем: $warehouses")

        _allWarehouses.add(warehouses)
        filterWarehouses(searchQuery.value ?: "")

        nyaService.addNewWarehouse(warehouses)
        _notificationEvent.value = "Добавлен склад" to "Склад \"${warehouses.warehousesName}\" успешно добавлен."
    }

    fun updateWarehouses(updatedWarehouses: Warehouses) {
        println("=== ОБНОВЛЕНИЕ СКЛАДА ===")
        println("Обновляем: $updatedWarehouses")

        if (updatedWarehouses.id != 0) {
            _allWarehouses.replaceAll { if (it.id == updatedWarehouses.id) updatedWarehouses else it }
        } else {
            _allWarehouses.replaceAll { if (it.warehousesName == updatedWarehouses.warehousesName) updatedWarehouses else it }
        }

        filterWarehouses(searchQuery.value ?: "")

        // Отправляем обновление на сервер
        nyaService.updateWarehouse(updatedWarehouses)
        _notificationEvent.value = "Склад обновлён" to "Склад \"${updatedWarehouses.warehousesName}\" успешно обновлён."
    }

    fun deleteWarehouse(warehouse: Warehouses) {
        println("=== УДАЛЕНИЕ СКЛАДА ===")
        println("Удаляем: $warehouse")

        _allWarehouses.removeAll { it.id == warehouse.id }
        filterWarehouses(searchQuery.value ?: "")

        nyaService.deleteWarehouse(warehouse)
        _notificationEvent.value = "Склад удалён" to "Склад \"${warehouse.warehousesName}\" успешно удалён."
    }

    fun loadUpdatedWarehouses() {
        println("=== ЗАПРОС ОБНОВЛЕНИЯ СКЛАДОВ ===")
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

        println("Фильтрация: запрос='$query', результат=${_warehouses.value?.size} складов")
    }
}
