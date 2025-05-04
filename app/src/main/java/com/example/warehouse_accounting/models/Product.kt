package com.example.warehouse_accounting.models

import android.net.Uri

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val barcode: String,
    val imageUri: Uri?,
    val quantity: Int
)
