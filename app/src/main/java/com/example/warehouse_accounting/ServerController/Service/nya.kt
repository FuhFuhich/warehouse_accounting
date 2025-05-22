package com.example.warehouse_accounting.ServerController.Service

import com.example.warehouse_accounting.ServerController.Repositories.poka_tak
import com.example.warehouse_accounting.models.Buyers
import kotlinx.serialization.json.Json

class nya(private val repository: poka_tak) {
    // Здесь будет передача данных из репозитория во ViewModel
    // Т.е. к примеру у меня есть функция getNewProducts(команда)
    // Я вызываю метод из poka_tak для отправки сообщения в веб-сокет
    // Там происходит шифровка данных посредством security и
    // Потом вызов метода sendMessage из WebSocketConnection


    fun getBuyersLiveData() = repository.buyersLiveData

    fun addNewBuyer(buyer: Buyers) {
        repository.send_request("buyersAdd", buyer)
    }

    fun requestAllBuyers() {
        repository.send_request<Buyers>("buyersGet")
    }

    // Suppliers


    // Profile


    // Warehouses


    // Documents


    // Products

}