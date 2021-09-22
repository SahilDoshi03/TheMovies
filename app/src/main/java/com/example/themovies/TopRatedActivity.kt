package com.example.themovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovies.models.Movies
import com.example.themovies.models.MoviesResponse
import com.example.themovies.services.TopRatedMoviesApiInterface
import com.example.themovies.services.MoviesApiService
import kotlinx.android.synthetic.main.activity_top_rated.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopRatedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_rated)

        rv_topRated.layoutManager = LinearLayoutManager(this)
        rv_topRated.setHasFixedSize(true)
        getMovieData { movies: List<Movies> ->
            rv_topRated.adapter = MoviesAdapter(movies)
        }
    }
    private  fun getMovieData(callback: (List<Movies>)->Unit){
        val apiService = MoviesApiService.getInstance().create(TopRatedMoviesApiInterface::class.java)
        apiService.getTRMovieList().enqueue(object : Callback<MoviesResponse> {

            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {

            }
        })
    }
}