package com.example.warehouse_accounting.utils

private var idCounter = 0

fun generateUniqueId(): Int {
    return idCounter++
}