package com.example.themovies.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrailerResponse (

    @SerializedName("id")
    val id_trailer: Int,

    @SerializedName("results")
    val trailers: List<Trailer>

    ):Parcelable