package com.example.themovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_Layout)
        val navView: NavigationView? = findViewById(R.id.nav_view)
        val headerView: View = navView!!.getHeaderView(0)
        val username = headerView.findViewById<TextView>(R.id.user_name)
        val userImage = headerView.findViewById<CircleImageView>(R.id.photoOP)
        val email = intent?.getStringExtra("email")
        val phno = Firebase.auth.currentUser?.phoneNumber
        val userImageUrl = Firebase.auth.currentUser?.photoUrl
        val gmail =  Firebase.auth.currentUser?.email
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
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
            drawerLayout?.closeDrawer(GravityCompat.START)
            when(it.itemId){

                R.id.nav_popular ->startActivity(Intent(this,PopularActivity::class.java))
                R.id.nav_toprated ->startActivity(Intent(this,TopRatedActivity::class.java))
                R.id.nav_upcoming -> startActivity(Intent(this,UpcomingActivity::class.java))
                R.id.nav_favourites -> startActivity(Intent(this,FavouritesActivity::class.java))
                R.id.nav_profile -> startActivity(Intent(this,ProfileActivity::class.java))
                R.id.nav_signIn -> signIn()
                R.id.nav_logout -> logout()
            }

            true
        }

        when {
            email!=null -> {
                username.text = email
                tv_email_main.text = getString(R.string.email_main,email)
            }
            phno!=null -> {
                username.text = phno
            }
            sIA!=null -> {
                username.text = sIA.displayName
                tv_name_main.text = getString(R.string.name_main,sIA.displayName)
                tv_email_main.text = getString(R.string.email_main,gmail)
                Glide.with(applicationContext)
                    .load(userImageUrl)
                    .into(userImage)
                Glide.with(applicationContext)
                    .load(userImageUrl)
                    .into(profile_photo_main)
            }
            else -> {
                username.text = getString(R.string.guest)
            }
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    fun logout(){
        Firebase.auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
    private fun signIn() {
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }
}