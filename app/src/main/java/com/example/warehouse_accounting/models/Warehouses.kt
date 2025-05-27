package com.example.warehouse_accounting.models

import kotlinx.serialization.Serializable

@Serializable
data class Warehouses(
    val id: Int = 0,
    val warehousesName: String,
    val totalQuantity: Int = 0
)
