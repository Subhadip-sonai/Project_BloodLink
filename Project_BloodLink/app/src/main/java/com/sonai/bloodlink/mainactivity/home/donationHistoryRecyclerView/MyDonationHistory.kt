package com.sonai.bloodlink.mainactivity.home.donationHistoryRecyclerView

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R

class MyDonationHistory : Fragment() {
    private lateinit var individuals: TextView
    private lateinit var camps: TextView
    private lateinit var campsListR: RecyclerView
    private lateinit var individualsListR: RecyclerView
    private lateinit var nothingpresent: TextView
    private lateinit var progressbar: ProgressBar

    private val campList: ArrayList<MyDonationHistoryCampsDataClass> = ArrayList()
    private val individualList: ArrayList<MyDonationHistoryIndividualsDataClass> = ArrayList()

    private lateinit var sp: String

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation_history, container, false)

        individuals = view.findViewById(R.id.individuals)
        camps = view.findViewById(R.id.camps)
        campsListR = view.findViewById(R.id.campsList)
        individualsListR = view.findViewById(R.id.individualsList)
        nothingpresent = view.findViewById(R.id.nothingpresent)
        progressbar = view.findViewById(R.id.progressbar)

        sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString("username", null)
            .toString()

        progressbar.visibility = View.VISIBLE
        Handler().postDelayed(
            {
                individuals.setBackgroundColor(Color.GREEN)
                individualsShow()
                progressbar.visibility = View.GONE
            }, 2000
        )

        individuals.setOnClickListener {
            nothingpresent.visibility = View.GONE
            camps.setBackgroundColor(Color.TRANSPARENT)
            individuals.setBackgroundColor(Color.GREEN)
            campsListR.visibility = View.GONE
            individualsShow()
            individualsListR.visibility = View.VISIBLE
        }

        camps.setOnClickListener {
            nothingpresent.visibility = View.GONE
            individuals.setBackgroundColor(Color.TRANSPARENT)
            camps.setBackgroundColor(Color.GREEN)
            individualsListR.visibility = View.GONE
            campsShow()
            campsListR.visibility = View.VISIBLE
        }

        return view
    }

    //Get individuals
    private fun individualsShow() {
        individualsListR.layoutManager = LinearLayoutManager(requireContext())
        individualsListR.setHasFixedSize(true)
        individualsListR.adapter =
            MyDonationHistoryIndividualsAdapterClass(individualList)

        progressbar.visibility = View.VISIBLE
        Handler().postDelayed(
            {
                getIndividualsData()
                progressbar.visibility = View.GONE
            }, 2000
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getIndividualsData() {
        val collectionRef = FirebaseFirestore.getInstance().collection("User_Donation_Individuals")
            .document(sp).collection("User")
        collectionRef.get().addOnSuccessListener { documents ->
            var count = 1
            individualList.clear()
            for (document in documents) {
                val user = document.toObject(MyDonationHistoryIndividualsDataClass::class.java)
                individualList.add(user)
                count = 2
            }
            individualsListR.adapter?.notifyDataSetChanged()
            if (count == 1) {
                nothingpresent.visibility = View.VISIBLE
            }
        }
    }

    //get Camps
    private fun campsShow() {
        campsListR.layoutManager = LinearLayoutManager(requireContext())
        campsListR.setHasFixedSize(true)
        campsListR.adapter =
            MyDonationHistoryCampsAdapterClass(campList)

        progressbar.visibility = View.VISIBLE
        Handler().postDelayed(
            {
                getCampsData()
                progressbar.visibility = View.GONE
            }, 2000
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getCampsData() {
        val collectionRef = FirebaseFirestore.getInstance().collection("User_Donation_Camps")
            .document(sp).collection("User")
        collectionRef.get().addOnSuccessListener { documents ->
            var count = 1
            campList.clear()
            for (document in documents) {
                val user = document.toObject(MyDonationHistoryCampsDataClass::class.java)
                campList.add(user)
                count = 2
            }
            campsListR.adapter?.notifyDataSetChanged()
            if (count == 1) {
                nothingpresent.visibility = View.VISIBLE
            }
        }
    }
}