package com.example.warehouse_accounting.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.warehouse_accounting.R

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(requireActivity().application, ServiceLocator.nyaService)
    }

    private lateinit var imageViewPhoto: ImageView
    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextLogin: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var buttonChangePhoto: Button
    private lateinit var buttonSave: Button

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageViewPhoto = view.findViewById(R.id.imageViewPhoto)
        editTextFirstName = view.findViewById(R.id.editTextFirstName)
        editTextLastName = view.findViewById(R.id.editTextLastName)
        editTextLogin = view.findViewById(R.id.editTextLogin)
        editTextPhone = view.findViewById(R.id.editTextPhone)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        buttonChangePhoto = view.findViewById(R.id.buttonChangePhoto)
        buttonSave = view.findViewById(R.id.buttonSave)

        viewModel.profile.observe(viewLifecycleOwner, Observer { profile ->
            profile?.let {
                if (editTextFirstName.text.toString() != it.firstName) {
                    editTextFirstName.setText(it.firstName ?: "")
                }
                if (editTextLastName.text.toString() != it.lastName) {
                    editTextLastName.setText(it.lastName ?: "")
                }
                if (editTextLogin.text.toString() != it.login) {
                    editTextLogin.setText(it.login)
                }
                if (editTextPhone.text.toString() != it.phone) {
                    editTextPhone.setText(it.phone ?: "")
                }
                if (editTextEmail.text.toString() != it.email) {
                    editTextEmail.setText(it.email ?: "")
                }
                if (it.photoUri != null) {
                    imageViewPhoto.setImageURI(Uri.parse(it.photoUri))
                } else {
                    imageViewPhoto.setImageResource(android.R.drawable.sym_def_app_icon)
                }
            }
        })

        viewModel.requestProfileFromServer()

        editTextFirstName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateFirstName(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        editTextLastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateLastName(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        editTextLogin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateLogin(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        editTextPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updatePhone(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateEmail(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        buttonChangePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        buttonSave.setOnClickListener {
            viewModel.saveProfile()
            Toast.makeText(requireContext(), "Профиль сохранён!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data

            val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            try {
                selectedImageUri?.let { uri ->
                    requireContext().contentResolver.takePersistableUriPermission(uri, takeFlags)
                }
            } catch (e: SecurityException) {
                // Тут если что будет обработка ошибки, если не в падлу будет делать
            }

            viewModel.updatePhoto(selectedImageUri?.toString())
        }
    }
}
