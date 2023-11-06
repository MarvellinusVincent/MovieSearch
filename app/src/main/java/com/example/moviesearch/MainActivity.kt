package com.example.moviesearch

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearch.model.MovieSearchResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://www.omdbapi.com"
private const val API_KEY  = "634e8c25"

/**
 * The main activity of the movie search app.
 */
class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    /** Declare and initialize the ViewModel for movie data. */
    private lateinit var movieViewModel: MovieViewModel

    /**
     * Called when the activity is created.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Initialize the ViewModel for movie data. */
        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        /** References to UI elements */
        val rvMovies = this.findViewById<RecyclerView>(R.id.rvMovies)
        val button = this.findViewById<Button>(R.id.searchButton)
        val inputMovie = this.findViewById<EditText>(R.id.movieSearch)
        val feedbackButton = this.findViewById<ImageButton>(R.id.feedback_button)

        /** Create an adapter for the RecyclerView and set it. */
        val adapter = MovieAdapter(this, movieViewModel)
        rvMovies.adapter = adapter

        /** Set up Retrofit for making API requests. */
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        val movieService = retrofit.create(MovieService::class.java)

        /** Define the onClickListener for the search button. */
        button.setOnClickListener {
            movieService.searchMovie(inputMovie.text.toString(), API_KEY).enqueue(object :
                Callback<MovieSearchResult> {
                override fun onResponse(
                    call: Call<MovieSearchResult>,
                    response: Response<MovieSearchResult>
                ) {
                    Log.i(TAG, "Response code: ${response.code()}")
                    Log.i(TAG, "Response message: ${response.message()}")
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Did not receive a valid response body from the OMDB API... exiting")
                        return
                    }
                    Log.i(TAG, "body $body")
                    adapter.submitList(body.movies)
                }

                override fun onFailure(call: Call<MovieSearchResult>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }
            })
        }

        /** Observe changes in the 'shareClicked' LiveData and handle sharing. */
        movieViewModel.shareClicked.observe(this, Observer { shareClicked ->
            if (shareClicked) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, "https://www.imdb.com/title/${movieViewModel.id}/")
                startActivity(Intent.createChooser(intent, "Share Link: "))
                movieViewModel.setBack()
            }
        })

        /** Handle feedback button clicks by opening an email app. */
        feedbackButton.setOnClickListener {
            val subject = "Feedback"
            val developerEmail = "mvincen@iu.edu"
            val message = ""
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:$developerEmail")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            try {
                startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
