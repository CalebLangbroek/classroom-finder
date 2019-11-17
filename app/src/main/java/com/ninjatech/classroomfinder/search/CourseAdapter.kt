package com.ninjatech.classroomfinder.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ninjatech.classroomfinder.R
import com.ninjatech.classroomfinder.TextItemViewHolder
import com.ninjatech.classroomfinder.database.Course

/**
* Adapter Class for formatting Courses for display.
*/
class CourseAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    // Get list of Courses
    var data = listOf<Course>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    /**
     * Display a specific Course.
     */
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.title + " " + item.subject
    }

    /**
     * Create a View for a Course.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        // Inflate the parent
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.course_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }
}