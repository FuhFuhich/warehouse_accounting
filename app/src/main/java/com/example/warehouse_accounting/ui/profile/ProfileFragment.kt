package com.example.warehouse_accounting.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.warehouse_accounting.R

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var imageViewPhoto: ImageView
    private lateinit var textViewFirstName: TextView
    private lateinit var textViewLastName: TextView
    private lateinit var textViewLogin: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        imageViewPhoto = root.findViewById(R.id.imageViewPhoto)
        textViewFirstName = root.findViewById(R.id.textViewFirstName)
        textViewLastName = root.findViewById(R.id.textViewLastName)
        textViewLogin = root.findViewById(R.id.textViewLogin)

        viewModel.profile.observe(viewLifecycleOwner, Observer { profile ->
            textViewFirstName.text = profile.firstName
            textViewLastName.text = profile.lastName
            textViewLogin.text = profile.login
            if (profile.photoUri != null) {
                imageViewPhoto.setImageURI(profile.photoUri)
            } else {
                imageViewPhoto.setImageResource(android.R.drawable.sym_def_app_icon)
            }
        })

        return root
    }
}
