package com.ninjatech.classroomfinder.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ninjatech.classroomfinder.R
import com.ninjatech.classroomfinder.database.AppDatabase
import com.ninjatech.classroomfinder.databinding.FragmentProfileBinding

/**
 * Fragment for profile screen.
 */
class ProfileFragment : Fragment() {

    // Binding for fragment
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater,
            R.layout.fragment_profile, container, false
        )

        // Initialize the view model for the profile
        this.initViewModel()

        return binding.root
    }

    /**
     * Initialize view model for profile fragment.
     */
    private fun initViewModel() {
        // Get this application
        val app = requireNotNull(this.activity).application

        // Get the database
        val database = AppDatabase.getDatabase(app).savedDao

        // Create the ViewModel through the ViewModel factory
        val viewModelFactory = ProfileViewModelFactory(database, app)
        val profileViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)

        // Bind to it
        this.binding.profileViewModel = profileViewModel
        this.binding.lifecycleOwner = this
    }
}
