package com.example.warehouse_accounting.ui.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.databinding.FragmentProfileBinding
import com.google.android.material.textfield.TextInputLayout

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(
            requireActivity().application,
            ServiceLocator.nyaService
        )
    }

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var loginEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var saveButton: Button

    private lateinit var firstNameLayout: TextInputLayout
    private lateinit var lastNameLayout: TextInputLayout
    private lateinit var loginLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout

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

        initializeViews()

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            updateUI(profile)
        }

        viewModel.validationErrors.observe(viewLifecycleOwner) { errors ->
            clearAllFieldErrors()

            errors.forEach { (field, error) ->
                showFieldError(field, error)
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }

        setupTextWatchers()

        setupSaveButton()

        viewModel.requestProfileFromServer()
    }

    private fun initializeViews() {
        firstNameEditText = binding.etFirstName
        lastNameEditText = binding.etLastName
        loginEditText = binding.etLogin
        phoneEditText = binding.etPhone
        emailEditText = binding.etEmail
        profileImageView = binding.ivProfilePicture
        saveButton = binding.btnSaveProfile

        firstNameLayout = binding.tilFirstName
        lastNameLayout = binding.tilLastName
        loginLayout = binding.tilLogin
        phoneLayout = binding.tilPhone
        emailLayout = binding.tilEmail
    }

    private fun updateUI(profile: com.example.warehouse_accounting.models.Profile) {
        firstNameEditText.setText(profile.firstName ?: "")
        lastNameEditText.setText(profile.lastName ?: "")
        loginEditText.setText(profile.login ?: "")
        phoneEditText.setText(profile.phone ?: "")
        emailEditText.setText(profile.email ?: "")

        loadProfileImage(profile.photoUri)
    }

    private fun loadProfileImage(photoUri: String?) {
        if (!photoUri.isNullOrEmpty()) {
            Glide.with(this)
                .load(photoUri)
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar)
                .circleCrop()
                .into(profileImageView)
        } else {
            profileImageView.setImageResource(R.drawable.ic_default_avatar)
        }
    }

    private fun setupTextWatchers() {
        firstNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateFirstName(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        lastNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateLastName(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loginEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateLogin(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        phoneEditText.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                isUpdating = true
                val cursorPosition = phoneEditText.selectionStart
                val originalLength = s?.length ?: 0

                viewModel.updatePhone(s.toString())

                viewModel.profile.value?.phone?.let { formattedPhone ->
                    if (formattedPhone != s.toString()) {
                        phoneEditText.setText(formattedPhone)
                        val newPosition = if (formattedPhone.length > originalLength) {
                            formattedPhone.length
                        } else {
                            cursorPosition.coerceAtMost(formattedPhone.length)
                        }
                        phoneEditText.setSelection(newPosition)
                    }
                }
                isUpdating = false
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateEmail(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            binding.root.clearFocus()

            viewModel.saveProfile()
        }
    }

    private fun showFieldError(field: String, error: String) {
        when (field) {
            "firstName" -> firstNameLayout.error = error
            "lastName" -> lastNameLayout.error = error
            "login" -> loginLayout.error = error
            "phone" -> phoneLayout.error = error
            "email" -> emailLayout.error = error
        }
    }

    private fun clearAllFieldErrors() {
        firstNameLayout.error = null
        lastNameLayout.error = null
        loginLayout.error = null
        phoneLayout.error = null
        emailLayout.error = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
