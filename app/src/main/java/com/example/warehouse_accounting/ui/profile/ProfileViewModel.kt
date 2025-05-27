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

    private val _validationErrors = MutableLiveData<Map<String, String>>()
    val validationErrors: LiveData<Map<String, String>> = _validationErrors

    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = _messages

    init {
        serverProfile.observeForever { serverProfile ->
            serverProfile?.let { profile ->
                _profile.value = profile
                saveProfileToPrefs(profile)
            }
        }
    }

    fun isPhoneValid(phone: String?): Boolean {
        if (phone.isNullOrEmpty()) return true
        val regex = Regex("^\\+7\\d{10}$")
        return regex.matches(phone)
    }

    fun isEmailValid(email: String?): Boolean {
        if (email.isNullOrEmpty()) return true // пустой email считаем валидным
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        return regex.matches(email)
    }

    fun isLoginValid(login: String?): Boolean {
        if (login.isNullOrEmpty()) return false
        return login.length >= 3 && login.matches(Regex("^[a-zA-Z0-9_-]+$"))
    }

    fun isNameValid(name: String?): Boolean {
        if (name.isNullOrEmpty()) return false
        return name.length >= 2 && name.matches(Regex("^[a-zA-Zа-яА-Я\\s-]+$"))
    }

    private fun formatPhone(input: String): String {
        val digits = input.filter { it.isDigit() }

        return when {
            digits.isEmpty() -> ""
            digits.startsWith("7") && digits.length <= 11 -> "+$digits"
            digits.startsWith("8") && digits.length <= 11 -> "+7${digits.substring(1)}"
            digits.length <= 10 -> "+7$digits"
            else -> "+7${digits.takeLast(10)}"
        }
    }

    fun updateFirstName(newName: String) {
        val errors = _validationErrors.value?.toMutableMap() ?: mutableMapOf()

        if (newName.isNotEmpty() && !isNameValid(newName)) {
            errors["firstName"] = "Имя должно содержать только буквы и быть не менее 2 символов"
        } else {
            errors.remove("firstName")
        }

        _validationErrors.value = errors
        _profile.value = _profile.value?.copy(firstName = newName)
    }

    fun updateLastName(newLastName: String) {
        val errors = _validationErrors.value?.toMutableMap() ?: mutableMapOf()

        if (newLastName.isNotEmpty() && !isNameValid(newLastName)) {
            errors["lastName"] = "Фамилия должна содержать только буквы и быть не менее 2 символов"
        } else {
            errors.remove("lastName")
        }

        _validationErrors.value = errors
        _profile.value = _profile.value?.copy(lastName = newLastName)
    }

    fun updateLogin(newLogin: String) {
        val errors = _validationErrors.value?.toMutableMap() ?: mutableMapOf()

        if (!isLoginValid(newLogin)) {
            errors["login"] = "Логин должен содержать минимум 3 символа (буквы, цифры, _ или -)"
        } else {
            errors.remove("login")
        }

        _validationErrors.value = errors
        _profile.value = _profile.value?.copy(login = newLogin)
    }

    fun updatePhone(newPhone: String) {
        val errors = _validationErrors.value?.toMutableMap() ?: mutableMapOf()

        // Форматируем номер телефона
        val formattedPhone = if (newPhone.isEmpty()) "" else formatPhone(newPhone)

        if (formattedPhone.isNotEmpty() && !isPhoneValid(formattedPhone)) {
            errors["phone"] = "Номер телефона должен быть в формате +7XXXXXXXXXX"
        } else {
            errors.remove("phone")
        }

        _validationErrors.value = errors
        _profile.value = _profile.value?.copy(phone = formattedPhone)
    }

    fun updateEmail(newEmail: String) {
        val errors = _validationErrors.value?.toMutableMap() ?: mutableMapOf()

        if (newEmail.isNotEmpty() && !isEmailValid(newEmail)) {
            errors["email"] = "Введите корректный email адрес"
        } else {
            errors.remove("email")
        }

        _validationErrors.value = errors
        _profile.value = _profile.value?.copy(email = newEmail)
    }

    fun updatePhoto(newPhotoUri: String?) {
        _profile.value = _profile.value?.copy(photoUri = newPhotoUri)
    }

    fun isProfileValid(): Boolean {
        val profile = _profile.value ?: return false
        val errors = mutableMapOf<String, String>()

        if (!isLoginValid(profile.login)) {
            errors["login"] = "Логин обязателен и должен содержать минимум 3 символа"
        }

        if (!isNameValid(profile.firstName)) {
            errors["firstName"] = "Имя обязательно и должно содержать только буквы"
        }

        if (!isNameValid(profile.lastName)) {
            errors["lastName"] = "Фамилия обязательна и должна содержать только буквы"
        }

        if (!profile.phone.isNullOrEmpty() && !isPhoneValid(profile.phone)) {
            errors["phone"] = "Номер телефона должен быть в формате +7XXXXXXXXXX"
        }

        if (!profile.email.isNullOrEmpty() && !isEmailValid(profile.email)) {
            errors["email"] = "Введите корректный email адрес"
        }

        _validationErrors.value = errors
        return errors.isEmpty()
    }

    fun saveProfile() {
        if (!isProfileValid()) {
            _messages.value = "Исправьте ошибки в полях перед сохранением"
            return
        }

        _profile.value?.let { profile ->
            saveProfileToPrefs(profile)
            nyaService.updateProfile(profile)
            _messages.value = "Профиль успешно сохранен"
        }
    }

    fun clearFieldError(fieldName: String) {
        val errors = _validationErrors.value?.toMutableMap() ?: return
        errors.remove(fieldName)
        _validationErrors.value = errors
    }

    fun clearAllErrors() {
        _validationErrors.value = emptyMap()
    }

    fun clearMessages() {
        _messages.value = ""
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
