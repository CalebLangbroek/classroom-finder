package com.ninjatech.classroomfinder.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ninjatech.classroomfinder.database.SectionAndCourse
import com.ninjatech.classroomfinder.databinding.CourseItemViewBinding
import com.ninjatech.classroomfinder.util.SectionAndCourseDiffCallBack

/**
 * Adapter Class for formatting Courses for display.
 */
class CourseAdapter : ListAdapter<SectionAndCourse, CourseAdapter.ViewHolder>(
    SectionAndCourseDiffCallBack()
) {

    /**
     * Display a specific Course.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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
        fun bind(item: SectionAndCourse) {
            binding.sectionAndCourse = item
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
