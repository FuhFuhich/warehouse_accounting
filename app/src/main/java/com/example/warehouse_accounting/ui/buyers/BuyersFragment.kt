package com.example.warehouse_accounting.ui.buyers

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.databinding.FragmentBuyersBinding
import com.example.warehouse_accounting.utils.NotificationUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BuyersFragment : Fragment() {

    private var _binding: FragmentBuyersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuyersViewModel by viewModels {
        BuyersViewModelFactory(ServiceLocator.nyaService, this)
    }

    private lateinit var buyersFabHelper: BuyersFabHelper
    private lateinit var buyersLongClickHelper: BuyersLongClickHelper
    private lateinit var adapter: BuyersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            buyersFabHelper.handleActivityResult(result.resultCode, result.data)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        buyersLongClickHelper = BuyersLongClickHelper(requireContext())
        buyersFabHelper = BuyersFabHelper(
            requireContext(),
            { buyers -> viewModel.addBuyers(buyers) },
            viewModel
        )

        adapter = BuyersAdapter(
            mutableListOf(),
            buyersLongClickHelper,
            { buyer ->
                buyersFabHelper.showEditBuyersDialog(buyer) { updatedBuyer ->
                    viewModel.updateBuyers(updatedBuyer)
                }
            },
            { buyer ->
                viewModel.deleteBuyer(buyer)
            }
        )
        binding.rvBuyers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBuyers.adapter = adapter

        viewModel.buyers.observe(viewLifecycleOwner) { buyers ->
            adapter.updateBuyers(buyers)
            if (buyers.isEmpty()) {
                binding.tvNoBuyers.visibility = View.VISIBLE
                binding.rvBuyers.visibility = View.GONE
            } else {
                binding.tvNoBuyers.visibility = View.GONE
                binding.rvBuyers.visibility = View.VISIBLE
            }
        }

        val fab: FloatingActionButton = binding.fabBuyers
        fab.setOnClickListener {
            buyersFabHelper.showAddBuyersDialog()
        }

        viewModel.notificationEvent.observe(viewLifecycleOwner) { (title, message) ->
            com.example.warehouse_accounting.utils.NotificationUtils.showNotification(requireContext(), title, message)
        }

        return root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.buyers_menu_action_bar, menu)
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
                viewModel.filterBuyers(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.filterBuyers("")
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_update -> {
                updateBuyersList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateBuyersList() {
        viewModel.loadUpdatedBuyers()
    }

}
