package com.example.warehouse_accounting.ui.costs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.warehouse_accounting.databinding.FragmentCostsBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CostsFragment : Fragment() {

    private var _binding: FragmentCostsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val costsViewModel =
            ViewModelProvider(this).get(CostsViewModel::class.java)

        _binding = FragmentCostsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCosts
        costsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val fab: FloatingActionButton = binding.fabCosts
        fab.setOnClickListener {
            Toast.makeText(requireContext(), "Добавление затратов", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}