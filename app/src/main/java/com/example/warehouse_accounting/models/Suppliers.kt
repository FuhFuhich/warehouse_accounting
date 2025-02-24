package com.example.warehouse_accounting.models

data class Suppliers(
    val name: String,
    val address: String,
    val email: String,
    val phone: String,
    val tin: String,
    val bankDetails: String,
    val note: String
)