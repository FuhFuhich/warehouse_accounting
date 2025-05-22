package com.example.warehouse_accounting.ui.profile

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.warehouse_accounting.ServerController.Service.nya

class ProfileViewModelFactory(
    private val application: Application,
    private val nyaService: nya
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(application, nyaService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
