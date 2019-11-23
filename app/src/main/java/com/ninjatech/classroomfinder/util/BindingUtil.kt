package com.ninjatech.classroomfinder.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ninjatech.classroomfinder.database.TimeAndClassRoom

@BindingAdapter("courseTimesAndRoomsFormatted")
fun TextView.setCourseTimesAndRoomsFormatted(timesAndRooms : List<TimeAndClassRoom>?) {
    timesAndRooms?.let{
        var formattedString = ""
        for(timeAndRoom in timesAndRooms) {
            formattedString += "${timeAndRoom.day} ${timeAndRoom.startTime} to ${timeAndRoom.endTime} in ${timeAndRoom.roomId} \n"
        }
        text = formattedString
    }
}

