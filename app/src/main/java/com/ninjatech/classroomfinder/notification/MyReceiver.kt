package com.ninjatech.classroomfinder.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ninjatech.classroomfinder.MainActivity
import com.ninjatech.classroomfinder.R
import java.util.*


class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmTime = intent.extras?.getInt("alarmTime")
        val calendar = Calendar.getInstance()
        val minute = calendar.get(Calendar.MINUTE)
        val time = alarmTime?.minus(minute)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26) {
            val mChannel = NotificationChannel(
                CLASS_REMINDER_NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.notification),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(mChannel)
        }

        val notificationBuilder =
            NotificationCompat.Builder(context, CLASS_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_ninja_black)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Your class starts soon!")
                .setContentText("Your class starts in $time minutes!")
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < 27) {
            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }
        if (CLASS_REMINDER_NOTIFICATION_ID > 10000) CLASS_REMINDER_NOTIFICATION_ID = 0
        CLASS_REMINDER_NOTIFICATION_ID++
        notificationManager.notify(CLASS_REMINDER_NOTIFICATION_ID, notificationBuilder.build())

        Handler().postDelayed({
            val rec = NotificationUtils()
            rec.setReminder(context)
        }, 300000)


    }



    companion object {
        private var CLASS_REMINDER_NOTIFICATION_ID = 0
        private val CLASS_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel"
        private var CLASS_REMINDER_PENDING_INTENT_ID = 0

        private fun largeIcon(context: Context): Bitmap {
            val res = context.resources
            return BitmapFactory.decodeResource(res, R.drawable.ic_ninja_black)
        }

        private fun contentIntent(context: Context): PendingIntent {
            val startActivityIntent = Intent(context, MainActivity::class.java)
            if (CLASS_REMINDER_PENDING_INTENT_ID > 10000) CLASS_REMINDER_PENDING_INTENT_ID = 0
            CLASS_REMINDER_PENDING_INTENT_ID++
            return PendingIntent.getActivity(
                context,
                CLASS_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        }
    }
}
