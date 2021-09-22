package com.example.themovies.services

import com.example.themovies.models.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET

interface PopularMoviesApiInterface{
    @GET("/3/movie/popular?api_key=6b1aa4a6d22cc141bd5302510eb4380d")
    fun getLMovieList(): Call<MoviesResponse>
}