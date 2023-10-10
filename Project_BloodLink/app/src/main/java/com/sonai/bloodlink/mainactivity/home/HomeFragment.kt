package com.sonai.bloodlink.mainactivity.home

import FindDonor
import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.sonai.bloodlink.R
import com.sonai.bloodlink.mainactivity.dashboard.DashboardPage
import com.sonai.bloodlink.mainactivity.home.bloodCampRecyclerView.BloodCampList
import com.sonai.bloodlink.mainactivity.home.donationHistoryRecyclerView.MyDonationHistory
import com.sonai.bloodlink.mainactivity.home.requestListRecycleiew.RequestList


class HomeFragment : Fragment() {

    lateinit var finddonor: CardView
    lateinit var requestlist: CardView
    lateinit var campList: LinearLayout
    lateinit var donationHistory: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment and return it
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        //we use false because

        finddonor = view.findViewById(R.id.finddonor)
        requestlist = view.findViewById(R.id.requestlist)
        campList = view.findViewById(R.id.campList)
        donationHistory = view.findViewById(R.id.donationHistory)
        val toolbar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        finddonor.setOnClickListener {
            (activity as DashboardPage).replaceFragment(FindDonor())
            toolbar.title = "Find Donor"
        }

        requestlist.setOnClickListener {
            (activity as DashboardPage).replaceFragment(RequestList())
            toolbar.title = "Request List"
        }

        campList.setOnClickListener {
            (activity as DashboardPage).replaceFragment(BloodCampList())
            toolbar.title = "Blood Camps"
        }

        donationHistory.setOnClickListener{
            (activity as DashboardPage).replaceFragment(MyDonationHistory())
            toolbar.title = "My Donation"
        }

        return view
    }

    @SuppressLint("CutPasteId")
    override fun onResume() {
        super.onResume()
        val toolbar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val logoutbtn = toolbar.findViewById<Button>(R.id.logoutbtn)
        val settxt = activity?.findViewById<NavigationView>(R.id.navigationview)?.getHeaderView(0)?.findViewById<TextView>(R.id.settxt)
        val loginregcontainer = activity?.findViewById<NavigationView>(R.id.navigationview)?.getHeaderView(0)?.findViewById<LinearLayout>(R.id.loginregcontainer)
        toolbar.title = "Dashboard"

        var sp = ""
        try {
            sp = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString("username", null)
                .toString()

        } catch (_: Exception) {}

        if (sp != "null") {
            settxt?.visibility = View.VISIBLE
            loginregcontainer?.visibility = View.GONE
            settxt?.text = sp
            logoutbtn.visibility = View.VISIBLE
        } else {
            loginregcontainer?.visibility = View.VISIBLE
            settxt?.visibility = View.GONE
            logoutbtn.visibility = View.GONE
        }
    }
}