package com.example.movielibraryAu538062;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.InputStream;

public class OverviewActivity extends AppCompatActivity {

    private MovieModel[] MovieList;
    private MovieService MovieServiceBind;
    private boolean MovieServiceBound = false;

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /// service part
        Intent IntentForIntentSerice = new Intent(this,MovieService.class);
        startForegroundService(IntentForIntentSerice); // start the service

        // bind service part
        try {
            Intent intent = new Intent(this, MovieService.class);
            bindService(intent,connection, Context.BIND_AUTO_CREATE);
        } catch (Exception exceotion){
        }
        setContentView(R.layout.activity_overview);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(BroadCastReceiver);
        unbindService(connection);

    }

    public void Btn_Exit(View v) { // close app
        ActivityCompat.finishAffinity(this);
    }

    private void InitiateListView(MovieModel[] Movies) {
        // Create the adapter to convert the array to views
        MovieAdapter adapter = new MovieAdapter(OverviewActivity.this, Movies);
        // Attach the adapter to a ListView
        ListView list = findViewById(R.id.ListViewOverview);
        list.setAdapter(adapter);
    }

    // providing connection for the bind service
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MovieService.LocalBinder binder = (MovieService.LocalBinder) service;
            MovieServiceBind = binder.getService();
            MovieServiceBound = true;

            MovieList = MovieServiceBind.Movies;
            if (MovieList == null || MovieList.length == 0) {
                MovieList = fetchCSVData();
                MovieServiceBind.AddMovies(MovieList);

            } else {
            }
            InitiateListView(MovieList); // load up the listview
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            MovieServiceBound = false;
        }
    };

    private  MovieModel[] fetchCSVData() { // get movies as list from csv file
        InputStream inputStream = getResources().openRawResource(R.raw.movielist);
        CSVFile csvFile = new CSVFile(inputStream);
        return csvFile.read();
    }

    public void AddMovieFromView(View v) {
        EditText TitleEdit = (EditText)findViewById(R.id.editTextAddMovie);
        String TitleToFind = String.valueOf(TitleEdit.getText());

        AddNewMovie(TitleToFind);
    }

    private void AddNewMovie(String Title) {
        final Context context = this;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.omdbapi.com/?apikey=8e43bc5b&t=" + Title;
// Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Gson g = new Gson();
                        OMDBMovie Movie = g.fromJson(response.toString(),OMDBMovie.class);

                        if (Movie.title != null) {
                            MovieModel AddMovie = new MovieModel(Movie.title,Movie.plot, Movie.genre,Movie.imdbRating);
                            MovieServiceBind.AddMovie(AddMovie);
                            InitiateListView(MovieServiceBind.Movies);
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Volley error" + error);
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
