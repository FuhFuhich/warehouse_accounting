package com.example.warehouse_accounting.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.ServerController.PokaRepository
import com.example.warehouse_accounting.ui.auth.AuthViewModel
import com.example.warehouse_accounting.ui.auth.AuthViewModelFactory

class LoginFragment : Fragment() {
    private val vm: AuthViewModel by activityViewModels {
        AuthViewModelFactory(PokaRepository.instance)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etLogin    = view.findViewById<EditText>(R.id.et_register_login)
        val etPassword = view.findViewById<EditText>(R.id.et_register_password)
        val btnLogin   = view.findViewById<Button>(R.id.btnLogin)

        vm.profileLiveData.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                findNavController().navigate(R.id.action_loginFragment_to_nav_profile)
            }
        }

        vm.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            vm.login(login, pass)
        }
    }
}
