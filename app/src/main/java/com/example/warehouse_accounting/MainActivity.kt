package com.example.warehouse_accounting

import android.os.Bundle
import android.view.View
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
import com.example.warehouse_accounting.ServerController.WebSocketConnection

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var webSocketConnection: WebSocketConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = android.graphics.Color.parseColor("#222222")

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

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
            val hideUI = destination.id == R.id.welcomeFragment ||
                    destination.id == R.id.loginFragment ||
                    destination.id == R.id.registerFragment

            if (hideUI) {
                binding.appBarMain.toolbar.visibility = View.GONE
                binding.navView.visibility = View.GONE
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                binding.appBarMain.toolbar.visibility = View.VISIBLE
                binding.navView.visibility = View.VISIBLE
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }

        val wsUrl = "ws://192.168.8.104:5400"
        webSocketConnection = WebSocketConnection(wsUrl)
        webSocketConnection?.connect()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketConnection?.close()
    }
}
