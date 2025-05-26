package com.example.warehouse_accounting.models

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id_user: Int,
    val login:   String?,
    val firstName: String? = null,
    val lastName:  String? = null,
    val phone:     String? = null,
    val email:     String? = null,
    val photoUri:  String? = null,
    val password:  String? = null
)
