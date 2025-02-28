package com.example.warehouse_accounting.ui.suppliers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Suppliers

class SuppliersViewModel : ViewModel() {
    private val _allSuppliers = mutableListOf<Suppliers>()
    private val _suppliers = MutableLiveData<MutableList<Suppliers>>(mutableListOf())
    val suppliers: LiveData<MutableList<Suppliers>> = _suppliers

    fun addSuppliers(supplier: Suppliers) {
        _allSuppliers.add(supplier)
        _suppliers.value = _allSuppliers.toMutableList()
    }

    fun updateSuppliers(updatedSupplier: Suppliers) {
        _allSuppliers.replaceAll { if (it.tin == updatedSupplier.tin) updatedSupplier else it }
        _suppliers.value = _allSuppliers.toMutableList()
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
