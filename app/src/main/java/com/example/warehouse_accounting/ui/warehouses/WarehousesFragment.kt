package com.example.warehouse_accounting.ui.warehouses

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
import com.example.warehouse_accounting.databinding.FragmentWarehousesBinding
import com.example.warehouse_accounting.ui.buyers.BuyersFabHelper
import com.example.warehouse_accounting.utils.NotificationUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WarehousesFragment : Fragment() {

    private var _binding: FragmentWarehousesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: WarehousesViewModel
    private lateinit var warehousesFabHelper: WarehousesFabHelper
    private lateinit var warehousesLongClickHelper: WarehousesLongClickHelper
    private lateinit var adapter: WarehousesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            warehousesFabHelper.handleActivityResult(result.resultCode, result.data)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(WarehousesViewModel::class.java)
        _binding = FragmentWarehousesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        warehousesLongClickHelper = WarehousesLongClickHelper(requireContext())
        warehousesFabHelper = WarehousesFabHelper(
            requireContext(),
            { warehouses -> viewModel.addWarehouses(warehouses) },
            viewModel
        )

        adapter = WarehousesAdapter(mutableListOf(), warehousesLongClickHelper) { warehouses ->
            warehousesFabHelper.showEditWarehousesDialog(warehouses) { updatedWarehouses ->
                viewModel.updateWarehouses(updatedWarehouses)
            }
        }
        binding.rvWarehouses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWarehouses.adapter = adapter

        viewModel.warehouses.observe(viewLifecycleOwner) { warehouses ->
            adapter.updateWarehouses(warehouses)
        }

        val fab: FloatingActionButton = binding.fabWarehouses
        fab.setOnClickListener {
            warehousesFabHelper.showAddWarehousesDialog()
        }

        viewModel.notificationEvent.observe(viewLifecycleOwner) { (title, message) ->
            NotificationUtils.showNotification(requireContext(), title, message)
        }

        viewModel.warehouses.observe(viewLifecycleOwner) { warehouses ->
            adapter.updateWarehouses(warehouses)

            if (warehouses.isEmpty()) {
                binding.tvNoWarehouses.visibility = View.VISIBLE
                binding.rvWarehouses.visibility = View.GONE
            } else {
                binding.tvNoWarehouses.visibility = View.GONE
                binding.rvWarehouses.visibility = View.VISIBLE
            }
        }

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.warehouses_menu_action_bar, menu)
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
                viewModel.filterWarehouses(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.filterWarehouses("")
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update -> {
                updateWarehousesList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateWarehousesList() {
        viewModel.loadUpdatedWarehouses()
    }

}
