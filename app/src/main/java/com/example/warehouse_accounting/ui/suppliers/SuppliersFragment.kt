package com.example.warehouse_accounting.ui.suppliers

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.databinding.FragmentSuppliersBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SuppliersFragment : Fragment() {

    private var _binding: FragmentSuppliersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SuppliersViewModel
    private lateinit var suppliersFabHelper: SuppliersFabHelper
    private lateinit var suppliersLongClickHelper: SuppliersLongClickHelper
    private lateinit var adapter: SuppliersAdapter

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            suppliersFabHelper.handleActivityResult(result.resultCode, result.data)
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
        viewModel = ViewModelProvider(this).get(SuppliersViewModel::class.java)
        _binding = FragmentSuppliersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        suppliersLongClickHelper = SuppliersLongClickHelper(requireContext())
        suppliersFabHelper = SuppliersFabHelper(requireContext(), activityResultLauncher) { suppliers ->
            viewModel.addSuppliers(suppliers)
        }

        adapter = SuppliersAdapter(mutableListOf(), suppliersLongClickHelper) { suppliers ->
            suppliersFabHelper.showEditSuppliersDialog(suppliers) { updatedSuppliers ->
                viewModel.updateSuppliers(updatedSuppliers)
            }
        }
        binding.rvSuppliers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSuppliers.adapter = adapter

        viewModel.suppliers.observe(viewLifecycleOwner) { suppliers ->
            adapter.updateSuppliers(suppliers)
        }

        val fab: FloatingActionButton = binding.fabSuppliers
        fab.setOnClickListener {
            suppliersFabHelper.showAddSuppliersDialog()
        }

        viewModel.suppliers.observe(viewLifecycleOwner) { suppliers ->
            adapter.updateSuppliers(suppliers)

            if (suppliers.isEmpty()) {
                binding.tvNoSuppliers.visibility = View.VISIBLE
                binding.rvSuppliers.visibility = View.GONE
            } else {
                binding.tvNoSuppliers.visibility = View.GONE
                binding.rvSuppliers.visibility = View.VISIBLE
            }
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.suppliers_menu_action_bar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.icon?.setTint(Color.WHITE)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterSuppliers(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.filterSuppliers("")
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
