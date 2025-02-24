package com.example.warehouse_accounting.ui.suppliers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Suppliers

class SuppliersViewModel : ViewModel() {
    private val _suppliers = MutableLiveData<MutableList<Suppliers>>(mutableListOf())
    val suppliers: LiveData<MutableList<Suppliers>> = _suppliers

    fun addSuppliers(suppliers: Suppliers) {
        _suppliers.value?.add(suppliers)
        _suppliers.value = _suppliers.value
    }

    fun updateSuppliers(updatedSuppliers: Suppliers) {
        _suppliers.value = _suppliers.value?.map {
            if (it.tin == updatedSuppliers.tin) updatedSuppliers else it
        }?.toMutableList()
    }
}
