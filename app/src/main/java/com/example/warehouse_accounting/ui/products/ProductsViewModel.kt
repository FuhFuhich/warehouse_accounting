package com.example.warehouse_accounting.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.warehouse_accounting.models.Product

class ProductsViewModel : ViewModel() {
    private val _products = MutableLiveData<MutableList<Product>>(mutableListOf())
    val products: LiveData<MutableList<Product>> = _products

    fun addProduct(product: Product) {
        _products.value?.add(product)
        _products.value = _products.value
    }

    fun updateProduct(updatedProduct: Product) {
        val currentList = _products.value ?: return
        val index = currentList.indexOfFirst { it.barcode == updatedProduct.barcode }
        if (index != -1) {
            currentList[index] = updatedProduct
            _products.value = currentList
        }
    }
}
