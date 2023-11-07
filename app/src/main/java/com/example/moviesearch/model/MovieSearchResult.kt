package com.example.moviesearch.model

import com.google.gson.annotations.SerializedName

/**
 * A data class representing a search result for movies retrieved from the OMDB API.
 *
 * @property total The total number of search results.
 * @property movies A list of movies matching the search criteria.
 */
data class MovieSearchResult(
    @SerializedName("totalResults") val total: Int,
    @SerializedName("Search") val movies: List<MovieOMDB>
)

