package com.example.warehouse_accounting.ui.registration

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

class RegisterFragment : Fragment() {
    private val vm: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val etLogin    = view.findViewById<EditText>(R.id.et_login_login)
        val etPassword = view.findViewById<EditText>(R.id.et_login_password)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val pass  = etPassword.text.toString().trim()
            vm.register(login, pass)
        }

        vm.profileLiveData.observe(viewLifecycleOwner) { prof ->
            if (prof != null) {
                Toast.makeText(
                    requireContext(),
                    "Registered id=${prof.id_user}",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.nav_profile)
            }
        }
        return view
    }
}
