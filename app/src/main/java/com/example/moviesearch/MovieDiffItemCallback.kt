package com.example.moviesearch

import androidx.recyclerview.widget.DiffUtil
import com.example.moviesearch.model.MovieOMDB

/**
 * Callback class for calculating the difference between two lists of MovieOMDB items.
 *
 * This class is used to determine if two items in the list are the same and if their contents are the same.
 */
class MovieDiffItemCallback : DiffUtil.ItemCallback<MovieOMDB>() {
    /**
     * Checks if two items are the same by comparing their titles.
     *
     * @param oldItem The old MovieOMDB item.
     * @param newItem The new MovieOMDB item.
     * @return True if the items have the same title, otherwise false.
     */
    override fun areItemsTheSame(oldItem: MovieOMDB, newItem: MovieOMDB)
            = (oldItem.title == newItem.title)

    /**
     * Checks if two items have the same content by comparing them directly.
     *
     * @param oldItem The old MovieOMDB item.
     * @param newItem The new MovieOMDB item.
     * @return True if the items have the same content, otherwise false.
     */
    override fun areContentsTheSame(oldItem: MovieOMDB, newItem: MovieOMDB)
            = (oldItem == newItem)
}
