package com.ninjatech.classroomfinder.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ninjatech.classroomfinder.R
import com.ninjatech.classroomfinder.TextItemViewHolder
import com.ninjatech.classroomfinder.database.Course
import com.ninjatech.classroomfinder.database.SavedCourse

/**
 * Adapter Class for formatting SavedCourses for display.
 */
class SavedCourseAdapter: RecyclerView.Adapter<TextItemViewHolder>() {
    // Get list of SavedCourses
    var data = listOf<SavedCourse>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
//    var data = listOf<Course>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    override fun getItemCount() = data.size

    /**
     * Display a specific SavedCourse.
     */
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.id.toString() + " || " + item.remoteId.toString()
//        holder.textView.text = item.id.toString() + " || " + item.title.toString() + " || " + item.subject.toString()
    }

    /**
     * Create a View for a SavedCourse.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        // Inflate the parent
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.saved_course_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }
}