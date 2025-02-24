package com.example.warehouse_accounting.ui.products

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse_accounting.databinding.FragmentProductsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductsViewModel

    private lateinit var productsFabHelper: ProductsFabHelper
    private lateinit var productsLongClickHelper: ProductsLongClickHelper
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        productsLongClickHelper = ProductsLongClickHelper(requireContext())
        productsFabHelper = ProductsFabHelper(requireContext()) { product ->
            viewModel.addProduct(product)
        }

        adapter = ProductsAdapter(mutableListOf(), productsLongClickHelper) { product ->
            productsFabHelper.showEditProductDialog(product) { updatedProduct ->
                viewModel.updateProduct(updatedProduct)
            }
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.updateProducts(products)
        }

        val fab: FloatingActionButton = binding.fabProducts
        fab.setOnClickListener {
            productsFabHelper.showAddProductDialog()
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        productsFabHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
