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
import com.example.warehouse_accounting.models.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val productList = mutableListOf<Product>()

    private lateinit var ProductFabHelper: ProductFabHelper
    private lateinit var productLongClickHelper: ProductLongClickHelper
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val productsViewModel =
            ViewModelProvider(this).get(ProductsViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        productLongClickHelper = ProductLongClickHelper(requireContext())
        ProductFabHelper = ProductFabHelper(requireContext(), productList) { product ->
            adapter.notifyDataSetChanged()
        }

        adapter = ProductAdapter(productList, productLongClickHelper) { product ->
            ProductFabHelper.showEditProductDialog(product) { updatedProduct ->
                val index = productList.indexOf(product)
                if (index != -1) {
                    productList[index] = updatedProduct
                    adapter.notifyItemChanged(index)
                }
            }
        }
        binding.rvProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProducts.adapter = adapter

        val fab: FloatingActionButton = binding.fabProducts
        fab.setOnClickListener {
            ProductFabHelper.showAddProductDialog()
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ProductFabHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
