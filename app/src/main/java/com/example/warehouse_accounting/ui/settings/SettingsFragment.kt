package com.example.warehouse_accounting.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.warehouse_accounting.R

class SettingsFragment : Fragment() {

    private lateinit var switchPushNotifications: SwitchCompat
    private lateinit var switchTheme: SwitchCompat
    private lateinit var textViewVersion: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        switchPushNotifications = root.findViewById(R.id.switchPushNotifications)
        switchTheme = root.findViewById(R.id.switchTheme)
        textViewVersion = root.findViewById(R.id.textViewVersion)

        val prefs = requireContext().getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)

        switchPushNotifications.isChecked = prefs.getBoolean("push_notifications", true)
        switchTheme.isChecked = prefs.getBoolean("dark_theme", false)

        switchPushNotifications.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("push_notifications", isChecked).apply()
        }
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_theme", isChecked).apply()
            // смена темы
        }

        textViewVersion.text = "Версия: 1.0.0"

        return root
    }
}
