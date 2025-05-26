package com.example.warehouse_accounting.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.warehouse_accounting.ServerController.Service.nya
import com.example.warehouse_accounting.models.Profile

class ProfileViewModel(
    application: Application,
    private val nyaService: nya
) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)

    private val _profile = MutableLiveData(loadProfile())
    val profile: LiveData<Profile> = _profile

    val serverProfile: LiveData<Profile?> = nyaService.getProfileLiveData()

    init {
        serverProfile.observeForever { serverProfile ->
            serverProfile?.let { profile ->
                _profile.value = profile
                saveProfileToPrefs(profile)
            }
        }
    }

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
            saveProfileToPrefs(profile)
            nyaService.updateProfile(profile)
        }
    }

    private fun saveProfileToPrefs(profile: Profile) {
        prefs.edit()
            .putInt("id_user", profile.id_user)
            .putString("firstName", profile.firstName)
            .putString("lastName", profile.lastName)
            .putString("login", profile.login)
            .putString("phone", profile.phone)
            .putString("email", profile.email)
            .putString("photoUri", profile.photoUri)
            .apply()
    }

    private fun loadProfile(): Profile {
        return Profile(
            id_user   = prefs.getInt("id_user", -1),
            firstName = prefs.getString("firstName", ""),
            lastName  = prefs.getString("lastName", ""),
            login     = prefs.getString("login", ""),
            phone     = prefs.getString("phone", ""),
            email     = prefs.getString("email", ""),
            photoUri  = prefs.getString("photoUri", null),
            password  = ""
        )
    }

    fun requestProfileFromServer() {
        nyaService.requestProfile()
    }

    override fun onCleared() {
        super.onCleared()
        serverProfile.removeObserver { }
    }
}
