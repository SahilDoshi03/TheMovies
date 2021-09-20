package com.example.themovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val bl = findViewById<Button>(R.id.btn_login)

        bl.setOnClickListener{
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
        }


        registerbtn.setOnClickListener {
            when{
                TextUtils.isEmpty(et_register_email.text.toString().trim{it<=' '})->{
                    Toast.makeText(this@RegisterActivity,
                        "Please Enter Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_password.text.toString().trim{it<=' '})->{
                    Toast.makeText(this@RegisterActivity,
                        "Please Enter Password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = et_register_email.text.toString().trim{it<=' '}
                    val password: String = et_register_password.text.toString().trim{it<=' '}

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "You were registered successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("email", email)
                                    intent.putExtra("password", password)
                                    startActivity(intent)
                                    finish()
                                }else{
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }
            }
        }
    }
}