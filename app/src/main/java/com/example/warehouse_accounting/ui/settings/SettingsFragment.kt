package com.example.warehouse_accounting.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.warehouse_accounting.R

class SettingsFragment : Fragment() {

    private lateinit var switchPushNotifications: SwitchCompat
    private lateinit var textViewVersion: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        switchPushNotifications = root.findViewById(R.id.switchPushNotifications)
        textViewVersion = root.findViewById(R.id.textViewVersion)

        val prefs = requireContext().getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

        switchPushNotifications.isChecked = prefs.getBoolean("push_notifications", true)
        switchPushNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("push_notifications", isChecked).apply()
        }

        textViewVersion.text = "Версия: 1.0.0"

        return root
    }
}
