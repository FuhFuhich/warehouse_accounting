package com.example.warehouse_accounting.ServerController.Repositories

import androidx.lifecycle.MutableLiveData
import com.example.warehouse_accounting.ServerController.GlobalWebSocket
import com.example.warehouse_accounting.ServerController.WebSocketConnection
import com.example.warehouse_accounting.models.Buyers
import com.example.warehouse_accounting.models.Product
import com.example.warehouse_accounting.models.Profile
import com.example.warehouse_accounting.models.Warehouses
import com.example.warehouse_accounting.models.Documents
import com.example.warehouse_accounting.models.Suppliers
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class AuthRequest(val login: String, val password: String)

open class poka_tak {

    companion object {
        val json = Json { ignoreUnknownKeys = true }
    }

    val webSocketConnection = GlobalWebSocket.instance
    var currentProfile: Profile? = null
        private set

    val buyersLiveData = MutableLiveData<MutableList<Buyers>>()
    val suppliersLiveData = MutableLiveData<MutableList<Suppliers>>()
    val profileLiveData = MutableLiveData<Profile?>()
    val warehousesLiveData = MutableLiveData<MutableList<Warehouses>>()
    val documentsLiveData = MutableLiveData<MutableList<Documents>>()
    val productsLiveData = MutableLiveData<MutableList<Product>>()

    init {
        webSocketConnection.onTextMessage = { message ->
            handle_request(message)
        }
    }

    inline fun <reified T : Any> send_request(type: String, data: T? = null) {
        val prefix = currentProfile?.id_user?.toString()?.let { "$it " } ?: ""
        val message = if (data != null) {
            val jsonStr = Companion.json.encodeToString(data)
            "$type $prefix$jsonStr"
        } else {
            (type + " " + prefix).trim()
        }
        webSocketConnection.sendMessage(message)
    }

    fun handle_request(message: String) {
        when {
            message.startsWith("buyersGet")      -> handleBuyers(message.removePrefix("buyersGet").trim())
            message.startsWith("suppliersGet")   -> handleSuppliers(message.removePrefix("suppliersGet").trim())
            message.startsWith("profileGet")     -> handleProfile(message.removePrefix("profileGet").trim())
            message.startsWith("warehousesGet")  -> handleWarehouses(message.removePrefix("warehousesGet").trim())
            message.startsWith("documentsGet")   -> handleDocuments(message.removePrefix("documentsGet").trim())
            message.startsWith("productsGet")    -> handleProducts(message.removePrefix("productsGet").trim())
            message.startsWith("registration")   -> handleProfile(message.removePrefix("registration").trim())
            message.startsWith("login")          -> handleProfile(message.removePrefix("login").trim())
            else                                 -> handleUnknown(message)
        }
    }

    private fun handleBuyers(data: String) {
        println("Обработка buyers: $data")
        try {
            val buyersList = Json.decodeFromString<List<Buyers>>(data)
            buyersLiveData.postValue(buyersList.toMutableList())
        } catch (e: Exception) {
            println("Ошибка парсинга покупателей: ${e.message}")
        }
    }

    private fun handleSuppliers(data: String) {
        println("Обработка suppliers: $data")
        try {
            val suppliersList = Json.decodeFromString<List<Suppliers>>(data)
            suppliersLiveData.postValue(suppliersList.toMutableList())
        } catch (e: Exception) {
            println("Ошибка парсинга поставщиков: ${e.message}")
        }
    }

    private fun handleProfile(data: String) {
        println("Обработка profile: $data")
        try {
            val profile = Json.decodeFromString<Profile>(data)
            currentProfile = profile
            profileLiveData.postValue(profile)
        } catch (e: Exception) {
            println("Ошибка парсинга профиля: ${e.message}")
        }
    }

    private fun handleWarehouses(data: String) {
        println("Обработка warehouses: $data")
        try {
            val warehousesList = Json.decodeFromString<List<Warehouses>>(data)
            warehousesLiveData.postValue(warehousesList.toMutableList())
        } catch (e: Exception) {
            println("Ошибка парсинга складов: ${e.message}")
        }
    }

    private fun handleDocuments(data: String) {
        println("Обработка documents: $data")
        try {
            val documentsList = Json.decodeFromString<List<Documents>>(data)
            documentsLiveData.postValue(documentsList.toMutableList())
        } catch (e: Exception) {
            println("Ошибка парсинга документов: ${e.message}")
        }
    }

    private fun handleProducts(data: String) {
        println("Обработка products: $data")
        try {
            val productsList = Json.decodeFromString<List<Product>>(data)
            productsLiveData.postValue(productsList.toMutableList())
        } catch (e: Exception) {
            println("Ошибка парсинга товаров: ${e.message}")
        }
    }

    private fun handleUnknown(data: String) {
        println("Неизвестная команда: $data")
    }

    fun register(login: String, password: String) {
        send_request("registration", AuthRequest(login, password))
    }

    fun login(login: String, password: String) {
        send_request("login", AuthRequest(login, password))
    }
}
