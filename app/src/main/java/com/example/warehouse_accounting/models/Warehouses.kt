package com.example.warehouse_accounting.models

import kotlinx.serialization.Serializable

@Serializable
data class Warehouses(
    val id: Int,
    val warehousesName: String
)