package com.example.moviesearch

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.moviesearch.model.MovieOMDB
import com.example.moviesearch.databinding.MovieItemBinding
import com.example.moviesearch.model.DetailedMovieResponse

/**
 * An adapter for the RecyclerView to display movie items.
 *
 * @param context The Android application context.
 * @param viewModel The ViewModel for managing movie data.
 */
class MovieAdapter(
    val context: Context,
    private val viewModel: MovieViewModel
) : ListAdapter<MovieOMDB, MovieAdapter.ItemViewHolder>(MovieDiffItemCallback()) {
    private val detailedMovieList: MutableList<DetailedMovieResponse> = mutableListOf()

    /**
     * Creates a new item view holder by inflating the corresponding layout.
     *
     * @param parent The parent view group.
     * @param viewType The type of view.
     * @return A new instance of ItemViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.inflateFrom(parent)
    }

    fun addDetailedMovieInfo(detailedMovie: DetailedMovieResponse) {
        detailedMovieList.add(detailedMovie)
        notifyDataSetChanged()
    }

    /**
     * Binds movie data to the item view holder.
     *
     * @param holder The view holder to bind data to.
     * @param position The position of the item in the RecyclerView.
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        val detailedMovie = detailedMovieList.getOrNull(position) // Get detailed movie info if available
        holder.bind(item, detailedMovie, context, viewModel)
    }

    /**
     * View holder for displaying individual movie items.
     *
     * @param binding The data binding object for the movie item layout.
     */
    class ItemViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            /**
             * Inflates a new item view holder from the parent view group.
             *
             * @param parent The parent view group.
             * @return A new instance of ItemViewHolder.
             */
            fun inflateFrom(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }

        /**
         * Binds movie data to the item view holder.
         *
         * @param movie The movie data to display.
         * @param context The Android application context.
         * @param viewModel The ViewModel for managing movie data.
         */
        fun bind(movie: MovieOMDB, detailedMovie: DetailedMovieResponse?, context: Context, viewModel: MovieViewModel) {
            binding.movieName.text = movie.title
            binding.movieYear.text = movie.year
            val imdbLink = "https://www.imdb.com/title/${movie.imdbID}"
            binding.imdbLink.text = imdbLink
            binding.imdbLink.paintFlags = binding.imdbLink.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            Glide.with(context).load(movie.poster)
                .apply(
                    RequestOptions().transform(
                        CenterCrop(), RoundedCorners(20)
                    )
                )
                .into(binding.imageView)

            binding.shareButton.setOnClickListener {
                viewModel.selectedMovie = movie
                viewModel.shareMovie()
            }

            binding.imdbLink.setOnClickListener {
                val imdbUri = Uri.parse(imdbLink)
                val intent = Intent(Intent.ACTION_VIEW, imdbUri)
                context.startActivity(intent)
            }
            detailedMovie?.let {
                binding.movieRunTime.text = detailedMovie.runtime
                binding.movieGenre.text = detailedMovie.genre
                binding.movieRatings.text = detailedMovie.ratings.toString()
                binding.imdbRating.text = detailedMovie.imdbRating
            }
        }
    }
}