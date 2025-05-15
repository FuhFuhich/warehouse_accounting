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
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
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
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        imageViewPhoto = root.findViewById(R.id.imageViewPhoto)
        editTextFirstName = root.findViewById(R.id.editTextFirstName)
        editTextLastName = root.findViewById(R.id.editTextLastName)
        editTextLogin = root.findViewById(R.id.editTextLogin)
        editTextPhone = root.findViewById(R.id.editTextPhone)
        editTextEmail = root.findViewById(R.id.editTextEmail)
        buttonChangePhoto = root.findViewById(R.id.buttonChangePhoto)
        buttonSave = root.findViewById(R.id.buttonSave)

        viewModel.profile.observe(viewLifecycleOwner, Observer { profile ->
            if (editTextFirstName.text.toString() != profile.firstName) {
                editTextFirstName.setText(profile.firstName)
            }
            if (editTextLastName.text.toString() != profile.lastName) {
                editTextLastName.setText(profile.lastName)
            }
            if (editTextLogin.text.toString() != profile.login) {
                editTextLogin.setText(profile.login)
            }
            if (editTextPhone.text.toString() != profile.phone) {
                editTextPhone.setText(profile.phone)
            }
            if (editTextEmail.text.toString() != profile.email) {
                editTextEmail.setText(profile.email)
            }
            if (profile.photoUri != null) {
                imageViewPhoto.setImageURI(profile.photoUri)
            } else {
                imageViewPhoto.setImageResource(android.R.drawable.sym_def_app_icon)
            }
        })

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

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            viewModel.updatePhoto(selectedImageUri)
        }
    }
}
