package com.example.warehouse_accounting.ui.suppliers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.warehouse_accounting.databinding.FragmentSuppliersBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SuppliersFragment : Fragment() {

    private var _binding: FragmentSuppliersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val suppliersViewModel =
            ViewModelProvider(this).get(SuppliersViewModel::class.java)

        _binding = FragmentSuppliersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSuppliers
        suppliersViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val fab: FloatingActionButton = binding.fabSuppliers
        fab.setOnClickListener {
            Toast.makeText(requireContext(), "Добавление поставщиков", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}