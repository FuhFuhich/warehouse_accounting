package com.example.warehouse_accounting.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val barcode: String,
    val imageUri: String?,
    val quantity: Int
)
