package com.example.themovies

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.themovies.models.Trailer
import com.example.themovies.models.TrailerResponse
import com.example.themovies.services.MoviesApiService
import com.example.themovies.services.TrailerResponseApiInterface
import kotlinx.android.synthetic.main.activity_info.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        rv_info.layoutManager = LinearLayoutManager(this)
        rv_info.setHasFixedSize(true)
        getTrailerData { trailers: List<Trailer> ->
            rv_info.adapter = TrailerAdapter(trailers)
        }

        val str = "jRn48HxssPI"
        val thumbnail = intent.extras?.getString("poster_path")
        val url = intent.extras?.getString("url")

        info_movieTitle.text = intent.extras?.getString("ogtitle")
        expand_text_view.text = intent.extras?.getString("overview")
        info_releaseDate.text = intent.extras?.getString("release_date")
        info_averageVotes.text = intent.extras?.getString("voteAverage")
        Glide.with(this)
            .load(url+thumbnail)
            .into(info_movie_poster)


        info_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$str")))
        }

        var fav = false
        fav_button.setOnClickListener{
            if (!fav){
                fav_button.setImageResource(R.drawable.ic_favorite)
                fav = true
            }
            else{
                fav_button.setImageResource(R.drawable.ic_not_favourite)
                fav = false
            }
        }

    }
    private  fun getTrailerData(callback: (List<Trailer>)->Unit){
        val movieID = intent.extras?.getInt("movieId")
        val apiService = MoviesApiService.getInstance().create(TrailerResponseApiInterface::class.java)
        apiService.getTrailerList(movieID).enqueue(object : Callback<TrailerResponse> {

            override fun onResponse(call: Call<TrailerResponse>, response: Response<TrailerResponse>) {
                return callback(response.body()!!.trailers)
            }

            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {

            }
        })
    }

}