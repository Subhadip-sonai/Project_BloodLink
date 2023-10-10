package com.sonai.bloodlink.mainactivity.home.bloodCampRecyclerView

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.sonai.bloodlink.utilityClasses.LocationGets

class BloodCampList : Fragment() {
    private lateinit var listofcamps: RecyclerView
    private lateinit var nothingpresent: TextView
    private lateinit var progressbar: ProgressBar

    private val campList: ArrayList<BloodCampListDataClass> = ArrayList()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camp_list, container, false)

        listofcamps = view.findViewById(R.id.listofcamps)
        nothingpresent = view.findViewById(R.id.nothingpresent)
        progressbar = view.findViewById(R.id.progressbar)

        listofcamps.layoutManager = LinearLayoutManager(requireContext())
        listofcamps.setHasFixedSize(true)
        listofcamps.adapter =
            BloodCampListAdapterClass(campList)

        progressbar.visibility = View.VISIBLE
        getData()
        progressbar.visibility = View.GONE

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData(){
        //getting user location
        var latitude = 0.0
        var longitude = 0.0
        val l = LocationGets(requireContext())
        l.locationRequest()
        l.getCurrentLocation { address ->
            if (address != null) {
                latitude = address.latitude
                longitude = address.longitude
            }
            if (latitude == 0.0 || longitude == 0.0) {
                nothingpresent.text = "Location Error"
                nothingpresent.visibility = View.VISIBLE
            } else {
                val collectionRef = FirebaseFirestore.getInstance().collection("Camp_List")
                collectionRef.get().addOnSuccessListener { documents ->
                    var count = 1
                    campList.clear()
                    for (document in documents) {
                        val user = document.toObject(BloodCampListDataClass::class.java)
                        if (user.lat!! >= latitude.minus(0.06) && user.lat!! <= latitude.plus(0.06)
                            && user.long!! >= longitude.minus(0.06) && user.long!! <= longitude.plus(0.06))
                        {
                            campList.add(user)
                            count = 2
                        }
                    }
                    listofcamps.adapter?.notifyDataSetChanged()
                    if (count == 1) {
                        nothingpresent.visibility = View.VISIBLE
                        nothingpresent.text = "No Camp at this time"
                    }
                }
            }
        }
    }
}