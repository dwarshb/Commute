package com.dwarshb.commute.presentation.mainScreen.ui.vehicle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.dwarshb.commute.R
import com.dwarshb.commute.data.Vehicle
import com.dwarshb.commute.databinding.FragmentVehicleBinding
import com.dwarshb.commute.presentation.mainScreen.ui.FilterActionSheet
import com.dwarshb.commute.presentation.mapScreen.MapsActivity
import com.dwarshb.commute.utils.Constants
import com.google.android.material.chip.Chip

/**
 * VehicleFragment is used to display list of vehicles fetched from API. With the help of
 * <code>VehicleListViewModel</code> it manages the list and help to filter it based
 * on its parameter.
 * It also includes VehicleListAdapter which is used to manage each item of vehicleList.
 * On click of each item in VehicleList. It will open <code>MapsActivity</code> which will display
 * the current position of vehicle and other information.
 *
 * Created at : December 25, 2021

 * @see VehicleListViewModel
 * @see VehicleListAdapter
 * @see MapsActivity
 * @see FilterActionSheet
 *
 * @author Darshan Bhanushali
 */
class VehicleListFragment : Fragment() {

    private lateinit var homeViewModel: VehicleListViewModel
    private var _binding: FragmentVehicleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //This property is used to store the response received through intent
    lateinit var bodyResponse : String

    var filterActionSheet : FilterActionSheet = FilterActionSheet()

    /**
     * It is default method which is executed whenever the fragment is loaded
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get the argument from intent and set it to variable
        bodyResponse = arguments?.get(Constants.Intent.BODY).toString()
    }

    /**
     * It is default method which is executed after the View is created
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this)[VehicleListViewModel::class.java]

        _binding = FragmentVehicleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Initialize & set adapter to recyclerView
        val vehiclesRecyclerView: RecyclerView = binding.vehiclesList
        val adapter = VehicleListAdapter(requireContext())
        vehiclesRecyclerView.adapter = adapter

        //Pass the response to viewModel, so it can set to the list
        homeViewModel.setVehicleList(bodyResponse)

        //Observe the vehicle list and update it to adapter.
        homeViewModel.vehicleList.observe(viewLifecycleOwner,{
            adapter.setList(it)
        })


        //Observe the filtered vehicle list and update the list to adapter.

        homeViewModel.filteredVehicleList.observe(viewLifecycleOwner,{
            adapter.clear()
            adapter.setList(it)
        })

        //On click of Map button at the top, open MapsActivity

        binding.mapsFab.setOnClickListener {
            val intent = Intent(context,MapsActivity::class.java)
            val listToPass = ArrayList<Vehicle>(homeViewModel.vehicleList.value)
            intent.putParcelableArrayListExtra(Constants.Intent.BODY,listToPass)
            startActivity(intent)
        }

        /*
         * On click of filter button at the top, display FilterActionSheet which show available
         * filter options in BottomNavigationSheet
         */
        binding.filterFab.setOnClickListener {
            filterActionSheet.show(parentFragmentManager,filterActionSheet.tag)
        }

        /*
         * Handle events performed under FilterActionSheet. Here we filter the list based on
         * the options selected from FilterActionSheet. FleetType & headingDirection are two
         * parameters based on which list is filtered.
         *
         */
        filterActionSheet.setEventListener(object : FilterActionSheet.EventListener {
            override fun filter(fleetType: String?, headingDirection: String?) {
                if (!fleetType.isNullOrEmpty() && !headingDirection.isNullOrEmpty())
                    homeViewModel.filterList(requireContext(),fleetType,headingDirection)
                binding.filterChipGroup.removeAllViewsInLayout()
                fleetType?.let {
                    if (!fleetType.equals("all",ignoreCase = true)) {
                        createChip("Fleet: $fleetType")
                    }
                }

                headingDirection?.let {
                    if (!headingDirection.equals("all",ignoreCase = true)) {
                        createChip("Heading:: $headingDirection")
                    }
                }
                if (binding.filterChipGroup.childCount==0) {
                    binding.filterChipGroup.visibility = View.GONE
                } else {
                    binding.filterChipGroup.visibility = View.VISIBLE
                }
            }

        })
        return root
    }

    /**
     * It is used to create a <code>Chip</code> which will display type of filter
     * applied to the list & add it under <code>ChipGroup</code>
     *
     * @param str is a String object which will be set as text in chip
     *
     * @author Darshan Bhanushali
     */
    fun createChip(str : String) {
        val chip = Chip(context)
        chip.text = str
        chip.setChipBackgroundColorResource(R.color.secondary)
        binding.filterChipGroup.addView(chip)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}