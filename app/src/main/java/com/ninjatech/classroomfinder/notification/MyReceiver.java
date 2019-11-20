package com.ninjatech.classroomfinder.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.ninjatech.classroomfinder.MainActivity;
import com.ninjatech.classroomfinder.R;


public class MyReceiver extends BroadcastReceiver {
    private static int CLASS_REMINDER_NOTIFICATION_ID = 0;
    private static final String CLASS_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    private static int CLASS_REMINDER_PENDING_INTENT_ID = 0;

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_ninja_black);
        return largeIcon;
    }

    private static PendingIntent contentIntent(Context context){
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        if (CLASS_REMINDER_PENDING_INTENT_ID > 10000) CLASS_REMINDER_PENDING_INTENT_ID = 0;
        CLASS_REMINDER_PENDING_INTENT_ID++;
        return PendingIntent.getActivity(context, CLASS_REMINDER_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26){
            NotificationChannel mChannel = new NotificationChannel(CLASS_REMINDER_NOTIFICATION_CHANNEL_ID, context.getString(R.string.notification), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CLASS_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_ninja_black)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Your class starts soon!")
                .setContentText("Your class begins in 20 minutes")
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < 27) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        if (CLASS_REMINDER_NOTIFICATION_ID > 10000) CLASS_REMINDER_NOTIFICATION_ID = 0;
        CLASS_REMINDER_NOTIFICATION_ID++;
        notificationManager.notify(CLASS_REMINDER_NOTIFICATION_ID, notificationBuilder.build());

    }
}
