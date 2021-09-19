package com.example.themovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout? = findViewById(R.id.drawerLayout)
        val navView: NavigationView? = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navView?.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.nav_latest -> Toast.makeText(this,"Clicked Latest", Toast.LENGTH_SHORT).show()
                R.id.nav_toprated -> Toast.makeText(this,"Clicked Top Rated", Toast.LENGTH_SHORT).show()
                R.id.nav_upcoming -> Toast.makeText(this,"Clicked Upcoming", Toast.LENGTH_SHORT).show()
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}