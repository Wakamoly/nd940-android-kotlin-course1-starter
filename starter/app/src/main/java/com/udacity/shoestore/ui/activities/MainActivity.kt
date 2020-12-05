package com.udacity.shoestore.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import com.udacity.shoestore.R
import com.udacity.shoestore.UserPreferences
import com.udacity.shoestore.databinding.ActivityMainBinding
import com.udacity.shoestore.db.ShoeStoreDatabase
import com.udacity.shoestore.ui.fragments.RegisterFragmentDirections
import com.udacity.shoestore.ui.fragments.WelcomeFragmentDirections
import com.udacity.shoestore.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        drawerLayout = binding.drawerLayout

        val navHost: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        navController = navHost.navController

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        // prevent nav gesture and hide appBar if on starting destinations
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, args: Bundle? ->
            if (nd.id != R.id.register_destination && nd.id != R.id.welcome_destination && nd.id != R.id.instruction_destination) {
                unhideAppBar()
            } else {
                hideAppBar()
            }
        }

        Timber.plant(Timber.DebugTree())
        val userLoggedIn = runBlocking { UserPreferences(this@MainActivity).isUserLoggedIn() }
        if (!userLoggedIn){
            navController.graph = navController.graph.apply {
                startDestination = R.id.register_destination
            }
        }
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout->{
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    CoroutineScope(Dispatchers.IO).launch {
                        UserPreferences(this@MainActivity).clear()
                        ShoeStoreDatabase(this@MainActivity).clearAllTables()
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    finish()
                    startActivity(intent)
                    true
                }
                R.id.listFragment->{
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    this.findNavController(R.id.nav_host_fragment).navigate(R.id.shoelist_destination)
                    true
                }
                else -> false
            }
        }
    }

    private fun hideAppBar(){
        toolbar.visible(false)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
    private fun unhideAppBar(){
        toolbar.visible(true)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


}
