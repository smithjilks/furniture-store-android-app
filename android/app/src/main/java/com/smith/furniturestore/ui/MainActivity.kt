package com.smith.furniturestore.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.databinding.ActivityMainBinding
import com.smith.furniturestore.databinding.FragmentCartBinding
import com.smith.furniturestore.ui.auth.LoginFragment
import com.smith.furniturestore.ui.main.MainActivityViewModel
import com.smith.furniturestore.ui.main.MainActivityViewModelFactory
import com.smith.furniturestore.viewmodel.CartFragmentViewModel
import com.smith.furniturestore.viewmodel.CartFragmentViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    private val viewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory(
            (application as App).furnitureRepository,
            application
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve NavController from the host
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupWithNavController(binding.bottomNavigation, navController)


        //Set up action bar for use with NavController
        NavigationUI.setupActionBarWithNavController(this, navController)

        // Observing work status
        viewModel.outputWorkInfos.observe(this, workInfosObserver())



    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        return Observer { listOfWorkInfo ->

            // If there are no matching work infos, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@Observer
            }

            val workInfo = listOfWorkInfo[0]
            Log.d("Work output data", workInfo.toString())

            if (workInfo.outputData.getString(AuthActivity.TOKEN_EXPIRY_STATUS) == "invalid") {
                viewModel.logoutUser()
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            } else {
                // Do nothing
            }
        }
    }

}