package com.example.warehouse_accounting.ui.documents

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
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

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            documentsFabHelper.handleActivityResult(result.resultCode, result.data)
        }

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
        documentsFabHelper = DocumentsFabHelper(requireContext(), activityResultLauncher) { documents ->
            viewModel.addDocuments(documents)
        }

        adapter = DocumentsAdapter(mutableListOf(), documentsLongClickHelper) { documents ->
            documentsFabHelper.showEditDocumentsDialog(documents) { updatedDocuments ->
                viewModel.updateDocuments(updatedDocuments)
            }
        }
        binding.rvDocuments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDocuments.adapter = adapter

        viewModel.documents.observe(viewLifecycleOwner) { documents ->
            adapter.updateDocuments(documents)
        }

        val fab: FloatingActionButton = binding.fabDocuments
        fab.setOnClickListener {
            documentsFabHelper.showAddDocumentsDialog()
        }

        viewModel.documents.observe(viewLifecycleOwner) { documents ->
            adapter.updateDocuments(documents)

            if (documents.isEmpty()) {
                binding.tvNoDocuments.visibility = View.VISIBLE
                binding.rvDocuments.visibility = View.GONE
            } else {
                binding.tvNoDocuments.visibility = View.GONE
                binding.rvDocuments.visibility = View.VISIBLE
            }
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.documents_menu_action_bar, menu)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update -> {
                updateDocumentsList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateDocumentsList() {
        viewModel.loadUpdatedDocuments()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
