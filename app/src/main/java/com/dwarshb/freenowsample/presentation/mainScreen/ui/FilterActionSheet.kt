package com.dwarshb.freenowsample.presentation.mainScreen.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.dwarshb.freenowsample.databinding.FragmentFilterActionsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.widget.AdapterView

/**
 * FilterActionSheet is subclass of BottomSheetDialogFragment which is used to display filter
 * operations for the vehicle list shown in VehicleListFragment.
 *
 * Created at : December 25, 2021

 * @see com.dwarshb.freenowsample.presentation.mainScreen.ui.vehicle.VehicleListFragment
 * @see BottomSheetDialogFragment
 *
 * @author Darshan Bhanushali
 *
 */
class FilterActionSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterActionsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var fleetType : String? = null

    private var headingDirection : String? = null

    private var event: EventListener? = null

    /**
     * It is default method which is executed after the View is created
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        _binding = FragmentFilterActionsBinding.inflate(inflater, container, false)
        val views : View = binding.root

        views?.viewTreeObserver.addOnGlobalLayoutListener {
            var dialog = dialog as BottomSheetDialog?
            // androidx should use: com.google.android.material.R.id.design_bottom_sheet
            var bottomSheet =
                dialog!!.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            var behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from<FrameLayout?>(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
        }

        fleetType = binding.fleetAll.text.toString()
        binding.fleetGroup.setOnCheckedChangeListener { _, i ->
            run {
                when {
                    i.equals(binding.fleetAll.id) -> {
                        fleetType = binding.fleetAll.text.toString()
                    }
                    i.equals(binding.fleetPooling.id) -> {
                        fleetType = binding.fleetPooling.text.toString()
                    }
                    i.equals(binding.fleetTaxi.id) -> {
                        fleetType = binding.fleetTaxi.text.toString()
                    }
                }
            }
        }

        binding.directionSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                headingDirection = binding.directionSpinner.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                //TODO(")
            }
        }
        return views
    }

    /**
     * On Dismiss of BottomSheetDialogFragment, trigger the filter event to pass the selected parameter
     * back to VehicleListFragment
     *
     * @param dialog
     */
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        event?.filter(fleetType,headingDirection)
    }

    /**
     * It is used to handle the events performed within FilterActionSheet
     * @param eventListener is object of EventListener interface
     *
     * @see EventListener
     *
     * @author Darshan Bhanushali
     */
    fun setEventListener(eventListener: EventListener) {
       event = eventListener
    }

    /**
     * It is interface which is used to communicate between FilterActionSheet & VehicleFragment
     * It uses <code>filter(fleetType : String, headingDirection: String)</code> method to pass data.
     *
     * @author Darshan Bhanushali
     */
    interface EventListener {
        fun filter(fleetType : String?,headingDirection : String?)
    }
}