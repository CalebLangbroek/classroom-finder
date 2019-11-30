package com.ninjatech.classroomfinder.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import com.ninjatech.classroomfinder.database.SavedSection
import com.ninjatech.classroomfinder.database.SavedSectionsDao
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

import android.content.Context.ALARM_SERVICE
import java.lang.Integer.parseInt


class NotificationUtils {

    private var alarmMgr: AlarmManager? = null
    private var alarmIntent: PendingIntent? = null
    internal var savedDao: SavedSectionsDao? = null


    /**
     * Create a notification 20 minutes before a saved class starts
     * Triggers a check for class once an hour.
     * @param context
     */
    fun setReminder(context: Context) {

        alarmMgr = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyReceiver::class.java)
        alarmIntent = PendingIntent.getBroadcast(context, intentId, intent, 0)

        val data = ArrayList<SavedSection>()
        if (intentId > 100000) intentId = 0
        intentId++
        val current = GregorianCalendar()
        val calendar = GregorianCalendar()
        current.timeInMillis = System.currentTimeMillis()
        calendar.timeInMillis = System.currentTimeMillis()
//        Test line left in for immediate firing of notification for testing.
//        alarmMgr!!.set(AlarmManager.RTC_WAKEUP, 0, alarmIntent)
        if (data.size != 0) {
            val cr = savedDao!!.getAlarms()
            if (cr == null) {
                println("No Data Found")
            } else {
                val d = Date()
                val day = calendar.get(Calendar.DAY_OF_WEEK)
                var today: String? = null
                if (day == 2) {
                    today = "Mon"
                } else if (day == 3) {
                    today = "Tue"
                } else if (day == 4) {
                    today = "Wed"
                } else if (day == 5) {
                    today = "Thu"
                } else if (day == 6) {
                    today = "Fri"
                } else if (day == 7) {
                    today = "Sat"
                } else if (day == 1) {
                    today = "Sun"
                }
                val system_hour = d.hours
                val system_minute = d.minutes
                cr.moveToFirst()
                for (i in 0 until cr.count) {
                    val str = cr.getString(4)
                    val time =
                        str.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val hour = parseInt(time[0])
                    val minute = parseInt(time[1])
                    if (cr.getString(2) == today && (system_hour < hour || system_hour == hour && system_minute < minute)) {
                        alarmHour = hour
                        alarmMinute = minute - 20
                        if (alarmMinute < 0) {
                            alarmHour--
                            alarmMinute = 60 + alarmMinute
                            calendar.set(Calendar.HOUR_OF_DAY, alarmHour)
                            calendar.set(Calendar.MINUTE, alarmMinute)
                            alarmHour++
                        } else {
                            calendar.set(Calendar.HOUR_OF_DAY, alarmHour)
                            calendar.set(Calendar.MINUTE, alarmMinute)
                        }
                        if (Build.VERSION.SDK_INT >= 19)
                            alarmMgr!!.setExact(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                alarmIntent
                            )
                        else
                            alarmMgr!!.set(
                                AlarmManager.RTC_WAKEUP,
                                calendar.timeInMillis,
                                alarmIntent
                            )
                    }
                    cr.moveToNext()
                }
            }
            cr.close()

            /**
             * Handler class to rerun method every hour.
             */
            //            final Handler handler = new Handler();
            //            Runnable runnable = new Runnable() {
            //                @Override
            //                public void run() {
            //                    L.e("Called");
            //                    handler.setReminder(this, 3600000);
            //                }
            //            };
            //            handler.setReminder(runnable, 3600000);
        }
    }

    companion object {
        internal var alarmHour = 0
        internal var alarmMinute = 0
        private var intentId = 0
    }


}

