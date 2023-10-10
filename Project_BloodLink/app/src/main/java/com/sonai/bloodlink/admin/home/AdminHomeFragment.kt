package com.sonai.bloodlink.admin.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.sonai.bloodlink.R
import com.sonai.bloodlink.admin.AdminDashboard
import com.sonai.bloodlink.admin.home.agencyRequestlist.AgencyRequestlist
import com.sonai.bloodlink.admin.home.agencylist.AgencyList
import com.sonai.bloodlink.admin.home.certificateIssue.AdminCertificateIssue
import com.sonai.bloodlink.admin.home.donroList.DonorList

class AdminHomeFragment : Fragment() {

    lateinit var certificateissue: LinearLayout
    lateinit var verifiedagencylist: LinearLayout
    lateinit var agencyrequestlist: LinearLayout
    lateinit var donroList: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_home_page, container, false)

        certificateissue = view.findViewById(R.id.certificateissue)
        verifiedagencylist = view.findViewById(R.id.verifiedagencylist)
        agencyrequestlist = view.findViewById(R.id.agencyrequestlist)
        donroList = view.findViewById(R.id.donroList)

        certificateissue.setOnClickListener{

            (activity as AdminDashboard).replaceFragment(AdminCertificateIssue())

        }
        agencyrequestlist.setOnClickListener{
            (activity as AdminDashboard).replaceFragment(AgencyRequestlist())
        }
        verifiedagencylist.setOnClickListener {
            (activity as AdminDashboard).replaceFragment(AgencyList())
        }
        donroList.setOnClickListener {
            (activity as AdminDashboard).replaceFragment(DonorList())
        }


        return view
    }

}