package com.ninjatech.classroomfinder.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ninjatech.classroomfinder.database.SectionAndCourse
import com.ninjatech.classroomfinder.databinding.SavedCourseItemViewBinding
import com.ninjatech.classroomfinder.util.ProfileListener
import com.ninjatech.classroomfinder.util.SectionAndCourseDiffCallBack

/**
 * Adapter Class for formatting SavedCourses for display.
 */
class SavedCourseAdapter(
    val clickListener: ProfileListener
) :
    ListAdapter<SectionAndCourse, SavedCourseAdapter.ViewHolder>(
        SectionAndCourseDiffCallBack()
    ) {

    /**
     * Display a specific SavedCourse.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
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
        fun bind(
            item: SectionAndCourse,
            clickListener: ProfileListener
        ) {
            binding.sectionAndCourse = item
            binding.clickListener = clickListener
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
