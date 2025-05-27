package com.example.warehouse_accounting.ui.products

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.databinding.FragmentProductsBinding
import com.example.warehouse_accounting.utils.NotificationUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductsViewModel by viewModels {
        ProductsViewModelFactory(ServiceLocator.nyaService, this)
    }

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
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        productsLongClickHelper = ProductsLongClickHelper(requireContext())
        productsFabHelper = ProductsFabHelper(requireContext(), viewModel)

        adapter = ProductsAdapter(
            mutableListOf(),
            productsLongClickHelper,
            { product ->
                productsFabHelper.showEditProductDialog(product) { updatedProduct ->
                    viewModel.updateProducts(updatedProduct)
                }
            },
            { product ->
                viewModel.deleteProduct(product)
            }
        )

        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

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

        viewModel.notificationEvent.observe(viewLifecycleOwner) { (title, message) ->
            NotificationUtils.showNotification(requireContext(), title, message)
        }

        val fab: FloatingActionButton = binding.fabProducts
        fab.setOnClickListener {
            productsFabHelper.showAddProductDialog()
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.products_menu_action_bar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.icon?.setTint(Color.WHITE)
        val updateItem = menu.findItem(R.id.action_update)
        updateItem.icon?.setTint(Color.WHITE)

        val searchView = searchItem.actionView as SearchView

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update -> {
                updateProductsList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateProductsList() {
        viewModel.loadUpdatedProducts()
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
