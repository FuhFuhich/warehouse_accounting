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
        println("=== BUYERS OBSERVER ===")
        println("Получены покупатели с сервера: ${list.size}")
        list.forEach { buyer ->
            println("Buyer: id=${buyer.id}, name=${buyer.name}")
        }

        _allBuyers.clear()
        _allBuyers.addAll(list)
        filterBuyers(searchQuery.value ?: "")

        println("_allBuyers обновлен, размер: ${_allBuyers.size}")
    }

    init {
        nyaService.getBuyersLiveData().observeForever(buyersObserver)
        startUpdatingBuyers()
    }

    private fun startUpdatingBuyers() {
        viewModelScope.launch {
            while (isActive) {
                loadUpdatedBuyers()
                delay(5000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
        nyaService.getBuyersLiveData().removeObserver(buyersObserver)
    }

    fun addBuyers(buyers: Buyers) {
        println("=== ДОБАВЛЕНИЕ ПОКУПАТЕЛЯ ===")
        println("Добавляем: $buyers")

        _allBuyers.add(buyers)
        filterBuyers(searchQuery.value ?: "")

        nyaService.addNewBuyer(buyers)
        _notificationEvent.value = "Добавлен покупатель" to "Покупатель \"${buyers.name}\" успешно добавлен."
    }

    fun updateBuyers(updatedBuyers: Buyers) {
        println("=== ОБНОВЛЕНИЕ ПОКУПАТЕЛЯ ===")
        println("Обновляем: $updatedBuyers")

        if (updatedBuyers.id != 0) {
            _allBuyers.replaceAll { if (it.id == updatedBuyers.id) updatedBuyers else it }
        } else {
            _allBuyers.replaceAll { if (it.name == updatedBuyers.name) updatedBuyers else it }
        }

        filterBuyers(searchQuery.value ?: "")

        nyaService.updateBuyer(updatedBuyers)
        _notificationEvent.value = "Покупатель обновлён" to "Покупатель \"${updatedBuyers.name}\" успешно обновлён."
    }

    fun deleteBuyer(buyer: Buyers) {
        println("=== УДАЛЕНИЕ ПОКУПАТЕЛЯ ===")
        println("Удаляем: $buyer")

        _allBuyers.removeAll { it.id == buyer.id }
        filterBuyers(searchQuery.value ?: "")

        nyaService.deleteBuyer(buyer)
        _notificationEvent.value = "Покупатель удалён" to "Покупатель \"${buyer.name}\" успешно удалён."
    }

    fun loadUpdatedBuyers() {
        println("=== ЗАПРОС ОБНОВЛЕНИЯ ПОКУПАТЕЛЕЙ ===")
        nyaService.requestAllBuyers()
    }

    fun getId(): Int = 0

    fun filterBuyers(query: String) {
        searchQuery.value = query
        _buyers.value = if (query.isEmpty()) {
            _allBuyers.toMutableList()
        } else {
            _allBuyers.filter { it.name.contains(query, ignoreCase = true) }.toMutableList()
        }

        println("Фильтрация: запрос='$query', результат=${_buyers.value?.size} покупателей")
    }
}
