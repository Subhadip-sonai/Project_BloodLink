package com.sonai.bloodlink.admin.home.donroList

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

class DonorList : Fragment() {
    private lateinit var listofdonor: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var nothingpresent: TextView
    private val donorList: ArrayList<DonorListDataClass> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_donor_list, container, false)

        listofdonor = view.findViewById(R.id.listofdonor)
        progressBar = view.findViewById(R.id.progressbar)
        nothingpresent = view.findViewById(R.id.nothingpresent)

        listofdonor.layoutManager = LinearLayoutManager(requireContext())
        listofdonor.setHasFixedSize(true)
        listofdonor.adapter = DonorListAdapterClass(donorList)

        progressBar.visibility = View.VISIBLE
        getData()
        progressBar.visibility = View.GONE

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        val collectionRef = FirebaseFirestore.getInstance().collection("User_Information")
        collectionRef.get().addOnSuccessListener { documents ->
            var count = 1
            donorList.clear()
            for (document in documents) {
                val user = document.toObject(DonorListDataClass::class.java)
                donorList.add(user)
                count = 2
            }
            listofdonor.adapter?.notifyDataSetChanged()
            if (count == 1) {
                nothingpresent.visibility = View.VISIBLE
            }
        }

    }

}