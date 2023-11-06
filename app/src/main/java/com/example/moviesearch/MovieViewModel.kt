package com.example.moviesearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesearch.model.MovieOMDB

/**
 * ViewModel class for managing movie data and share functionality.
 */
class MovieViewModel : ViewModel() {
    /**
     * The currently selected movie.
     */
    var selectedMovie: MovieOMDB? = null

    /**
     * LiveData to notify when the share button is clicked.
     */
    private val _shareClicked = MutableLiveData<Boolean>()
    val shareClicked: LiveData<Boolean>
        get() = _shareClicked

    /**
     * The IMDb ID of the selected movie.
     */
    var id: String? = null

    /**
     * Triggers sharing of the selected movie's IMDb link.
     */
    fun shareMovie() {
        selectedMovie?.let {
            id = it.imdbID
            _shareClicked.value = true
        }
    }

    /**
     * Sets the shareClicked LiveData to false, indicating that sharing is no longer active.
     */
    fun setBack() {
        _shareClicked.value = false
    }
}
