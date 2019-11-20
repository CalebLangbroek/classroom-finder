package com.ninjatech.classroomfinder.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import com.ninjatech.classroomfinder.database.SavedSection;
import com.ninjatech.classroomfinder.database.SavedSectionsDao;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.Context.ALARM_SERVICE;
import static java.lang.Integer.parseInt;


public class NotificationUtils {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    static int alarmHour = 0;
    static int alarmMinute = 0;
    SavedSectionsDao savedDao;
    private static int intentId = 0;


    /**
     * Create a notification 20 minutes before a saved class starts
     * Triggers a check for class once an hour.
     *
     * @param context
     */
    public void setReminder(Context context) {

        alarmMgr = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, MyReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, intentId, intent, 0);

        ArrayList<SavedSection> data = new ArrayList<>();
        if (intentId > 100000) intentId = 0;
        intentId++;
        Calendar current = new GregorianCalendar();
        Calendar calendar = new GregorianCalendar();
        current.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeInMillis(System.currentTimeMillis());
        //Test line left in for immediate firing of notification for testing.
        alarmMgr.set(AlarmManager.RTC_WAKEUP, 0, alarmIntent);
        if (data.size() != 0) {
            Cursor cr = savedDao.getAlarms();
            if (cr.equals(null)) {
                System.out.println("No Data Found");
            } else {
                Date d = new Date();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                String today = null;
                if (day == 2) {
                    today = "Mon";
                } else if (day == 3) {
                    today = "Tue";
                } else if (day == 4) {
                    today = "Wed";
                } else if (day == 5) {
                    today = "Thu";
                } else if (day == 6) {
                    today = "Fri";
                } else if (day == 7) {
                    today = "Sat";
                } else if (day == 1) {
                    today = "Sun";
                }
                int system_hour = d.getHours();
                int system_minute = d.getMinutes();
                cr.moveToFirst();
                for (int i = 0; i < cr.getCount(); i++) {
                    String str = cr.getString(4);
                    String[] time = str.split(":");
                    int hour = parseInt(time[0]);
                    int minute = parseInt(time[1]);
                    if (cr.getString(2).equals(today) && (system_hour < hour || (system_hour == hour && system_minute < minute))) {
                        alarmHour = hour;
                        alarmMinute = minute - 20;
                        if (alarmMinute < 0) {
                            alarmHour--;
                            alarmMinute = 60 + alarmMinute;
                            calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
                            calendar.set(Calendar.MINUTE, alarmMinute);
                            alarmHour++;
                        } else {
                            calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
                            calendar.set(Calendar.MINUTE, alarmMinute);
                        }
                        if (Build.VERSION.SDK_INT >= 19)
                            alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                        else
                            alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                    }
                    cr.moveToNext();
                }
            }
            cr.close();

//            /**
//             * Handler class to rerun method every hour.
//             */
//            final Handler handler = new Handler();
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    L.e("Called");
//                    handler.setReminder(this, 3600000);
//                }
//            };
//            handler.setReminder(runnable, 3600000);
//        }
        }


    }
}

