package com.dwarshb.commute.presentation.mainScreen.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dwarshb.commute.databinding.FragmentDashboardBinding

/**
 * DashboardFragment is a view which is planned to be used as User Account. It will display
 * User Account Information and other options related to rides. Currently it shows dummy data.
 * But it will be implemented in Future.
 *
 * @author Darshan Bhanushali
 *
 * Created at : December 27, 2021
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}