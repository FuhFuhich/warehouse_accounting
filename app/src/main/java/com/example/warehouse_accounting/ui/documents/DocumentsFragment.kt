// File: ui/documents/DocumentsFragment.kt
package com.example.warehouse_accounting.ui.documents

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.databinding.FragmentDocumentsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DocumentsFragment : Fragment() {

    private var _binding: FragmentDocumentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DocumentsViewModel
    private lateinit var documentsFabHelper: DocumentsFabHelper
    private lateinit var documentsLongClickHelper: DocumentsLongClickHelper
    private lateinit var adapter: DocumentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(DocumentsViewModel::class.java)
        _binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        documentsLongClickHelper = DocumentsLongClickHelper(requireContext())

        documentsFabHelper = DocumentsFabHelper(requireContext(),
            object : DocumentsFabHelper.OnDocumentsTypeSelectedListener {
                override fun onDocumentsTypeSelected(documentType: String) {
                    viewModel.addDocumentByType(documentType)
                }
            }
        )

        adapter = DocumentsAdapter(
            mutableListOf(),
            documentsLongClickHelper
        ) { document ->
            documentsLongClickHelper.showToast("Редактирование пока не реализовано")
        }
        binding.rvDocuments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDocuments.adapter = adapter

        viewModel.documents.observe(viewLifecycleOwner) { docs ->
            adapter.updateDocuments(docs)
            binding.tvNoDocuments.isVisible = docs.isEmpty()
            binding.rvDocuments.isVisible = docs.isNotEmpty()
        }

        val fab: FloatingActionButton = binding.fabDocuments
        fab.setOnClickListener {
            documentsFabHelper.showDocumentTypeSelectionDialog()
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_menu_action_bar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.icon?.setTint(Color.WHITE)
        val updateItem = menu.findItem(R.id.action_update)
        updateItem.icon?.setTint(Color.WHITE)

        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        val isLandscape = resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
        searchView.maxWidth = if (isLandscape) Integer.MAX_VALUE else 800

        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            if (!query.isNullOrEmpty()) {
                searchItem.expandActionView()
                searchView.setQuery(query, false)
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterDocuments(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.filterDocuments("")
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_update -> {
                viewModel.loadUpdatedDocuments()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
