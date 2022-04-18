package com.smith.furniturestore.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.smith.furniturestore.R
import com.smith.furniturestore.databinding.ActivityAuthBinding


class AuthActivity : AppCompatActivity() {
    companion object {
        const val TOKEN_EXPIRY = "tokenExpiry"
        const val TOKEN_EXPIRY_STATUS = "tokenExpiryStatus"
    }

    private lateinit var binding: ActivityAuthBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve NavController from the host
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.auth_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


        //Set up action bar for use with NavController
        setupActionBarWithNavController(this, navController)


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}