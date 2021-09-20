package com.example.themovies

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.nav_header.*

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var googleSignInClient: GoogleSignInClient

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout? = findViewById(R.id.drawerLayout)
        val navView: NavigationView? = findViewById(R.id.nav_view)
        val headerView: View = navView!!.getHeaderView(0)
        val username = headerView.findViewById<TextView>(R.id.user_name)
        val email = intent?.getStringExtra("email")
        val phno = Firebase.auth.currentUser?.phoneNumber
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()
        val sIA: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navView.setNavigationItemSelectedListener {




            when(it.itemId){

                R.id.nav_latest -> Toast.makeText(this,"Clicked Latest", Toast.LENGTH_SHORT).show()
                R.id.nav_toprated -> Toast.makeText(this,"Clicked Top Rated", Toast.LENGTH_SHORT).show()
                R.id.nav_upcoming -> Toast.makeText(this,"Clicked Upcoming", Toast.LENGTH_SHORT).show()
                R.id.nav_signIn -> login()
                R.id.nav_logout -> logout()
            }
            true
        }

        when {
            email!=null -> {
                username.text = "$email"
            }
            phno!=null -> {
                username.text = "$phno"
            }
            sIA!=null -> {
                username.setText(sIA.displayName)
            }
            else -> {
                username.text = "Guest"
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    fun logout(){
        Firebase.auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            user_name.text = "Guest"
        }
    }

    private fun login() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

}