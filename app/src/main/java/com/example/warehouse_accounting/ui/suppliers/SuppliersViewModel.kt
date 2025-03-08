package com.example.warehouse_accounting.ui.suppliers

import androidx.lifecycle.*
import com.example.warehouse_accounting.models.Suppliers

class SuppliersViewModel(state: SavedStateHandle) : ViewModel() {
    private val _allSuppliers = mutableListOf<Suppliers>()
    private val _suppliers = MutableLiveData<MutableList<Suppliers>>(mutableListOf())
    val suppliers: LiveData<MutableList<Suppliers>> = _suppliers

    private val savedStateHandle = state
    val searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData("searchQuery", "")

    fun addSuppliers(suppliers: Suppliers) {
        _allSuppliers.add(suppliers)
        filterSuppliers(searchQuery.value ?: "")
    }

    fun updateSuppliers(updatedSuppliers: Suppliers) {
        _allSuppliers.replaceAll { if (it.tin == updatedSuppliers.tin) updatedSuppliers else it }
        filterSuppliers(searchQuery.value ?: "")
    }

    fun loadUpdatedSuppliers() {
        //_suppliers.value = fetchSuppliersFromDatabase()
    }

    fun filterSuppliers(query: String) {
        searchQuery.value = query
        _suppliers.value = if (query.isEmpty()) {
            _allSuppliers.toMutableList()
        } else {
            _allSuppliers.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }
    }
}
