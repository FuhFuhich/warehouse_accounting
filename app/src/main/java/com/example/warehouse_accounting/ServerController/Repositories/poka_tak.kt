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

class poka_tak private constructor() {

    companion object {
        val json = Json { ignoreUnknownKeys = true }

        @Volatile
        private var INSTANCE: poka_tak? = null

        fun getInstance(): poka_tak {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: poka_tak().also { INSTANCE = it }
            }
        }
    }

    val webSocketConnection = GlobalWebSocket.instance
    var currentProfile: Profile? = null
        private set

    val isProfileLoaded: Boolean
        get() = currentProfile != null

    val buyersLiveData = MutableLiveData<MutableList<Buyers>>()
    val suppliersLiveData = MutableLiveData<MutableList<Suppliers>>()
    val profileLiveData = MutableLiveData<Profile?>()
    val warehousesLiveData = MutableLiveData<MutableList<Warehouses>>()
    val documentsLiveData = MutableLiveData<MutableList<Documents>>()
    val productsLiveData = MutableLiveData<MutableList<Product>>()
    val errorLiveData = MutableLiveData<String?>()

    init {
        webSocketConnection.onTextMessage = { message ->
            handle_request(message)
        }
    }

    inline fun <reified T : Any> send_request(type: String, data: T? = null) {
        val prefix = currentProfile?.id_user?.toString() ?: "-1"
        val body   = data?.let { json.encodeToString(it) } ?: ""
        val message = if (body.isNotEmpty()) {
            "$type $prefix $body"
        } else {
            "$type $prefix"
        }

        println("=== SEND_REQUEST ===")
        println("CurrentProfile: $currentProfile")
        println("CurrentProfile ID: ${currentProfile?.id_user}")
        println("Отправляем запрос с ID: $prefix, сообщение: $message")
        webSocketConnection.sendMessage(message)
    }

    fun handle_request(message: String) {
        when {
            message.startsWith("buyersGet")                    -> handleBuyers(message.removePrefix("buyersGet").trim())
            message.startsWith("suppliersGet")                 -> handleSuppliers(message.removePrefix("suppliersGet").trim())
            message.startsWith("profileGet")                   -> handleProfile(message.removePrefix("profileGet").trim())
            message.startsWith("warehousesGetWithQuantity")    -> handleWarehouses(message.removePrefix("warehousesGetWithQuantity").trim()) // НОВЫЙ
            message.startsWith("warehousesGet")                -> handleWarehouses(message.removePrefix("warehousesGet").trim())
            message.startsWith("documentsGet")                 -> handleDocuments(message.removePrefix("documentsGet").trim())
            message.startsWith("productsGet")                  -> handleProducts(message.removePrefix("productsGet").trim())
            message.startsWith("registration")                 -> handleProfile(message.removePrefix("registration").trim())
            message.startsWith("login")                        -> handleProfile(message.removePrefix("login").trim())
            else                                               -> handleUnknown(message)
        }
    }

    private fun handleBuyers(data: String) {
        println("=== HANDLE_BUYERS ===")
        println("Обработка buyers: $data")
        try {
            if (data.isEmpty() || data == "[]") {
                println("Получен пустой список покупателей")
                buyersLiveData.postValue(mutableListOf())
                return
            }

            val buyersList = Json.decodeFromString<List<Buyers>>(data)
            println("Успешно разобран JSON, получено ${buyersList.size} покупателей:")
            buyersList.forEach { buyer ->
                println("  - Buyer: id=${buyer.id}, name=${buyer.name}")
            }

            buyersLiveData.postValue(buyersList.toMutableList())
            println("buyersLiveData обновлен")
        } catch (e: Exception) {
            println("Ошибка парсинга покупателей: ${e.message}")
            println("JSON input: $data")
            e.printStackTrace()

            buyersLiveData.postValue(mutableListOf())
        }
    }

    private fun handleSuppliers(data: String) {
        println("=== HANDLE_SUPPLIERS ===")
        println("Обработка suppliers: $data")
        try {
            if (data.isEmpty() || data == "[]") {
                println("Получен пустой список поставщиков")
                suppliersLiveData.postValue(mutableListOf())
                return
            }

            val suppliersList = Json.decodeFromString<List<Suppliers>>(data)
            println("Успешно разобран JSON, получено ${suppliersList.size} поставщиков:")
            suppliersList.forEach { supplier ->
                println("  - Supplier: id=${supplier.id}, name=${supplier.name}")
            }

            suppliersLiveData.postValue(suppliersList.toMutableList())
            println("suppliersLiveData обновлен")
        } catch (e: Exception) {
            println("Ошибка парсинга поставщиков: ${e.message}")
            println("JSON input: $data")
            e.printStackTrace()

            suppliersLiveData.postValue(mutableListOf())
        }
    }

    private fun handleProfile(data: String) {
        println("=== HANDLE_PROFILE ===")
        println("Обработка profile: $data")
        println("Текущий currentProfile ДО обновления: $currentProfile")

        try {
            if (data.contains("error") || data.isEmpty()) {
                println("Ошибка авторизации")

                // Определяем тип ошибки
                val errorMessage = when {
                    data.contains("Invalid credentials") -> "Неправильный логин или пароль"
                    data.contains("Database error") -> "Ошибка базы данных"
                    data.contains("Query error") -> "Ошибка запроса"
                    else -> "Ошибка авторизации"
                }

                errorLiveData.postValue(errorMessage)
                profileLiveData.postValue(null)
                return
            }

            errorLiveData.postValue(null)

            val profile = Json.decodeFromString<Profile>(data)
            currentProfile = profile
            println("=== CURRENTPROFILE ОБНОВЛЕН ===")
            println("Новый currentProfile: $currentProfile")
            println("Установлен currentProfile с ID: ${profile.id_user}")
            profileLiveData.postValue(profile)
            println("Профиль успешно обработан: $profile")
        } catch (e: Exception) {
            println("Ошибка парсинга профиля: ${e.message}")
            errorLiveData.postValue("Ошибка обработки данных")
            profileLiveData.postValue(null)
        }
    }

    private fun handleWarehouses(data: String) {
        println("=== HANDLE_WAREHOUSES ===")
        println("Обработка warehouses: $data")
        try {
            if (data.isEmpty() || data == "[]") {
                println("Получен пустой список складов")
                warehousesLiveData.postValue(mutableListOf())
                return
            }

            val warehousesList = Json.decodeFromString<List<Warehouses>>(data)
            println("Успешно разобран JSON, получено ${warehousesList.size} складов:")
            warehousesList.forEach { warehouse ->
                println("  - Warehouse: id=${warehouse.id}, name=${warehouse.warehousesName}")
            }

            warehousesLiveData.postValue(warehousesList.toMutableList())
            println("warehousesLiveData обновлен")
        } catch (e: Exception) {
            println("Ошибка парсинга складов: ${e.message}")
            println("JSON input: $data")
            e.printStackTrace()

            warehousesLiveData.postValue(mutableListOf())
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
        println("=== HANDLE_PRODUCTS ===")
        println("Обработка products: $data")
        try {
            if (data.isEmpty() || data == "[]") {
                println("Получен пустой список продуктов")
                productsLiveData.postValue(mutableListOf())
                return
            }

            val productsList = Json.decodeFromString<List<Product>>(data)
            println("Успешно разобран JSON, получено ${productsList.size} продуктов:")
            productsList.forEach { product ->
                println("  - Product: id=${product.id}, name=${product.name}, warehouse=${product.warehouse}")
            }

            productsLiveData.postValue(productsList.toMutableList())
            println("productsLiveData обновлен")
        } catch (e: Exception) {
            println("Ошибка парсинга товаров: ${e.message}")
            println("JSON input: $data")
            e.printStackTrace()

            productsLiveData.postValue(mutableListOf())
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
