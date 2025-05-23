package com.example.warehouse_accounting.ui.buyers

import androidx.lifecycle.*
import com.example.warehouse_accounting.ServerController.Service.nya
import com.example.warehouse_accounting.models.Buyers
import kotlinx.coroutines.*

class BuyersViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val nyaService: nya
) : ViewModel() {
    private val _allBuyers = mutableListOf<Buyers>()
    private val _buyers = MutableLiveData<MutableList<Buyers>>(mutableListOf())
    val buyers: LiveData<MutableList<Buyers>> = _buyers

    val searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData("searchQuery", "")

    private val _notificationEvent = MutableLiveData<Pair<String, String>>()
    val notificationEvent: LiveData<Pair<String, String>> = _notificationEvent

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    private val buyersObserver = Observer<MutableList<Buyers>> { list ->
        _allBuyers.clear()
        _allBuyers.addAll(list)
        filterBuyers(searchQuery.value ?: "")
    }

    init {
        nyaService.getBuyersLiveData().observeForever(buyersObserver)
        startUpdatingProducts()
    }

    private fun startUpdatingProducts() {
        viewModelScope.launch {
            while (isActive) {
                loadUpdatedBuyers()
                delay(10000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        nyaService.getBuyersLiveData().removeObserver(buyersObserver)
    }

    fun addBuyers(buyers: Buyers) {
        _allBuyers.add(buyers)
        nyaService.addNewBuyer(buyers)
        filterBuyers(searchQuery.value ?: "")
        _notificationEvent.value = "Добавлен покупатель" to "Покупатель \"${buyers.name}\" успешно добавлен."
    }

    fun updateBuyers(updatedBuyers: Buyers) {
        _allBuyers.replaceAll { if (it.tin == updatedBuyers.tin) updatedBuyers else it }
        filterBuyers(searchQuery.value ?: "")
        _notificationEvent.value = "Покупатель обновлён" to "Покупатель \"${updatedBuyers.name}\" успешно обновлён."
    }

    fun loadUpdatedBuyers() {
        nyaService.requestAllBuyers()
    }

    fun getId(): Int {
        return 0
    }

    fun filterBuyers(query: String) {
        searchQuery.value = query
        _buyers.value = if (query.isEmpty()) {
            _allBuyers.toMutableList()
        } else {
            _allBuyers.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }
    }
}
