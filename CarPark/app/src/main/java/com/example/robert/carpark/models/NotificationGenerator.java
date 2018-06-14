package com.example.robert.carpark.models;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.robert.carpark.LeaderboardActivity;
import com.example.robert.carpark.R;

/**
 * Created by Robert on 14.06.2018.
 */

public class NotificationGenerator {

    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;

    public static void openActivityNotification(Context context) {
        NotificationCompat.Builder nc = new NotificationCompat.Builder(context);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, LeaderboardActivity.class);

        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nc.setContentIntent(pendingIntent);

        nc.setSmallIcon(R.drawable.ic_car);
        nc.setAutoCancel(true);
        nc.setContentTitle("CarPark");
        nc.setContentText("Have you just parked your personal car? If so, help other users, too!");

        nm.notify(NOTIFICATION_ID_OPEN_ACTIVITY, nc.build());




    }
}
