package com.example.warehouse_accounting.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.ui.profile.ProfileViewModel

class SettingsFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextLogin: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        editTextFirstName = root.findViewById(R.id.editTextFirstName)
        editTextLastName = root.findViewById(R.id.editTextLastName)
        editTextLogin = root.findViewById(R.id.editTextLogin)

        viewModel.profile.observe(viewLifecycleOwner, Observer { settings ->
            if (editTextFirstName.text.toString() != settings.firstName) {
                editTextFirstName.setText(settings.firstName)
            }
            if (editTextLastName.text.toString() != settings.lastName) {
                editTextLastName.setText(settings.lastName)
            }
            if (editTextLogin.text.toString() != settings.login) {
                editTextLogin.setText(settings.login)
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

        return root
    }
}
