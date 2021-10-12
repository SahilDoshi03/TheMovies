package com.example.themovies

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themovies.models.Trailer
import kotlinx.android.synthetic.main.trailer_item.view.*

class TrailerAdapter(
    private val trailers: List<Trailer>,
): RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>(){
    inner class TrailerViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position!=RecyclerView.NO_POSITION){
                val key = trailers[position].key
                itemView.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$key")))
            }
        }

        fun bindTrailer(trailer: Trailer){
            itemView.tv_trailerTitle.text = trailer.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        return TrailerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.trailer_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bindTrailer(trailers[position])
    }

    override fun getItemCount() = trailers.size
}