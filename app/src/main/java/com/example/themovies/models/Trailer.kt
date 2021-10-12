package com.example.themovies.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Trailer (
    @SerializedName("key")
    val key: String,

    @SerializedName("name")
    val name: String

    ):Parcelable