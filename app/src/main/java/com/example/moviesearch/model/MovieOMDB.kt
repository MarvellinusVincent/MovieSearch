package com.example.moviesearch.model

import com.google.gson.annotations.SerializedName

/**
 * A data class representing a movie retrieved from the OMDB API.
 *
 * @property title The title of the movie.
 * @property poster The URL of the movie's poster.
 * @property year The release year of the movie.
 * @property type The type or category of the movie.
 * @property imdbID The IMDb ID of the movie.
 */
data class MovieOMDB(
    @SerializedName("Title") var title: String,
    @SerializedName("Poster") var poster: String,
    @SerializedName("Year") var year: String,
    @SerializedName("imdbID") var imdbID: String

)