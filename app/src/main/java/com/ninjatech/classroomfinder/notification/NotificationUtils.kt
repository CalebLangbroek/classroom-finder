package com.ninjatech.classroomfinder.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.content.Context.ALARM_SERVICE
import com.ninjatech.classroomfinder.database.AppDatabase
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtils {

    private var alarmMgr: AlarmManager? = null
    private var alarmIntent: PendingIntent? = null

    /**
     * Create a notification 20 minutes before a saved class starts
     * Triggers a check for class once an hour.
     * @param context
     */
    fun setReminder(context: Context) {
        
        var t = Thread(Runnable(){

            if (intentId > 100000) intentId = 0
            intentId++
            alarmMgr = context.getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, MyReceiver::class.java)
            alarmIntent = PendingIntent.getBroadcast(context, intentId, intent, 0)
            if (intentId > 100000) intentId = 0
            intentId++
            val current = GregorianCalendar()
            val calendar = Calendar.getInstance()
            current.timeInMillis = System.currentTimeMillis()
            calendar.timeInMillis = System.currentTimeMillis()
            val date = calendar.time
            var day = SimpleDateFormat("EE", Locale.ENGLISH).format(date.time)
            day = "%$day%"
            var minute = calendar.get(Calendar.MINUTE).toString()
            if (parseInt(minute) < 10)
                minute = "0$minute"
            var hour = calendar.get(Calendar.HOUR_OF_DAY).toString()
            if (parseInt(hour) < 10)
                hour = "0$hour"
            val time = "$hour:$minute"
            val database = AppDatabase.getDatabase(context).savedSectionsDao
            val count = database.getAlarmsCount(day, time)
            if (count > 0 ) {
                val cursor = database.getAlarms(day, time)
                val str = cursor.times.startTime

                val times = str.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                var hour = parseInt(times[0])
                var minute = parseInt(times[1]) - 20
                if (minute < 0) {
                    minute += 60
                    hour--
                }
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                if (Build.VERSION.SDK_INT >= 19)
                    alarmMgr!!.setExact(
                        AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
                else
                    alarmMgr!!.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
            }
        }
    )
        t.start()
    }


    companion object {
        private var intentId = 0
    }



}

