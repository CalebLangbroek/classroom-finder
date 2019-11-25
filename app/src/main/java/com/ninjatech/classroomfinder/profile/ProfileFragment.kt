package com.ninjatech.classroomfinder.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ninjatech.classroomfinder.R
import com.ninjatech.classroomfinder.database.AppDatabase
import com.ninjatech.classroomfinder.databinding.FragmentProfileBinding
import com.ninjatech.classroomfinder.util.ProfileListener

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
        binding = DataBindingUtil.inflate(
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
        val database = AppDatabase.getDatabase(app).savedSectionsDao

        // Create the ViewModel through the ViewModel factory
        val viewModelFactory = ProfileViewModelFactory(database, app)
        val profileViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)

        // Bind to it
        this.binding.profileViewModel = profileViewModel

        val profileListener = ProfileListener({ crn ->
            profileViewModel.onDeleteButtonClicked(crn)
        }, { crn ->
            profileViewModel.onNavigateButtonClicked(crn)
        }
        )

        val adapter = SavedCourseAdapter(profileListener)

        binding.savedCourseList.adapter = adapter

        profileViewModel.savedCourses?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // Observe navigateToMap, when it's not null navigate with the id as a parameter
        profileViewModel.navigateToMap.observe(viewLifecycleOwner, Observer { coordinate ->
            coordinate?.let {
                // Navigate to the map passing the coordinate id
                this.findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToMapFragment(coordinate.id)
                )

                // Notify the ViewModel that we are done navigating
                profileViewModel.navigationFinished()
            }
        })

        this.binding.lifecycleOwner = this
    }
}
