package com.example.themovies.services

import com.example.themovies.models.TrailerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TrailerResponseApiInterface {
    @GET("/3/movie/{movie_id}/videos?api_key=6b1aa4a6d22cc141bd5302510eb4380d")
    fun getTrailerList(@Path("movie_id")id:Int?): Call<TrailerResponse>
}