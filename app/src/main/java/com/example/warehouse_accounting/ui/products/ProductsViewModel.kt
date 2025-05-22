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
        _allProducts.clear()
        _allProducts.addAll(list)
        filterProducts(searchQuery.value ?: "")
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
        _allProducts.add(product)
        filterProducts(searchQuery.value ?: "")
        _notificationEvent.value = "Добавлен товар" to "Товар \"${product.name}\" успешно добавлен."
    }

    fun updateProducts(updatedProduct: Product) {
        _allProducts.replaceAll { if (it.barcode == updatedProduct.barcode) updatedProduct else it }
        filterProducts(searchQuery.value ?: "")
        _notificationEvent.value = "Товар обновлён" to "Товар \"${updatedProduct.name}\" успешно обновлён."
    }

    fun loadUpdatedProducts() {
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
    }
}
