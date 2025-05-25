package com.example.warehouse_accounting.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Profile
import com.example.warehouse_accounting.ui.auth.AuthViewModel

class LoginFragment : Fragment() {
    private val vm: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val etLogin    = view.findViewById<EditText>(R.id.et_register_login)
        val etPassword = view.findViewById<EditText>(R.id.et_register_password)
        val btnLogin   = view.findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val pass  = etPassword.text.toString().trim()
            vm.login(login, pass)
        }

        vm.profileLiveData.observe(viewLifecycleOwner) { prof ->
            if (prof != null) {
                Toast.makeText(
                    requireContext(),
                    "Logged in id=${prof.id_user}",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.nav_profile)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Invalid credentials",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return view
    }
}
