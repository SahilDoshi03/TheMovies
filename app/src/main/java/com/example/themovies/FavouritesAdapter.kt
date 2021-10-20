package com.example.themovies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_item.view.*

class FavouritesAdapter (
    private val context: Context,
    private val listOfMap : ArrayList <Map<String,*>>) :
    RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {

    inner class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val imageBaseUrl = "https://image.tmdb.org/t/p/w500/"

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position!=RecyclerView.NO_POSITION){
                Intent(v?.context,InfoActivity::class.java)
            }
        }

        fun insertMovie(position: Int){
            Glide.with(itemView).load(imageBaseUrl + listOfMap[position]["poster"]).into(itemView.movie_poster)
            itemView.tv_movieTitle.text = listOfMap[position]["title"].toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false)

        return FavouritesViewHolder(itemView)
    }

    override fun onBindViewHolder(holderFavourites: FavouritesViewHolder, position: Int) {


        holderFavourites.insertMovie(position)


    }
    override fun getItemCount(): Int {
        return listOfMap.size
    }

}

