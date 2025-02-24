package com.example.warehouse_accounting.ui.suppliers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse_accounting.databinding.FragmentSuppliersBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SuppliersFragment : Fragment() {

    private var _binding: FragmentSuppliersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SuppliersViewModel

    private lateinit var suppliersFabHelper: SuppliersFabHelper
    private lateinit var suppliersLongClickHelper: SuppliersLongClickHelper
    private lateinit var adapter: SuppliersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(SuppliersViewModel::class.java)
        _binding = FragmentSuppliersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        suppliersLongClickHelper = SuppliersLongClickHelper(requireContext())
        suppliersFabHelper = SuppliersFabHelper(requireContext()) { suppliers ->
            viewModel.addSuppliers(suppliers)
        }

        adapter = SuppliersAdapter(mutableListOf(), suppliersLongClickHelper) { suppliers ->
            suppliersFabHelper.showEditSuppliersDialog(suppliers) { updatedSuppliers ->
                viewModel.updateSuppliers(updatedSuppliers)
            }
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

        viewModel.suppliers.observe(viewLifecycleOwner) { suppliers ->
            adapter.updateSuppliers(suppliers)
        }

        val fab: FloatingActionButton = binding.fabSuppliers
        fab.setOnClickListener {
            suppliersFabHelper.showAddSuppliersDialog()
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        suppliersFabHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
