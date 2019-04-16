package com.example.movielibraryAu538062;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MovieService extends Service {
    private AsyncMovieClass MovieClass;
    private AppDatabase db;
    private MovieBroadCastReceiver BroadCastReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(BroadCastReceiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MovieLib", "Service starting");
        // start the version for channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        this.MovieClass = new AsyncMovieClass(this); // set the private property
        this.MovieClass.execute(); // start the async work

        // setup the db instance
        this.db = Room.databaseBuilder(this, AppDatabase.class, "MovieDb").allowMainThreadQueries().build();
        this.Movies = this.getAllMovies();


        BroadCastReceiver = new MovieBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("RandomMovie");
        registerReceiver(BroadCastReceiver,intentFilter);
    }

    public MovieService() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){

        // make clickable
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, OverviewActivity.class), 0);

        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "Movie suggestions";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.notification_movie_lib)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.drawable.notification_movie_lib))
                .setContentTitle("Best Movie library ever.")
                .setCategory(Notification.CATEGORY_SERVICE).setContentIntent(contentIntent)
                .build();
        startForeground(2, notification);
    }


    private final IBinder binder = new LocalBinder(); // binder for clients
    public class LocalBinder extends Binder {
        MovieService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MovieService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    MovieModel[] Movies;
    MovieModel CurrentMovie;

    public MovieModel[] getAllMovies() {
        return this.db.MovieModelDao().getAll();
    }

    public void AddMovies(MovieModel[] moviesSended) {
        Intent localIntent = new Intent();
        localIntent.setAction("UpdateOverview" );
        sendBroadcast(localIntent);

        db.MovieModelDao().insertAll(moviesSended);
        this.Movies = this.getAllMovies();
    }

    public void AddMovie(MovieModel Movie) {
        db.MovieModelDao().InsertOne(Movie);
        this.Movies = this.getAllMovies();
    }

    public MovieModel GetMovieByName(String Name) {
        return db.MovieModelDao().findByName(Name);
    }

    public void DeleteMovie(MovieModel Movie) {
        db.MovieModelDao().delete(Movie);
        this.Movies = this.getAllMovies();

        Intent localIntent = new Intent();
        localIntent.setAction("UpdateOverview" );
        sendBroadcast(localIntent);
    }

    public void UpdateMoie(MovieModel Movie) {
        db.MovieModelDao().update(Movie);
        this.Movies = this.getAllMovies();

        Intent localIntent = new Intent();
        localIntent.setAction("UpdateOverview" );
        sendBroadcast(localIntent);
    }

}
