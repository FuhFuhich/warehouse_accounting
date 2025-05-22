package com.example.warehouse_accounting.ServerController.Repositories

import com.example.warehouse_accounting.ServerController.WebSocketConnection
import kotlinx.serialization.json.Json

class poka_tak(private val webSocketConnection: WebSocketConnection) {

    private inline fun <reified T : Any> send_request(type: String, data: T? = null) {
        val message = if (data != null) {
            val json = Json.encodeToString(data)
            "$type $json"
        } else {
            type
        }
        webSocketConnection.sendMessage(message)
    }

    fun handle_request(message: String) {
        when {
            message.startsWith("buyers") -> handleBuyers(message.removePrefix("buyers ").trim())
            message.startsWith("suppliers") -> handleSuppliers(message.removePrefix("suppliers ").trim())
            message.startsWith("profile") -> handleProfile(message.removePrefix("profile ").trim())
            message.startsWith("warehouses") -> handleWarehouses(message.removePrefix("warehouses ").trim())
            message.startsWith("documents") -> handleDocuments(message.removePrefix("documents ").trim())
            message.startsWith("products") -> handleProducts(message.removePrefix("products ").trim())
            else -> handleUnknown(message)
        }
    }

    private fun handleBuyers(data: String) {
        println("Обработка buyers: $data")
        // здесь логика с покупателем
    }

    private fun handleSuppliers(data: String) {
        println("Обработка suppliers: $data")
        // и тд
    }

    private fun handleProfile(data: String) {
        println("Обработка profile: $data")

    }

    private fun handleWarehouses(data: String) {
        println("Обработка warehouses: $data")

    }

    private fun handleDocuments(data: String) {
        println("Обработка documents: $data")

    }

    private fun handleProducts(data: String) {
        println("Обработка products: $data")

    }

    private fun handleUnknown(data: String) {
        println("Неизвестная команда: $data")

    }
}
