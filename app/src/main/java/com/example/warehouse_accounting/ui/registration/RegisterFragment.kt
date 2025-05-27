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
        AuthViewModelFactory(PokaRepository.instance)
    }

    private lateinit var btnRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etLogin     = view.findViewById<EditText>(R.id.et_login_login)
        val etPassword  = view.findViewById<EditText>(R.id.et_login_password)
        btnRegister = view.findViewById<Button>(R.id.btnRegister)

        vm.profileLiveData.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                resetButton()
                Toast.makeText(requireContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_nav_profile)
            }
        }

        vm.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                resetButton()

                when {
                    errorMessage.contains("уже существует") || errorMessage.contains("already exists") -> {
                        Toast.makeText(requireContext(), "Этот логин уже занят! Попробуйте другой", Toast.LENGTH_LONG).show()
                    }
                    errorMessage.contains("короткий") || errorMessage.contains("минимум") -> {
                        Toast.makeText(requireContext(), "$errorMessage", Toast.LENGTH_SHORT).show()
                    }
                    errorMessage.contains("пустые") || errorMessage.contains("заполните") -> {
                        Toast.makeText(requireContext(), "$errorMessage", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "$errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            val login = etLogin.text.toString().trim()
            val pass  = etPassword.text.toString().trim()

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "✏Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (login.length < 3) {
                Toast.makeText(requireContext(), "Логин должен содержать минимум 3 символа", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass.length < 3) {
                Toast.makeText(requireContext(), "Пароль должен содержать минимум 3 символа", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidLogin(login)) {
                Toast.makeText(requireContext(), "Логин может содержать только буквы, цифры и символы _ -", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            setLoadingState()
            Toast.makeText(requireContext(), "Проверяем доступность логина...", Toast.LENGTH_SHORT).show()

            vm.register(login, pass)
        }
    }

    private fun isValidLogin(login: String): Boolean {
        return login.matches(Regex("^[a-zA-Z0-9_-]+$"))
    }

    private fun setLoadingState() {
        btnRegister.isEnabled = false
        btnRegister.text = "Регистрация..."
    }

    private fun resetButton() {
        btnRegister.isEnabled = true
        btnRegister.text = "Зарегистрироваться"
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::btnRegister.isInitialized) {
            resetButton()
        }
    }
}
