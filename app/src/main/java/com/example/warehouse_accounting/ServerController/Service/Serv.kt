package com.example.warehouse_accounting.ServerController.Service

import com.example.warehouse_accounting.ServerController.Repositories.poka_tak
import com.example.warehouse_accounting.models.Buyers
import com.example.warehouse_accounting.models.Product
import com.example.warehouse_accounting.models.Profile
import com.example.warehouse_accounting.models.Suppliers
import com.example.warehouse_accounting.models.Warehouses

class Serv(private val repository: poka_tak) {

    // Buyers
    fun getBuyersLiveData() = repository.buyersLiveData
    fun addNewBuyer(buyer: Buyers) = repository.send_request("buyersAdd", buyer)
    fun updateBuyer(buyer: Buyers) = repository.send_request("buyersUpdate", buyer)
    fun deleteBuyer(buyer: Buyers) = repository.send_request("buyersDelete", buyer)
    fun requestAllBuyers() = repository.send_request<Buyers>("buyersGet")

    // Suppliers
    fun getSuppliersLiveData() = repository.suppliersLiveData
    fun addNewSupplier(supplier: Suppliers) = repository.send_request("suppliersAdd", supplier)
    fun updateSupplier(supplier: Suppliers) = repository.send_request("suppliersUpdate", supplier)
    fun deleteSupplier(supplier: Suppliers) = repository.send_request("suppliersDelete", supplier)
    fun requestAllSuppliers() = repository.send_request<Suppliers>("suppliersGet")

    // Profile
    fun getProfileLiveData() = repository.profileLiveData
    fun requestProfile() = repository.send_request<Profile>("profileGet")
    fun updateProfile(profile: Profile) = repository.send_request("profileUpdate", profile)

    // Warehouses
    fun getWarehousesLiveData() = repository.warehousesLiveData
    fun addNewWarehouse(warehouse: Warehouses) = repository.send_request("warehousesAdd", warehouse)
    fun updateWarehouse(warehouse: Warehouses) = repository.send_request("warehousesUpdate", warehouse)
    fun deleteWarehouse(warehouse: Warehouses) = repository.send_request("warehousesDelete", warehouse)
    fun requestAllWarehouses() = repository.send_request<Warehouses>("warehousesGet")
    fun requestWarehousesWithQuantity() = repository.send_request<Warehouses>("warehousesGetWithQuantity")

    // Products
    fun getProductsLiveData() = repository.productsLiveData
    fun addNewProduct(product: Product) = repository.send_request("productsAdd", product)
    fun updateProduct(product: Product) = repository.send_request("productsUpdate", product)
    fun deleteProduct(product: Product) = repository.send_request("productsDelete", product)
    fun requestAllProducts() = repository.send_request<Product>("productsGet")
}
