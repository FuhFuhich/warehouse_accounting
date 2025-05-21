package com.example.warehouse_accounting.models

import kotlinx.serialization.Serializable

@Serializable
data class Buyers(
    val id: Int,
    val name: String,
    val address: String,
    val email: String,
    val phone: String,
    val tin: String,
    val bankDetails: String,
    val note: String
)