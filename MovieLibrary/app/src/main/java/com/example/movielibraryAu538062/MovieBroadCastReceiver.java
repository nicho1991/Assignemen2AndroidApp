package com.example.movielibraryAu538062;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

class MovieBroadCastReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == "RandomMovie") {

            // get the received movie to display
            MovieModel ChosenMovie = intent.getExtras().getParcelable("GetMovie");

            // make clickable
            PendingIntent contentIntent =
                    PendingIntent.getActivity(context, 0, new Intent(context, OverviewActivity.class), 0);

            String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
            Notification notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.drawable.notification_movie_lib)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.notification_movie_lib))
                    .setContentTitle("Movie Time!")
                    .setContentText("Watch this random movie: " + ChosenMovie.Title)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setCategory(Notification.CATEGORY_EVENT).setContentIntent(contentIntent).build();
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(2 , notification);
        }

    }

}