package com.example.warehouse_accounting.ui.products

import androidx.lifecycle.*
import com.example.warehouse_accounting.ServerController.Service.nya
import com.example.warehouse_accounting.models.Product
import kotlinx.coroutines.*

class ProductsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val nyaService: nya
) : ViewModel() {
    private val _allProducts = mutableListOf<Product>()
    private val _products = MutableLiveData<MutableList<Product>>(mutableListOf())
    val products: LiveData<MutableList<Product>> = _products

    val searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData("searchQuery", "")

    private val _notificationEvent = MutableLiveData<Pair<String, String>>()
    val notificationEvent: LiveData<Pair<String, String>> = _notificationEvent

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    private val productsObserver = Observer<MutableList<Product>> { list ->
        println("=== PRODUCTS OBSERVER ===")
        println("Получены продукты с сервера: ${list.size}")
        list.forEach { product ->
            println("Product: id=${product.id}, name=${product.name}, barcode=${product.barcode}")
        }

        _allProducts.clear()
        _allProducts.addAll(list)
        filterProducts(searchQuery.value ?: "")

        println("_allProducts обновлен, размер: ${_allProducts.size}")
    }

    init {
        nyaService.getProductsLiveData().observeForever(productsObserver)
        startUpdatingProducts()
    }

    private fun startUpdatingProducts() {
        viewModelScope.launch {
            while (isActive) {
                loadUpdatedProducts()
                delay(5000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        nyaService.getProductsLiveData().removeObserver(productsObserver)
    }

    fun addProducts(product: Product) {
        println("=== ДОБАВЛЕНИЕ ПРОДУКТА ===")
        println("Добавляем: $product")

        _allProducts.add(product)
        filterProducts(searchQuery.value ?: "")

        nyaService.addNewProduct(product)
        _notificationEvent.value = "Добавлен товар" to "Товар \"${product.name}\" успешно добавлен."
    }

    fun updateProducts(updatedProduct: Product) {
        println("=== ОБНОВЛЕНИЕ ПРОДУКТА ===")
        println("Обновляем: $updatedProduct")

        if (updatedProduct.id != 0) {
            _allProducts.replaceAll { if (it.id == updatedProduct.id) updatedProduct else it }
        } else {
            _allProducts.replaceAll { if (it.barcode == updatedProduct.barcode) updatedProduct else it }
        }

        filterProducts(searchQuery.value ?: "")

        nyaService.updateProduct(updatedProduct)
        _notificationEvent.value = "Товар обновлён" to "Товар \"${updatedProduct.name}\" успешно обновлён."
    }

    fun deleteProduct(product: Product) {
        println("=== УДАЛЕНИЕ ПРОДУКТА ===")
        println("Удаляем: $product")

        _allProducts.removeAll { it.id == product.id }
        filterProducts(searchQuery.value ?: "")

        nyaService.deleteProduct(product)
        _notificationEvent.value = "Товар удалён" to "Товар \"${product.name}\" успешно удалён."
    }

    fun loadUpdatedProducts() {
        println("=== ЗАПРОС ОБНОВЛЕНИЯ ПРОДУКТОВ ===")
        nyaService.requestAllProducts()
    }

    fun getId(): Int = 0

    fun filterProducts(query: String) {
        searchQuery.value = query
        _products.value = if (query.isEmpty()) {
            _allProducts.toMutableList()
        } else {
            _allProducts.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }

        println("Фильтрация: запрос='$query', результат=${_products.value?.size} продуктов")
    }
}
