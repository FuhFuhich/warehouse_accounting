package com.example.warehouse_accounting.ui.profile

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.warehouse_accounting.models.Profile

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    private val _profile = MutableLiveData(loadProfile())
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

    fun updatePhone(newPhone: String) {
        _profile.value = _profile.value?.copy(phone = newPhone)
    }

    fun updateEmail(newEmail: String) {
        _profile.value = _profile.value?.copy(email = newEmail)
    }

    fun updatePhoto(newPhotoUri: String?) {
        _profile.value = _profile.value?.copy(photoUri = newPhotoUri)
    }

    fun saveProfile() {
        _profile.value?.let { profile ->
            prefs.edit()
                .putString("firstName", profile.firstName)
                .putString("lastName", profile.lastName)
                .putString("login", profile.login)
                .putString("phone", profile.phone)
                .putString("email", profile.email)
                .putString("photoUri", profile.photoUri?.toString())
                .apply()
        }
    }

    private fun loadProfile(): Profile {
        return Profile(
            firstName = prefs.getString("firstName", "John") ?: "John",
            lastName = prefs.getString("lastName", "Doe") ?: "Doe",
            login = prefs.getString("login", "johndoe") ?: "johndoe",
            phone = prefs.getString("phone", "+7 999 123-45-67") ?: "+7 999 123-45-67",
            email = prefs.getString("email", "john.doe@example.com") ?: "john.doe@example.com",
            photoUri = prefs.getString("photoUri", null)
        )
    }
}
