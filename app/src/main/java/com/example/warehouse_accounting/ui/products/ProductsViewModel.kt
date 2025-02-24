package com.example.warehouse_accounting.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Product

class ProductsViewModel : ViewModel() {
    private val _products = MutableLiveData<MutableList<Product>>(mutableListOf())
    val products: LiveData<MutableList<Product>> = _products

    fun addProduct(product: Product) {
        _products.value = (_products.value ?: mutableListOf()).apply { add(product) }
    }

    fun updateProduct(updatedProduct: Product) {
        _products.value = _products.value?.map {
            if (it.barcode == updatedProduct.barcode) updatedProduct else it
        }?.toMutableList()
    }
}
