package com.example.tobuy.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.tobuy.R
import com.example.tobuy.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    val navController by lazy { navController() }

    private val topLevelDestinationIds by lazy { setOf(R.id.homeFragment, R.id.profileFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNavigation()
        setDestinationListenerForNavController()
    }

    private fun setupBottomNavigation() {
        val bottomNavigation = binding.bottomNavigation
        setupWithNavController(bottomNavigation, navController)
    }


    private fun setDestinationListenerForNavController() {

        val bottomNavigation = binding.bottomNavigation

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigation.isGone = topLevelDestinationIds.contains(destination.id).not()
        }

    }

    private fun navController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        return navHostFragment.navController
    }

}
