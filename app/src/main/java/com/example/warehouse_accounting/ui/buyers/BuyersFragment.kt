package com.example.warehouse_accounting.ui.buyers

import android.content.Intent
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
import com.example.warehouse_accounting.databinding.FragmentBuyersBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BuyersFragment : Fragment() {

    private var _binding: FragmentBuyersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: BuyersViewModel
    private lateinit var buyersFabHelper: BuyersFabHelper
    private lateinit var buyersLongClickHelper: BuyersLongClickHelper
    private lateinit var adapter: BuyersAdapter

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            buyersFabHelper.handleActivityResult(result.resultCode, result.data)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(BuyersViewModel::class.java)
        _binding = FragmentBuyersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        buyersLongClickHelper = BuyersLongClickHelper(requireContext())
        buyersFabHelper = BuyersFabHelper(requireContext(), activityResultLauncher) { buyers ->
            viewModel.addBuyers(buyers)
        }

        adapter = BuyersAdapter(mutableListOf(), buyersLongClickHelper) { buyers ->
            buyersFabHelper.showEditBuyersDialog(buyers) { updatedBuyers ->
                viewModel.updateBuyers(updatedBuyers)
            }
        }
        binding.rvBuyers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBuyers.adapter = adapter

        viewModel.buyers.observe(viewLifecycleOwner) { buyers ->
            adapter.updateBuyers(buyers)
        }

        val fab: FloatingActionButton = binding.fabBuyers
        fab.setOnClickListener {
            buyersFabHelper.showAddBuyersDialog()
        }

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

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.buyers_menu_action_bar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
