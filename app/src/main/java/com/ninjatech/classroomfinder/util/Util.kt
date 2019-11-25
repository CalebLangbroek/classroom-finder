package com.ninjatech.classroomfinder.util

import androidx.recyclerview.widget.DiffUtil
import com.ninjatech.classroomfinder.database.SectionAndCourse

/**
 * Callback for ListAdapter to calculate the difference between items and update them.
 */
class SectionAndCourseDiffCallBack : DiffUtil.ItemCallback<SectionAndCourse>() {

    override fun areItemsTheSame(oldItem: SectionAndCourse, newItem: SectionAndCourse): Boolean {
        return oldItem.section.crn == newItem.section.crn
    }

    override fun areContentsTheSame(oldItem: SectionAndCourse, newItem: SectionAndCourse): Boolean {
        return false
    }
}

/**
 * Click listener class to pass clicks up to view holder.
 *
 * @param{clickListener} Callback function for the click listener.
 */
class SearchListener(val clickListener: (crn: Int) -> Unit) {
    fun onClick(sectionAndCourse: SectionAndCourse) = clickListener(sectionAndCourse.section.crn)
}

class ProfileListener(val onDeleteClickListener: (crn: Int) -> Unit, val onNavigateClickListener: (crn: Int) -> Unit ) {
    fun onNavigateClick(sectionAndCourse: SectionAndCourse) = onNavigateClickListener(sectionAndCourse.section.crn)
    fun onDeleteClick(sectionAndCourse: SectionAndCourse) = onDeleteClickListener(sectionAndCourse.section.crn)
}