package com.example.themovies.services

import com.example.themovies.models.LatestMoviesResponse
import retrofit2.Call
import retrofit2.http.GET

interface MovieApiInterface{
    @GET("/3/movie/latest?api_key=6b1aa4a6d22cc141bd5302510eb4380d")
    fun getMovieList(): Call<LatestMoviesResponse>
}