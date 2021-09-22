package com.example.themovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovies.models.LatestMovies
import com.example.themovies.models.LatestMoviesResponse
import com.example.themovies.services.MovieApiInterface
import com.example.themovies.services.MoviesApiService
import kotlinx.android.synthetic.main.activity_latest.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LatestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest)

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        getMovieData { movies: List<LatestMovies> ->
            recycler_view.adapter = MovieAdapter(movies)
        }

    }
    private  fun getMovieData(callback: (List<LatestMovies>)->Unit){
        val apiService = MoviesApiService.getInstance().create(MovieApiInterface::class.java)
        apiService.getMovieList().enqueue(object : Callback<LatestMoviesResponse> {

            override fun onResponse(call: Call<LatestMoviesResponse>, response: Response<LatestMoviesResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<LatestMoviesResponse>, t: Throwable) {

            }
        })
    }

}

