package com.ninjatech.classroomfinder.profile

import com.ninjatech.classroomfinder.util.SectionAndCourseDiffCallBack
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ninjatech.classroomfinder.database.SectionAndCourse
import com.ninjatech.classroomfinder.databinding.SavedCourseItemViewBinding

/**
 * Adapter Class for formatting SavedCourses for display.
 */
class SavedCourseAdapter : ListAdapter<SectionAndCourse, SavedCourseAdapter.ViewHolder>(
    SectionAndCourseDiffCallBack()
) {

    /**
     * Display a specific SavedCourse.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * Create a View for a SavedCourse.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: SavedCourseItemViewBinding) :
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
                val view = SavedCourseItemViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(view)
            }
        }
    }
}
