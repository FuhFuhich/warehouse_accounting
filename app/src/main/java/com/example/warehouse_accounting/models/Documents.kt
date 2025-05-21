package com.example.warehouse_accounting.models

import kotlinx.serialization.Serializable

@Serializable
data class Documents(
    val documents: String,
    val documentsNumber: String,
    val creationDate: String,
    val nameOfWarehouse: String,
    val quantityOfGoods: Int
)
