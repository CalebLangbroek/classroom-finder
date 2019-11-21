package com.ninjatech.classroomfinder.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ninjatech.classroomfinder.database.SectionAndCourse
import com.ninjatech.classroomfinder.databinding.CourseItemViewBinding
import com.ninjatech.classroomfinder.util.SectionAndCourseDiffCallBack
import com.ninjatech.classroomfinder.util.SectionAndCourseListener

/**
 * Adapter Class for formatting Courses for display.
 */
class CourseAdapter(val clickListener: SectionAndCourseListener) :
    ListAdapter<SectionAndCourse, CourseAdapter.ViewHolder>(
        SectionAndCourseDiffCallBack()
    ) {

    /**
     * Display a specific Course.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    /**
     * Create a View for a Course.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder constructor(private val binding: CourseItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind to the data binding variable.
         */
        fun bind(clickListener: SectionAndCourseListener, item: SectionAndCourse) {
            binding.sectionAndCourse = item
            binding.clickListener = clickListener

            TODO("not implemented") // Check that checkbox state matches db

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                // Inflate the parent
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = CourseItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(view)
            }
        }
    }

}
