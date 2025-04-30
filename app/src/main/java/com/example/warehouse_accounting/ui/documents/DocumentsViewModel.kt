package com.example.warehouse_accounting.ui.documents

import androidx.lifecycle.*
import com.example.warehouse_accounting.models.Documents
import kotlinx.coroutines.*
import java.util.Date

class DocumentsViewModel(state: SavedStateHandle) : ViewModel() {
    private val _allDocuments = mutableListOf<Documents>()
    private val _documents = MutableLiveData<MutableList<Documents>>(mutableListOf())
    val documents: LiveData<MutableList<Documents>> = _documents

    private val savedStateHandle = state
    val searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData("searchQuery", "")

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        startUpdatingDocuments()
    }

    private fun startUpdatingDocuments() {
        viewModelScope.launch {
            while (isActive) {
                loadUpdatedDocuments()
                delay(5000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun addDocumentByType(documentType: String) {
        val newDocument = Documents(
            documents = documentType,
            documentsNumber = System.currentTimeMillis().toString(),
            creationDate = Date().toString(),
            nameOfWarehouse = "Main Warehouse",
            quantityOfGoods = 0
        )
        _allDocuments.add(newDocument)
        filterDocuments(searchQuery.value ?: "")
    }

    fun updateDocuments(updatedDocument: Documents) {
        _allDocuments.replaceAll { if (it.documentsNumber == updatedDocument.documentsNumber) updatedDocument else it }
        filterDocuments(searchQuery.value ?: "")
    }

    fun loadUpdatedDocuments() {
        _documents.value = _allDocuments.toMutableList()
    }

    fun filterDocuments(query: String) {
        searchQuery.value = query
        _documents.value = if (query.isEmpty()) {
            _allDocuments.toMutableList()
        } else {
            _allDocuments.filter { it.documents.contains(query, ignoreCase = true) }.toMutableList()
        }
    }
}
