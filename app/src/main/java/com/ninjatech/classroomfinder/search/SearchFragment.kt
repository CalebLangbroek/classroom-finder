package com.ninjatech.classroomfinder.search


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ninjatech.classroomfinder.R
import com.ninjatech.classroomfinder.database.AppDatabase
import com.ninjatech.classroomfinder.databinding.FragmentSearchBinding
import com.ninjatech.classroomfinder.util.SectionAndCourseListener

/**
 * Fragment class for the SearchFragment.
 */
class SearchFragment : Fragment() {

    // Binding for fragment
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search, container, false
        )

        // Initialize the view model for the search fragment
        this.initViewModel()

        return binding.root
    }

    /**
     * Called when the fragment is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inform that we should create an options menu
        // onCreateOptionsMenu will be called
        setHasOptionsMenu(true)
    }

    /**
     * Initialize options menu.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Inflate the tool bar menu
        inflater.inflate(R.menu.toolbar_menu, menu)

        // Get the search view
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)

        // Setup to listen for text input in the search box
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(query: String?): Boolean {
                    // Filter the course list in the view model
                    binding.searchViewModel?.filterCourses(query)
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    // Filter the course list in the view model
                    binding.searchViewModel?.filterCourses(query)

                    // Close the keyboard
                    searchView.clearFocus()
                    return true
                }
            }
        )
    }

    /**
     * Initialize view model for profile fragment.
     */
    private fun initViewModel() {
        // Get this application
        val app = requireNotNull(this.activity).application

        // Get the database
        val database = AppDatabase.getDatabase(app).coursesDao

        // Create the ViewModel through the ViewModel factory
        val viewModelFactory = SearchViewModelFactory(database, app)
        val searchViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel::class.java)

        // Bind to it
        this.binding.searchViewModel = searchViewModel

        val adapter = CourseAdapter(SectionAndCourseListener { crn ->
            searchViewModel.onCourseClicked(crn)
        })
        binding.courseList.adapter = adapter

        searchViewModel.courses?.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        this.binding.lifecycleOwner = this
    }

}
