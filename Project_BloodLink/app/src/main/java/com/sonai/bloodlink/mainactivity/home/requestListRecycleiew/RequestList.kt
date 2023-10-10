package com.sonai.bloodlink.mainactivity.home.requestListRecycleiew

import android.annotation.SuppressLint
import android.os.Bundle
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
import java.time.LocalDate

class RequestList : Fragment() {

    lateinit var recyclerlistrequestlist: RecyclerView
    lateinit var nodata: TextView
    lateinit var progressbar: ProgressBar

    private val userArrayList: ArrayList<RequestListUserData> = ArrayList()

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_request_list, container, false)

        val toolbar =
            requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Request List"

        recyclerlistrequestlist = view.findViewById(R.id.recyclerlistrequestlist)
        progressbar = view.findViewById(R.id.progressbar)
        nodata = view.findViewById(R.id.nodata)

        recyclerlistrequestlist.layoutManager = LinearLayoutManager(requireContext())
        recyclerlistrequestlist.setHasFixedSize(true)
        recyclerlistrequestlist.adapter = RequestListMyAdapter(userArrayList, requireContext())

        val sharedName = PreferenceManager.getDefaultSharedPreferences(context)
            .getString("username", "null").toString()

        if (sharedName == "null"){
            nodata.visibility = View.VISIBLE
            progressbar.visibility = View.GONE
            nodata.text = "Login to see request"
        } else {
            val dbRef =
                FirebaseFirestore.getInstance().collection("Request_for_Blood").document(sharedName)
                    .collection(LocalDate.now().toString())
            dbRef.get()
                .addOnSuccessListener { documents ->
                    var count = 1
                    userArrayList.clear()
                    for (document in documents) {
                        val user = document.toObject(RequestListUserData::class.java)
                        userArrayList.add(user)
                        count = 2
                        progressbar.visibility = View.GONE
                    }
                    recyclerlistrequestlist.adapter?.notifyDataSetChanged()
                    if (count == 1) {
                        progressbar.visibility = View.GONE
                        nodata.text = "No Request"
                        nodata.visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener {

                }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val toolbar =
            requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = "Request List"
    }

}