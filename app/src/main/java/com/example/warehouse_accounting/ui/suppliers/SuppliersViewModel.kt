package com.example.warehouse_accounting.ui.suppliers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Suppliers

class SuppliersViewModel : ViewModel() {
    private val _allSuppliers = mutableListOf<Suppliers>()
    private val _suppliers = MutableLiveData<MutableList<Suppliers>>(mutableListOf())
    val suppliers: LiveData<MutableList<Suppliers>> = _suppliers

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    fun addSuppliers(suppliers: Suppliers) {
        _allSuppliers.add(suppliers)
        filterSuppliers(_searchQuery.value ?: "")
    }

    fun updateSuppliers(updatedSuppliers: Suppliers) {
        _allSuppliers.replaceAll { if (it.tin == updatedSuppliers.tin) updatedSuppliers else it }
        filterSuppliers(_searchQuery.value ?: "")
    }

    fun filterSuppliers(query: String) {
        if (query.isEmpty()) {
            _suppliers.value = _allSuppliers.toMutableList()
        } else {
            _suppliers.value = _allSuppliers.filter {
                it.name.contains(query, ignoreCase = true)
            }.toMutableList()
        }
    }
}
