package com.example.warehouse_accounting.ui.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.warehouse_accounting.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(requireActivity().application, ServiceLocator.nyaService)
    }

    private val validationHandler = Handler(Looper.getMainLooper())
    private var validationRunnable: Runnable? = null
    private var isUpdating = false

    companion object {
        private const val VALIDATION_DELAY = 500L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupTextWatchers()
        setupButtons()

        viewModel.requestProfileFromServer()
    }

    private fun setupObservers() {
        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            if (!isUpdating) {
                updateUI(profile)
            }
        }

        viewModel.validationErrors.observe(viewLifecycleOwner) { errors ->
            showValidationErrors(errors)
        }

        viewModel.messages.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }
    }

    private fun updateUI(profile: com.example.warehouse_accounting.models.Profile) {
        isUpdating = true

        val firstNameSelection = binding.etFirstName.selectionStart
        val lastNameSelection = binding.etLastName.selectionStart
        val loginSelection = binding.etLogin.selectionStart
        val phoneSelection = binding.etPhone.selectionStart
        val emailSelection = binding.etEmail.selectionStart

        if (binding.etFirstName.text?.toString() != (profile.firstName ?: "")) {
            binding.etFirstName.setText(profile.firstName ?: "")
        } else {
            val textLength = binding.etFirstName.text?.length ?: 0
            binding.etFirstName.setSelection(minOf(firstNameSelection, textLength))
        }

        if (binding.etLastName.text?.toString() != (profile.lastName ?: "")) {
            binding.etLastName.setText(profile.lastName ?: "")
        } else {
            val textLength = binding.etLastName.text?.length ?: 0
            binding.etLastName.setSelection(minOf(lastNameSelection, textLength))
        }

        if (binding.etLogin.text?.toString() != (profile.login ?: "")) {
            binding.etLogin.setText(profile.login ?: "")
        } else {
            val textLength = binding.etLogin.text?.length ?: 0
            binding.etLogin.setSelection(minOf(loginSelection, textLength))
        }

        if (binding.etPhone.text?.toString() != (profile.phone ?: "")) {
            binding.etPhone.setText(profile.phone ?: "")
        } else {
            val textLength = binding.etPhone.text?.length ?: 0
            binding.etPhone.setSelection(minOf(phoneSelection, textLength))
        }

        if (binding.etEmail.text?.toString() != (profile.email ?: "")) {
            binding.etEmail.setText(profile.email ?: "")
        } else {
            val textLength = binding.etEmail.text?.length ?: 0
            binding.etEmail.setSelection(minOf(emailSelection, textLength))
        }

        isUpdating = false
    }

    private fun setupTextWatchers() {
        binding.etFirstName.addTextChangedListener(createDebouncedWatcher { text ->
            viewModel.updateFirstName(text)
        })

        binding.etLastName.addTextChangedListener(createDebouncedWatcher { text ->
            viewModel.updateLastName(text)
        })

        binding.etLogin.addTextChangedListener(createDebouncedWatcher { text ->
            viewModel.updateLogin(text)
        })

        binding.etPhone.addTextChangedListener(createDebouncedWatcher { text ->
            viewModel.updatePhone(text)
        })

        binding.etEmail.addTextChangedListener(createDebouncedWatcher { text ->
            viewModel.updateEmail(text)
        })
    }

    private fun createDebouncedWatcher(action: (String) -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                validationRunnable?.let { validationHandler.removeCallbacks(it) }

                validationRunnable = Runnable {
                    action(s?.toString() ?: "")
                }
                validationHandler.postDelayed(validationRunnable!!, VALIDATION_DELAY)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun setupButtons() {
        binding.btnSaveProfile.setOnClickListener {
            binding.root.clearFocus()
            viewModel.saveProfile()
        }
    }

    private fun showValidationErrors(errors: Map<String, String>) {
        clearAllErrors()

        errors.forEach { (field, error) ->
            when (field) {
                "firstName" -> binding.tilFirstName.error = error
                "lastName" -> binding.tilLastName.error = error
                "login" -> binding.tilLogin.error = error
                "phone" -> binding.tilPhone.error = error
                "email" -> binding.tilEmail.error = error
            }
        }
    }

    private fun clearAllErrors() {
        binding.tilFirstName.error = null
        binding.tilLastName.error = null
        binding.tilLogin.error = null
        binding.tilPhone.error = null
        binding.tilEmail.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        validationRunnable?.let { validationHandler.removeCallbacks(it) }
        _binding = null
    }
}
