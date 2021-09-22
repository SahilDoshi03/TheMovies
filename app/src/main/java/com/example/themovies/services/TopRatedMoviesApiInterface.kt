package com.example.themovies.services

import com.example.themovies.models.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET

interface TopRatedMoviesApiInterface{
    @GET("/3/movie/top_rated?api_key=6b1aa4a6d22cc141bd5302510eb4380d")
    fun getTRMovieList(): Call<MoviesResponse>
}