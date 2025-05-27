package com.example.warehouse_accounting.ServerController

import com.example.warehouse_accounting.ServerController.Repositories.poka_tak

object ReposRepository {
    val instance = poka_tak.getInstance()

    fun register(login: String, password: String) = instance.register(login, password)
    fun login(login: String, password: String) = instance.login(login, password)
    val profileLiveData = instance.profileLiveData
    val buyersLiveData = instance.buyersLiveData
    val suppliersLiveData = instance.suppliersLiveData
    val warehousesLiveData = instance.warehousesLiveData
    val documentsLiveData = instance.documentsLiveData
    val productsLiveData = instance.productsLiveData
}
