package com.example.movielibraryAu538062;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import java.util.Random;

public class AsyncMovieClass extends AsyncTask {
    public static int x;
    Context context;

    private MovieService MovieServiceBind;
    private boolean MovieServiceBound = false;



    AsyncMovieClass(Context context) {

        this.context = context;
        Intent intent = new Intent(context, MovieService.class);
        context.bindService(intent,connection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected Object doInBackground(Object[] objects) {
        while(true) {
            try {
                if(isCancelled())
                    break;
                Thread.sleep(120000); // 2 minutes 120000
                x++;
                publishProgress(x);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    @Override
    protected void onProgressUpdate(Object[] values) { // every 2 minutes passed
        super.onProgressUpdate();
        Log.d("MovieLib", "Service sending movie suggestion");
        // create random number for random movie
        // https://stackoverflow.com/questions/21049747/how-can-i-generate-a-random-number-in-a-certain-range/21049922
        final int min = 0;
        final int max = MovieServiceBind.Movies.length -1;
        final int random = new Random().nextInt((max - min) + 1) + min;
        // update the movie if user clicks

        Intent localIntent = new Intent("RandomMovie" );

        MovieModel ChosenMovie = MovieServiceBind.Movies[random];
                localIntent.putExtra("GetMovie", (Parcelable) ChosenMovie);
        this.context.sendBroadcast(localIntent);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        context.unbindService(connection);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MovieService.LocalBinder binder = (MovieService.LocalBinder) service;
            MovieServiceBind = binder.getService();
            MovieServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            MovieServiceBound = false;
        }
    };
}