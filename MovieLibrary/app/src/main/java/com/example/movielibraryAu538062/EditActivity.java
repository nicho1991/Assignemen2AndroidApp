package com.example.movielibraryAu538062;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {
    public  MovieModel Movie;
    private SeekBar SeekbarUserRating;
    private TextView TextViewUserRating;
    private CheckBox CheckBoxWatched;
    private EditText EditTextUserComment;

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
        setContentView(R.layout.activity_edit);
        Intent intent = new Intent(this, MovieService.class);
        bindService(intent,connection, this.BIND_AUTO_CREATE);
    }

    public void Btn_Cancel(View v) {
        startActivity(new Intent(EditActivity.this, OverviewActivity.class));
    }

    public void Btn_OK (View v) {
        Intent GoesTo = new Intent(EditActivity.this, OverviewActivity.class);
        addAll();

        // change the movie in the database
        this.MovieServiceBind.UpdateMoie(Movie);

        startActivity(GoesTo);
    }

    private void LoadView() {
        TextView ViewTitle = findViewById(R.id.textView_Edit_Title);
        TextViewUserRating =  findViewById(R.id.textView__Edit_UserRating);
        SeekbarUserRating = findViewById(R.id.seekBar_Edit_UserRating);
        CheckBoxWatched = findViewById(R.id.checkBox_Edit_Watched);
        EditTextUserComment = findViewById(R.id.editText_edit_UserComment);

        ViewTitle.setText(Movie.Title);
        if (Movie.UserRating != null) {
            double value =  (Movie.UserRating);

            if (SeekbarUserRating.getProgress() == 0) {
                SeekbarUserRating.setProgress((int) value * 10);
                TextViewUserRating.setText(Double.toString(value));
            } else {
                TextViewUserRating.setText(Double.toString( SeekbarUserRating.getProgress() / 10.0));

            }

        }
        if (!CheckBoxWatched.isChecked()) {
            CheckBoxWatched.setChecked(Movie.Watched);
        }
        if (EditTextUserComment.getText().length() == 0) {
            EditTextUserComment.setText(Movie.Comment);
        }



        SeekbarUserRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double value = ((float)progress / 10.0);
                TextViewUserRating.setText( Double.toString(value) );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void addAll() {
        double value = ((float)SeekbarUserRating.getProgress() / 10.0);

        Movie.UserRating = value;
        Movie.Watched = CheckBoxWatched.isChecked();
        Movie.Comment = EditTextUserComment.getText().toString();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MovieService.LocalBinder binder = (MovieService.LocalBinder) service;
            MovieServiceBind = binder.getService();
            MovieServiceBound = true;

            if (Movie == null) {
                Movie = (MovieModel) getIntent().getExtras().getSerializable("Movie");
                MovieServiceBind.CurrentMovie = Movie;
            }

            LoadView();


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            MovieServiceBound = false;
        }
    };
}
