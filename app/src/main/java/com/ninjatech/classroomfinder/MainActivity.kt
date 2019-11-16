package com.ninjatech.classroomfinder

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup bottom nav bar
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav)

        val bottomNavController = findNavController(R.id.app_nav_host_fragment)

        bottomNav.setupWithNavController(bottomNavController)

        // Setup toolbar
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    /**
     * Add top toolbar.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

        return true;
    }
}
