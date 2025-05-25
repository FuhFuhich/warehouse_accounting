package com.example.warehouse_accounting.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.ServerController.Repositories.poka_tak
import com.example.warehouse_accounting.models.Profile

class AuthViewModel(
    private val repo: poka_tak
) : ViewModel() {
    val profileLiveData: LiveData<Profile?> = repo.profileLiveData

    fun register(login: String, password: String) {
        repo.register(login, password)
    }

    fun login(login: String, password: String) {
        repo.login(login, password)
    }
}
