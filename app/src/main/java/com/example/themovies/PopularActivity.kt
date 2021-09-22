package com.example.themovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovies.models.Movies
import com.example.themovies.models.MoviesResponse
import com.example.themovies.services.PopularMoviesApiInterface
import com.example.themovies.services.MoviesApiService
import kotlinx.android.synthetic.main.activity_popular.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popular)

        rv_latest.layoutManager = LinearLayoutManager(this)
        rv_latest.setHasFixedSize(true)
        getMovieData { movies: List<Movies> ->
            rv_latest.adapter = MoviesAdapter(movies)
        }

    }
    private  fun getMovieData(callback: (List<Movies>)->Unit){
        val apiService = MoviesApiService.getInstance().create(PopularMoviesApiInterface::class.java)
        apiService.getLMovieList().enqueue(object : Callback<MoviesResponse> {

            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {

            }
        })
    }

}

