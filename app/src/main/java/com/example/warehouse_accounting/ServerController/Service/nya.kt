package com.example.warehouse_accounting.ServerController.Service

import com.example.warehouse_accounting.ServerController.Repositories.poka_tak
import com.example.warehouse_accounting.models.Buyers
import com.example.warehouse_accounting.models.Product
import com.example.warehouse_accounting.models.Profile
import com.example.warehouse_accounting.models.Suppliers
import com.example.warehouse_accounting.models.Warehouses
import kotlinx.serialization.json.Json

class nya(private val repository: poka_tak) {
    // Здесь будет передача данных из репозитория во ViewModel
    // Т.е. к примеру у меня есть функция getNewProducts(команда)
    // Я вызываю метод из poka_tak для отправки сообщения в веб-сокет
    // Там происходит шифровка данных посредством security и
    // Потом вызов метода sendMessage из WebSocketConnection


    // Buyers
    fun getBuyersLiveData() = repository.buyersLiveData
    fun addNewBuyer(buyer: Buyers) = repository.send_request("buyersAdd", buyer)
    fun requestAllBuyers() = repository.send_request<Buyers>("buyersGet")

    // Suppliers
    fun getSuppliersLiveData() = repository.suppliersLiveData
    fun addNewSupplier(supplier: Suppliers) = repository.send_request("suppliersAdd", supplier)
    fun requestAllSuppliers() = repository.send_request<Suppliers>("suppliersGet")


    // Profile
    fun getProfileLiveData() = repository.profileLiveData
    fun requestProfile() = repository.send_request<Profile>("profileGet")
    fun updateProfile(profile: Profile) = repository.send_request("profileUpdate", profile)


    // Warehouses
    fun getWarehousesLiveData() = repository.warehousesLiveData
    fun addNewWarehouse(warehouse: Warehouses) = repository.send_request("warehousesAdd", warehouse)
    fun requestAllWarehouses() = repository.send_request<Warehouses>("warehousesGet")


    // Documents


    // Products
    fun getProductsLiveData() = repository.productsLiveData
    fun addNewProduct(product: Product) = repository.send_request("productsAdd", product)
    fun requestAllProducts() = repository.send_request<Product>("productsGet")

}