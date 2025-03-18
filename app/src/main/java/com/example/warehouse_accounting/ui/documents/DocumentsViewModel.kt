package com.example.warehouse_accounting.ui.documents

import androidx.lifecycle.*
import com.example.warehouse_accounting.models.Documents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DocumentsViewModel(state: SavedStateHandle) : ViewModel() {
    private val _allDocuments = mutableListOf<Documents>()
    private val _documents = MutableLiveData<MutableList<Documents>>(mutableListOf())
    val documents: LiveData<MutableList<Documents>> = _documents

    private val savedStateHandle = state
    val searchQuery: MutableLiveData<String> = savedStateHandle.getLiveData("searchQuery", "")

    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

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
        viewModelScope.cancel()
    }

    fun addDocuments(suppliers: Documents) {
        _allDocuments.add(suppliers)
        filterDocuments(searchQuery.value ?: "")
    }

    fun updateDocuments(updatedDocuments: Documents) {
        _allDocuments.replaceAll { if (it.documentsNumber == updatedDocuments.documentsNumber) updatedDocuments else it }
        filterDocuments(searchQuery.value ?: "")
    }

    fun loadUpdatedDocuments() {
        //_documents.value = fetchDocumentsFromDatabase()
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
