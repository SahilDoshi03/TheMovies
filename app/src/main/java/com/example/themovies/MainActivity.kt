package com.example.themovies

import android.app.Dialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.themovies.profile.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var dialog: Dialog
    private lateinit var user: User
    private lateinit var uid: String

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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()
        val sIA: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        if(uid.isNotEmpty()){
            getUserData()
        }

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
                R.id.nav_logout -> logout()
            }

            true
        }

        when {
            email!=null -> {
                username.text = email
            }
            phno!=null -> {
                username.text = phno
            }
            sIA!=null -> {
                username.text = sIA.displayName
                tv_name_main.text = getString(R.string.name_main,sIA.displayName)
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

    private fun logout(){
        Firebase.auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    private fun getUserData(){
        showProgressBar()
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
                tv_name_main.text = getString(R.string.name_main,user.Name)
                tv_dob_main.text = getString(R.string.dob_main,user.DOB)
                getUserProfile()
            }

            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
                Toast.makeText(this@MainActivity,"Failed to get User Profile",Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showProgressBar(){
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.progress_bar)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar(){
        dialog.dismiss()
    }

    private fun getUserProfile(){
        storageReference = FirebaseStorage.getInstance().reference.child("Users/$uid")
        val localFile = File.createTempFile("tempImage","jpg")
        storageReference.getFile(localFile).addOnCompleteListener{
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            profile_photo_main.setImageBitmap(bitmap)
            hideProgressBar()
        }.addOnFailureListener{
            hideProgressBar()
            Toast.makeText(this,"Failed to retrieve data, trying again",Toast.LENGTH_SHORT).show()
            getUserProfile()
        }
    }
}