package com.example.warehouse_accounting.ui.products

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.databinding.FragmentProductsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductsViewModel

    private lateinit var productsFabHelper: ProductsFabHelper
    private lateinit var productsLongClickHelper: ProductsLongClickHelper
    private lateinit var adapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        viewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.updateProducts(products)

            if (products.isEmpty()) {
                binding.tvNoProducts.visibility = View.VISIBLE
                binding.rvProducts.visibility = View.GONE
            } else {
                binding.tvNoProducts.visibility = View.GONE
                binding.rvProducts.visibility = View.VISIBLE
            }
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_menu_action_bar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.icon?.setTint(Color.WHITE)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterProducts(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.filterProducts("")
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
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
