package com.example.moviesearch

import com.example.moviesearch.model.DetailedMovieResponse
import com.example.moviesearch.model.MovieSearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for defining movie-related API endpoints and requests.
 */
public interface MovieService {
    /**
     * Sends a GET request to search for movies based on a title and API key.
     *
     * @param movieTitle The title of the movie to search for.
     * @param apiKey The API key for accessing the movie database.
     * @return A Call object representing the API request and response.
     */
    @GET("/")
    fun searchMovie(
        @Query("s") movieTitle: String,
        @Query("apikey") apiKey: String
    ): Call<MovieSearchResult>

    @GET("/")
    fun searchMovieIMDB(
        @Query("i") imbdId: String,
        @Query("apikey") apiKey: String
    ): Call<DetailedMovieResponse>
}
