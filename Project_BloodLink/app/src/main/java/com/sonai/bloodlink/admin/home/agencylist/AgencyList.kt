package com.sonai.bloodlink.admin.home.agencylist

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

class AgencyList : Fragment() {

    private lateinit var listofrequestedagency: RecyclerView
    private lateinit var nothingpresent: TextView
    private lateinit var progressbar: ProgressBar

    private val agencyList: ArrayList<AgencyListDataClass> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_agency_list, container, false)

        listofrequestedagency = view.findViewById(R.id.listofrequestedagency)
        nothingpresent = view.findViewById(R.id.nothingpresent)
        progressbar = view.findViewById(R.id.progressbar)

        listofrequestedagency.layoutManager = LinearLayoutManager(requireContext())
        listofrequestedagency.setHasFixedSize(true)
        listofrequestedagency.adapter =
            AgencyListAdapterClass(agencyList)

        progressbar.visibility = View.VISIBLE
        getData()
        progressbar.visibility = View.GONE

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData(){
        val collectionRef = FirebaseFirestore.getInstance().collection("Agency_List")
        collectionRef.get().addOnSuccessListener { documents ->
            var count = 1
            agencyList.clear()
            for (document in documents) {
                val user = document.toObject(AgencyListDataClass::class.java)
                if (user.verify == "verified") {
                    agencyList.add(user)
                    count = 2
                }
            }
            listofrequestedagency.adapter?.notifyDataSetChanged()
            if (count == 1) {
                nothingpresent.visibility = View.VISIBLE
            }
        }

    }

}