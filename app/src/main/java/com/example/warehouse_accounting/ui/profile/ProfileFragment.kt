package com.example.warehouse_accounting.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        ProfileViewModelFactory(requireActivity().application, ServiceLocator.nyaService)
    }

    private val validationHandler = Handler(Looper.getMainLooper())
    private var validationRunnable: Runnable? = null
    private var isUpdating = false

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val VALIDATION_DELAY = 500L // 500ms задержка
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

        binding.etFirstName.setText(profile.firstName ?: "")
        binding.etLastName.setText(profile.lastName ?: "")
        binding.etLogin.setText(profile.login ?: "")
        binding.etPhone.setText(profile.phone ?: "")
        binding.etEmail.setText(profile.email ?: "")

        loadProfileImage(profile.photoUri)

        isUpdating = false
    }

    private fun loadProfileImage(photoUri: String?) {
        Glide.with(this)
            .load(photoUri)
            .placeholder(R.drawable.ic_default_avatar)
            .error(R.drawable.ic_default_avatar)
            .circleCrop()
            .into(binding.ivProfilePicture)
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
                    action(s.toString())
                }
                validationHandler.postDelayed(validationRunnable!!, VALIDATION_DELAY)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun setupButtons() {
        binding.btnChangePhoto.setOnClickListener {
            openImagePicker()
        }

        binding.btnSaveProfile.setOnClickListener {
            binding.root.clearFocus()
            viewModel.saveProfile()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                handleImageSelection(uri)
            }
        }
    }

    private fun handleImageSelection(uri: Uri) {
        try {
            val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            requireContext().contentResolver.takePersistableUriPermission(uri, takeFlags)
            viewModel.updatePhoto(uri.toString())

            Glide.with(this)
                .load(uri)
                .circleCrop()
                .into(binding.ivProfilePicture)

        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Ошибка доступа к изображению", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Очищаем обработчики
        validationRunnable?.let { validationHandler.removeCallbacks(it) }
        _binding = null
    }
}
