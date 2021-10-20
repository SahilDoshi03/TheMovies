package com.example.themovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.activity_register.*

class FavouritesActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    lateinit var myAdapter: FavouritesAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var listOfMap : ArrayList <Map<String,*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        linearLayoutManager = LinearLayoutManager(applicationContext)
        rv_favourites.layoutManager = linearLayoutManager

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

            val docRef = db.collection("users").document(currentUser!!.uid)

            listOfMap = arrayListOf()
            docRef.get().addOnSuccessListener {

                if (it.exists() && it != null){

                    listOfMap = it.get("favourite") as ArrayList <Map<String,*>>

                    Toast.makeText(this,"snapshot is not null",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"failure to add data",Toast.LENGTH_SHORT).show()
                }
                if (listOfMap.isEmpty()){

                    Toast.makeText(this,"map is empty",Toast.LENGTH_SHORT).show()
                    rv_favourites.isVisible = false
                }else{
                    myAdapter = let { FavouritesAdapter(it, listOfMap) }
                    rv_favourites.adapter = myAdapter

                }

            }

    }

}
