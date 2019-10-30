package com.ninjatech.classroomfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)

        val bottomNavController = findNavController(R.id.app_nav_host_fragment)

        val appBarConfig =
            AppBarConfiguration(setOf(R.id.profileFragment, R.id.mapFragment, R.id.searchFragment))

        setupActionBarWithNavController(bottomNavController, appBarConfig)
        bottomNav.setupWithNavController(bottomNavController)

    }

}
