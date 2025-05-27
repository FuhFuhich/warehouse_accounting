package com.example.warehouse_accounting.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int = 0,
    val name: String,
    val description: String,
    val barcode: String,
    val imageUri: String? = null,
    val quantity: Int,
    val warehouse: String? = null
)
