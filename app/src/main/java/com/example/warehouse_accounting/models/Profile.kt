package com.example.warehouse_accounting.models

import android.net.Uri

data class Profile(
    val firstName: String,
    val lastName: String,
    val login: String,
    val phone: String,
    val email: String,
    val photoUri: Uri?
)
