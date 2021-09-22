package com.example.themovies.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LatestMoviesResponse(
    @SerializedName("results")
    val movies: List<LatestMovies>
):Parcelable{
    constructor():this(mutableListOf())
}