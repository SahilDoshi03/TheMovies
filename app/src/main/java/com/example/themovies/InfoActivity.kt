package com.example.themovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.themovies.models.Movies
import com.example.themovies.models.Trailer
import com.example.themovies.models.TrailerResponse
import com.example.themovies.services.MoviesApiService
import com.example.themovies.services.TrailerResponseApiInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val detail: Movies = intent.getParcelableExtra("obj")!!
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val movieID = intent.extras?.getInt("movieId").toString()

        val db = Firebase.firestore

        fav_button.setOnClickListener{

            val docRef = db.collection("users").document(currentUser?.uid!!)

            docRef.get().addOnSuccessListener {

                if (it != null && it.exists()) {

                    //  val a = it.toObject(ListDetail().javaClass)!!.listOfDetail
                    val a = it.get("favourite") as ArrayList <Map<String,*>>?

                    var contains = false

                    if (a != null) {
                        for (i in a) {
                            val id = i.getValue("movieId").toString()
                            Log.d("imp",id)
                            if ( id == movieID){
                                contains = true
                                break
                            }
                        }
                    }


                    Log.d("contains", contains.toString())
                    if (contains) {
                        docRef.update("favourite", FieldValue.arrayRemove(detail))
                        fav_button.setImageResource(R.drawable.ic_not_favourite)
                        Log.d("removed", "remove")

                    } else {
                        docRef.update("favourite", FieldValue.arrayUnion(detail))
                        fav_button.setImageResource(R.drawable.ic_favourite)
                        Log.d("added", "added")
                    }

                } else {
                    Log.d("snapshot", " is null")
                    Toast.makeText(this, "complete profile to use this feature", Toast.LENGTH_SHORT).show()

                }


            }
        }

        if (currentUser != null){
            val docRef = db.collection("users").document(currentUser.uid)

            docRef.get().addOnSuccessListener {

                if (it != null) {

                    val a = it.get("favourite") as ArrayList <Map<String,*>>?


                    var contains = false

                    if (a != null) {
                        for (i in a) {
                            val id = i.getValue("movieId").toString()
                            Log.d("imp",id)
                            if ( id == movieID){
                                contains = true
                                break
                            }

                        }
                    }

                    if (contains) {
                        fav_button.setImageResource(R.drawable.ic_favourite)
                    } else {
                        fav_button.setImageResource(R.drawable.ic_not_favourite)
                    }

                } else {
                    Log.d("snapshot", " is null")
                    return@addOnSuccessListener
                }


            }
        }

        rv_info.layoutManager = LinearLayoutManager(this)
        rv_info.setHasFixedSize(true)
        getTrailerData { trailers: List<Trailer> ->
            rv_info.adapter = TrailerAdapter(trailers)
        }

        val thumbnail = intent.extras?.getString("poster_path")
        val url = intent.extras?.getString("url")

        info_movieTitle.text = intent.extras?.getString("ogtitle")
        expand_text_view.text = intent.extras?.getString("overview")
        info_releaseDate.text = intent.extras?.getString("release_date")
        info_averageVotes.text = intent.extras?.getString("voteAverage")
        Glide.with(this)
            .load(url+thumbnail)
            .into(info_movie_poster)

    }
    private  fun getTrailerData(callback: (List<Trailer>)->Unit){
        val movieID = intent.extras?.getInt("movieId").toString()
        val apiService = MoviesApiService.getInstance().create(TrailerResponseApiInterface::class.java)
        apiService.getTrailerList(movieID.toInt()).enqueue(object : Callback<TrailerResponse> {

            override fun onResponse(call: Call<TrailerResponse>, response: Response<TrailerResponse>) {
                return callback(response.body()!!.trailers)
            }

            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {

            }
        })
    }

}