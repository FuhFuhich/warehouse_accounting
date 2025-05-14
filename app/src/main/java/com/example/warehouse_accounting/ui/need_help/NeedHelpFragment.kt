package com.example.warehouse_accounting.ui.need_help

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.warehouse_accounting.databinding.FragmentNeedHelpBinding

class NeedHelpFragment : Fragment() {

    private var _binding: FragmentNeedHelpBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NeedHelpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(NeedHelpViewModel::class.java)
        _binding = FragmentNeedHelpBinding.inflate(inflater, container, false)

        val root: View = binding.root

        viewModel.message.observe(viewLifecycleOwner) { savedText ->
            binding.editTextMessage.setText(savedText)
        }

        binding.editTextMessage.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.message.value = binding.editTextMessage.text.toString()
            }
        }

        binding.buttonSend.setOnClickListener {
            val message = binding.editTextMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendEmail(message)
                binding.editTextMessage.setText("")
                viewModel.message.value = ""
            }
        }

        return root
    }

    private fun sendEmail(message: String) {
        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("ilyegorow@yandex.ru"))
            putExtra(Intent.EXTRA_SUBJECT, "Сообщение из приложения")
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Отправить сообщение через:"))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Нет установленного почтового клиента", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.message.value = binding.editTextMessage.text.toString()
        _binding = null
    }
}
