package com.example.warehouse_accounting

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse_accounting.databinding.ActivityMainBinding
import com.example.warehouse_accounting.ServerController.GlobalWebSocket
import com.example.warehouse_accounting.ServerController.Repositories.poka_tak
import com.example.warehouse_accounting.ServerController.Service.Serv
import com.example.warehouse_accounting.utils.NavigationHeaderHelper
import com.example.warehouse_accounting.models.Profile

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var nyaService: Serv

    private lateinit var profilePicture: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = android.graphics.Color.parseColor("#222222")

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        nyaService = Serv(poka_tak.getInstance())

        val headerView = navView.getHeaderView(0)
        profilePicture = headerView.findViewById(R.id.iv_profile_picture)
        profileName = headerView.findViewById(R.id.tv_profile_name)
        profileEmail = headerView.findViewById(R.id.tv_profile_email)

        nyaService.getProfileLiveData().observe(this) { profile ->
            NavigationHeaderHelper.updateNavigationHeader(
                profile, profilePicture, profileName, profileEmail
            )
        }

        println("=== MAIN: Стартовое назначение: ${navController.graph.startDestinationId} ===")

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_profile, R.id.nav_products, R.id.nav_suppliers,
                R.id.nav_buyers, R.id.nav_warehouses, R.id.nav_settings,
                R.id.nav_help, R.id.nav_need_help
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.post {
            (navView.getChildAt(0) as? RecyclerView)?.scrollToPosition(0)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            println("=== НАВИГАЦИЯ К: ${destination.label} (ID: ${destination.id}) ===")

            val hideUI = destination.id == R.id.welcomeFragment ||
                    destination.id == R.id.loginFragment ||
                    destination.id == R.id.registerFragment

            if (hideUI) {
                println("=== СКРЫВАЕМ UI (auth экраны) ===")
                binding.appBarMain.toolbar.visibility = View.GONE
                binding.navView.visibility = View.GONE
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                println("=== ПОКАЗЫВАЕМ UI (основные экраны) ===")
                binding.appBarMain.toolbar.visibility = View.VISIBLE
                binding.navView.visibility = View.VISIBLE
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }

        GlobalWebSocket.instance.connect()
    }

    fun updateNavigationHeader(profile: Profile?) {
        if (::profilePicture.isInitialized && ::profileName.isInitialized && ::profileEmail.isInitialized) {
            NavigationHeaderHelper.updateNavigationHeader(
                profile, profilePicture, profileName, profileEmail
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalWebSocket.instance.close()
    }
}
