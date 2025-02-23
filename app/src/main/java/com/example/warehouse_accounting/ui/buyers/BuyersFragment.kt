package com.example.warehouse_accounting.ui.buyers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.warehouse_accounting.databinding.FragmentBuyersBinding

class BuyersFragment : Fragment() {

    private var _binding: FragmentBuyersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val buyersViewModel =
            ViewModelProvider(this).get(BuyersViewModel::class.java)

        _binding = FragmentBuyersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textBuyers
        buyersViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}