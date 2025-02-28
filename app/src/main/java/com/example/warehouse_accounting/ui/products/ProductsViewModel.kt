package com.example.warehouse_accounting.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Product

class ProductsViewModel : ViewModel() {
    private val _allProducts = mutableListOf<Product>()
    private val _products = MutableLiveData<MutableList<Product>>(mutableListOf())
    val products: LiveData<MutableList<Product>> = _products

    fun addProduct(product: Product) {
        _allProducts.add(product)
        _products.value = _allProducts.toMutableList()
    }

    fun updateProduct(updatedProduct: Product) {
        _allProducts.replaceAll { if (it.barcode == updatedProduct.barcode) updatedProduct else it }
        _products.value = _allProducts.toMutableList()
    }

    fun filterProducts(query: String) {
        if (query.isEmpty()) {
            _products.value = _allProducts.toMutableList()
        } else {
            _products.value = _allProducts.filter {
                it.name.contains(query, ignoreCase = true)
            }.toMutableList()
        }
    }
}