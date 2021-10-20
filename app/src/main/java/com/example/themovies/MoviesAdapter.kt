package com.example.themovies

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.themovies.models.Movies
import kotlinx.android.synthetic.main.movie_item.view.*

class MoviesAdapter (
    private val movies: List<Movies>,
    ): RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(){

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener{
        private val imageBaseUrl = "https://image.tmdb.org/t/p/w500/"

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position!=RecyclerView.NO_POSITION){
                val intent = Intent(v?.context,InfoActivity::class.java)
                intent.putExtra("movieId",movies[position].movieId)
                intent.putExtra("position",position)
                intent.putExtra("ogtitle", movies[position].title)
                intent.putExtra("poster_path",movies[position].poster)
                intent.putExtra("overview",movies[position].overview)
                intent.putExtra("release_date",movies[position].release)
                intent.putExtra("url",imageBaseUrl)
                intent.putExtra("voteAverage",movies[position].vote_average.toString())
                intent.putExtra("obj",movies[position])
                itemView.context.startActivity(intent)
            }
        }

        fun bindMovie(movie: Movies){
            itemView.tv_movieTitle.text = movie.title
            Glide.with(itemView).load(imageBaseUrl+movie.poster).into(itemView.movie_poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movies[position])
    }

    override fun getItemCount(): Int = movies.size

}