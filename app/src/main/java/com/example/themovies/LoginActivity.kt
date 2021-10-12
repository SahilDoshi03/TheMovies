package com.example.themovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    companion object{
        const val RC_SIGN_IN = 9001
        const val TAG = "LoginActivity"
    }

    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = getString(R.string.login)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        auth = Firebase.auth

        phnobtn.setOnClickListener {
            startActivity(Intent(this@LoginActivity,PhNoLogin::class.java))
            finish()
        }

        tv_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
        loginbtn.setOnClickListener {
            when {
                TextUtils.isEmpty(et_login_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please Enter Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_login_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please Enter Password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    val email: String = et_login_email.text.toString().trim { it <= ' ' }
                    val password: String = et_login_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                task.result!!.user!!
                                Toast.makeText(
                                    this@LoginActivity,
                                    "You were logged in successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("email", email)
                                intent.putExtra("password", password)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
        googleSignInBtn.setOnClickListener{
            signIn()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "onActivityResult: firebaseAuthWithGoogle")
                doAuthentication(account.idToken!!)

            }catch (e: ApiException){
                Toast.makeText(this,
                    e.message,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private  fun doAuthentication(idToken: String?){
        val credentials = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){
                    Toast.makeText(
                        this@LoginActivity,
                        "You were logged in successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this,ProfileActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
}