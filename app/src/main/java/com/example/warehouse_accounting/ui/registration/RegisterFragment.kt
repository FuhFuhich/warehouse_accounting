package com.example.warehouse_accounting.ui.registration

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

class RegisterFragment : Fragment() {
    private val vm: AuthViewModel by activityViewModels {
        AuthViewModelFactory(PokaRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etLogin     = view.findViewById<EditText>(R.id.et_login_login)
        val etPassword  = view.findViewById<EditText>(R.id.et_login_password)
        val btnRegister = view.findViewById<Button>(R.id.btnRegister)

        vm.profileLiveData.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                findNavController().navigate(R.id.action_registerFragment_to_nav_profile)
            }
        }

        btnRegister.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val pass  = etPassword.text.toString().trim()
            vm.register(login, pass)
        }
    }
}
