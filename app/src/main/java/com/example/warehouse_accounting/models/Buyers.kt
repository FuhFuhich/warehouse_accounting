package com.example.warehouse_accounting.models

import kotlinx.serialization.Serializable

@Serializable
data class Buyers(
    val id: Int = 0,
    val name: String,
    val address: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val tin: String? = null,
    val bankDetails: String? = null,
    val note: String? = null
)