package com.example.themovies

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.themovies.profile.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        button_profile.setOnClickListener {
            when {
                TextUtils.isEmpty(et_profile_name.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Please Enter Name.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_profile_dob.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Please Enter Date of Birth.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val name: String = et_profile_name.text.toString().trim { it <= ' ' }
                    val dob: String = et_profile_dob.text.toString().trim { it <= ' ' }
                    val user = User(name, dob)
                    if (uid != null) {
                        databaseReference.child(uid).setValue(user).addOnCompleteListener {
                            if (it.isSuccessful) {
                                uploadProfilePic()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun uploadProfilePic() {
        imageUri = Uri.parse("android.resource://$packageName/${R.drawable.photo}")
        storageReference = FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(this,"Profile successfully updated",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(this,"Failed to upload the image",Toast.LENGTH_SHORT).show()
        }
    }

}