package com.example.warehouse_accounting.models

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id_user: String?,
    val firstName: String,
    val lastName: String,
    val login: String,
    val phone: String,
    val email: String,
    val photoUri: String?,
    val password: String?
)
