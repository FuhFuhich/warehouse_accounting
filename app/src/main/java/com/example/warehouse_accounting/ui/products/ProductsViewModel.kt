package com.example.warehouse_accounting.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Product

class ProductsViewModel : ViewModel() {
    private val _allProducts = mutableListOf<Product>()
    private val _products = MutableLiveData<MutableList<Product>>(mutableListOf())
    val products: LiveData<MutableList<Product>> = _products

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    fun addProducts(product: Product) {
        _allProducts.add(product)
        filterProducts(_searchQuery.value ?: "")
    }

    fun updateProducts(updatedProduct: Product) {
        _allProducts.replaceAll { if (it.barcode == updatedProduct.barcode) updatedProduct else it }
        filterProducts(_searchQuery.value ?: "")
    }

    fun loadUpdatedProducts() {
        //_products.value = fetchProductsFromDatabase()
    }

    fun filterProducts(query: String) {
        _searchQuery.value = query
        _products.value = if (query.isEmpty()) {
            _allProducts.toMutableList()
        } else {
            _allProducts.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }
    }
}