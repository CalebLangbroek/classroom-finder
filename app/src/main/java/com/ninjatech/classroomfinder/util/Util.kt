package com.ninjatech.classroomfinder.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ninjatech.classroomfinder.database.SectionAndCourse
import com.ninjatech.classroomfinder.databinding.CourseItemViewBinding


class SectionAndCourseDiffCallBack : DiffUtil.ItemCallback<SectionAndCourse>() {

    override fun areItemsTheSame(oldItem: SectionAndCourse, newItem: SectionAndCourse): Boolean {
        return oldItem.section.crn == newItem.section.crn
    }

    override fun areContentsTheSame(oldItem: SectionAndCourse, newItem: SectionAndCourse): Boolean {
        return false
    }
}
