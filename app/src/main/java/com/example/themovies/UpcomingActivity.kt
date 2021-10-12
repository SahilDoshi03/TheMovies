package com.example.themovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovies.models.Movies
import com.example.themovies.models.MoviesResponse
import com.example.themovies.services.MoviesApiService
import com.example.themovies.services.UpcomingMoviesApiInterface
import kotlinx.android.synthetic.main.activity_upcoming.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming)

        rv_upcoming.layoutManager = LinearLayoutManager(this)
        rv_upcoming.setHasFixedSize(true)
        getMovieData { movies: List<Movies> ->
            rv_upcoming.adapter = MoviesAdapter(movies)
        }
    }


    private  fun getMovieData(callback: (List<Movies>)->Unit){
        val apiService = MoviesApiService.getInstance().create(UpcomingMoviesApiInterface::class.java)
        apiService.getUMovieList().enqueue(object : Callback<MoviesResponse> {

            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                return callback(response.body()!!.movies)
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {

            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.searchbar_menu, menu)

        return true
    }

}