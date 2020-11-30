package com.udacity.shoestore.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.udacity.shoestore.R
import com.udacity.shoestore.UserPreferences
import com.udacity.shoestore.databinding.ActivityMainBinding
import com.udacity.shoestore.ui.fragments.RegisterFragmentDirections
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        //setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.nav_host_fragment)
        Timber.plant(Timber.DebugTree())
        val userLoggedIn = runBlocking { UserPreferences(this@MainActivity).isUserLoggedIn() }
        if (userLoggedIn){
            navController.navigate(RegisterFragmentDirections.actionRegisterDestinationToWelcomeFragment())
        }
    }


    /*override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController)
    }*/


}
