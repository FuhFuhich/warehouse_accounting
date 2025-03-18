package com.example.warehouse_accounting.models

import java.util.Date

data class Documents(
    val documents: String,
    val documentsNumber: String,
    val creationDate: String,
    val nameOfWarehouse: String,
    val quantityOfGoods: Int
)
