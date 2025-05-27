package com.example.warehouse_accounting.ui.suppliers

import androidx.lifecycle.*
import com.example.warehouse_accounting.ServerController.Service.nya
import com.example.warehouse_accounting.models.Suppliers
import kotlinx.coroutines.*

class SuppliersViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val nyaService: nya
) : ViewModel() {
    private val _allSuppliers = mutableListOf<Suppliers>()
    private val _suppliers = MutableLiveData<MutableList<Suppliers>>(mutableListOf())
    val suppliers: LiveData<MutableList<Suppliers>> = _suppliers

    val searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData("searchQuery", "")

    private val _notificationEvent = MutableLiveData<Pair<String, String>>()
    val notificationEvent: LiveData<Pair<String, String>> = _notificationEvent

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    private val suppliersObserver = Observer<MutableList<Suppliers>> { list ->
        println("=== SUPPLIERS OBSERVER ===")
        println("Получены поставщики с сервера: ${list.size}")
        list.forEach { supplier ->
            println("Supplier: id=${supplier.id}, name=${supplier.name}")
        }

        _allSuppliers.clear()
        _allSuppliers.addAll(list)
        filterSuppliers(searchQuery.value ?: "")

        println("_allSuppliers обновлен, размер: ${_allSuppliers.size}")
    }

    init {
        nyaService.getSuppliersLiveData().observeForever(suppliersObserver)
        startUpdatingSuppliers()
    }

    private fun startUpdatingSuppliers() {
        viewModelScope.launch {
            while (isActive) {
                loadUpdatedSuppliers()
                delay(5000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        nyaService.getSuppliersLiveData().removeObserver(suppliersObserver)
    }

    fun addSuppliers(suppliers: Suppliers) {
        println("=== ДОБАВЛЕНИЕ ПОСТАВЩИКА ===")
        println("Добавляем: $suppliers")

        _allSuppliers.add(suppliers)
        filterSuppliers(searchQuery.value ?: "")

        nyaService.addNewSupplier(suppliers)
        _notificationEvent.value = "Добавлен поставщик" to "Поставщик \"${suppliers.name}\" успешно добавлен."
    }

    fun updateSuppliers(updatedSuppliers: Suppliers) {
        println("=== ОБНОВЛЕНИЕ ПОСТАВЩИКА ===")
        println("Обновляем: $updatedSuppliers")

        if (updatedSuppliers.id != 0) {
            _allSuppliers.replaceAll { if (it.id == updatedSuppliers.id) updatedSuppliers else it }
        } else {
            _allSuppliers.replaceAll { if (it.name == updatedSuppliers.name) updatedSuppliers else it }
        }

        filterSuppliers(searchQuery.value ?: "")

        nyaService.updateSupplier(updatedSuppliers)
        _notificationEvent.value = "Поставщик обновлён" to "Поставщик \"${updatedSuppliers.name}\" успешно обновлён."
    }

    fun deleteSupplier(supplier: Suppliers) {
        println("=== УДАЛЕНИЕ ПОСТАВЩИКА ===")
        println("Удаляем: $supplier")

        _allSuppliers.removeAll { it.id == supplier.id }
        filterSuppliers(searchQuery.value ?: "")

        nyaService.deleteSupplier(supplier)
        _notificationEvent.value = "Поставщик удалён" to "Поставщик \"${supplier.name}\" успешно удалён."
    }

    fun loadUpdatedSuppliers() {
        println("=== ЗАПРОС ОБНОВЛЕНИЯ ПОСТАВЩИКОВ ===")
        nyaService.requestAllSuppliers()
    }

    fun getId(): Int = 0

    fun filterSuppliers(query: String) {
        searchQuery.value = query
        _suppliers.value = if (query.isEmpty()) {
            _allSuppliers.toMutableList()
        } else {
            _allSuppliers.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }

        println("Фильтрация: запрос='$query', результат=${_suppliers.value?.size} поставщиков")
    }
}
