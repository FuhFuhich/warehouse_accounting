package com.example.warehouse_accounting.ui.suppliers

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.warehouse_accounting.ServerController.Service.nya

class SuppliersViewModelFactory(
    private val nyaService: nya,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(SuppliersViewModel::class.java)) {
            return SuppliersViewModel(handle, nyaService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
