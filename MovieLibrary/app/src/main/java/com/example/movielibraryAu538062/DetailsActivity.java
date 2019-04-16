package com.example.movielibraryAu538062;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    public MovieModel Movie;
    private MovieService MovieServiceBind;
    private boolean MovieServiceBound = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        // Movie = (MovieModel) getIntent().getExtras().getSerializable("Movie");

        Intent intent = new Intent(this, MovieService.class);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);


    }

    public void Btn_OK(View v) {
        startActivity(new Intent(DetailsActivity.this , OverviewActivity.class));
    }

    public void Btn_Delete(View v) {
        //delete from db
        MovieServiceBind.DeleteMovie(Movie);

        // go home
        Btn_OK(v);
    }

    private void LoadView() {
       ImageView ViewIcon = (ImageView )findViewById(R.id.imageView_Details);
       TextView ViewGenres = findViewById(R.id.textView_Details_Genres);
       TextView ViewIMDB = findViewById(R.id.textView_Details_IMDBRating);
       TextView ViewPlot = findViewById(R.id.textView_Details_Plot);
       TextView ViewTitle = findViewById(R.id.textView_Details_Title);
       TextView ViewUserRating =  findViewById(R.id.textView_Details_UserRating);
       TextView UserComment = findViewById(R.id.TextView_Details_UserComment);
       CheckBox Watched = findViewById(R.id.checkBox_Details_Watched);


        ViewIcon.setImageResource(Movie.Icon);
        ViewGenres.setText(Movie.Genres);

        ViewPlot.setText(Movie.Plot);
        ViewTitle.setText(Movie.Title);
        UserComment.setText(Movie.Comment);

        if (Movie.Comment == "" | Movie.Comment == null) {
            UserComment.setVisibility(View.GONE);
        }

        Watched.setChecked(Movie.Watched);


        ViewIMDB.setText(getResources().getString(R.string.String_IMDB) + " " + Movie.Rating);
        if (Movie.UserRating != null) {
            ViewUserRating.setText(getResources().getString(R.string.String_User) + " " + Double.toString(Movie.UserRating) );
        }

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MovieService.LocalBinder binder = (MovieService.LocalBinder) service;
            MovieServiceBind = binder.getService();
            MovieServiceBound = true;
            Movie = (MovieModel) getIntent().getExtras().getSerializable("Movie");
            MovieServiceBind.CurrentMovie = Movie;
            LoadView();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            MovieServiceBound = false;
        }
    };
}
