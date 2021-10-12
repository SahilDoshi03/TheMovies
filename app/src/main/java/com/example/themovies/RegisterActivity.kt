package com.example.themovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_login.setOnClickListener{
            startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))
            finish()
        }

        registerbtn.setOnClickListener {
            when{
                TextUtils.isEmpty(et_register_email.text.toString().trim{it<=' '})->{
                    Toast.makeText(this@RegisterActivity,
                        "Please Enter Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_password.text.toString().trim{it<=' '})-> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please Enter Password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = et_register_email.text.toString().trim{it<=' '}
                    val password: String = et_register_password.text.toString().trim{it<=' '}

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                task.result!!.user!!
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "You were registered successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("email", email)

                                startActivity(Intent(this,ProfileActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }
}