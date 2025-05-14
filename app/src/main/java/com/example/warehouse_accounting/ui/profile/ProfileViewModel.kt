package com.example.warehouse_accounting.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Profile

class ProfileViewModel : ViewModel() {

    private val _profile = MutableLiveData(
        Profile(
            firstName = "John",
            lastName = "Doe",
            login = "johndoe",
            photoUri = null
        )
    )
    val profile: LiveData<Profile> = _profile

    fun updateFirstName(newName: String) {
        _profile.value = _profile.value?.copy(firstName = newName)
    }

    fun updateLastName(newLastName: String) {
        _profile.value = _profile.value?.copy(lastName = newLastName)
    }

    fun updateLogin(newLogin: String) {
        _profile.value = _profile.value?.copy(login = newLogin)
    }

    fun updatePhoto(newPhotoUri: Uri?) {
        _profile.value = _profile.value?.copy(photoUri = newPhotoUri)
    }
}
