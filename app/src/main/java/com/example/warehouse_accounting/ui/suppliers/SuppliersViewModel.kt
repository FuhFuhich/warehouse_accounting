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
        _allSuppliers.clear()
        _allSuppliers.addAll(list)
        filterSuppliers(searchQuery.value ?: "")
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
        _allSuppliers.add(suppliers)
        nyaService.addNewSupplier(suppliers)
        filterSuppliers(searchQuery.value ?: "")
        _notificationEvent.value = "Добавлен поставщик" to "Поставщик \"${suppliers.name}\" успешно добавлен."
    }

    fun updateSuppliers(updatedSuppliers: Suppliers) {
        _allSuppliers.replaceAll { if (it.tin == updatedSuppliers.tin) updatedSuppliers else it }
        filterSuppliers(searchQuery.value ?: "")
        _notificationEvent.value = "Поставщик обновлён" to "Поставщик \"${updatedSuppliers.name}\" успешно обновлён."
    }

    fun loadUpdatedSuppliers() {
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
    }
}
