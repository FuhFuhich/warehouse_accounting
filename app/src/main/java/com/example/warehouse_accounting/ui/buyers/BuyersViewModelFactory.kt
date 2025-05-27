package com.example.warehouse_accounting.ui.buyers

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.warehouse_accounting.ServerController.Service.Serv

class BuyersViewModelFactory(
    private val nyaService: Serv,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(BuyersViewModel::class.java)) {
            return BuyersViewModel(handle, nyaService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
