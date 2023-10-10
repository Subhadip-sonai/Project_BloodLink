package com.sonai.bloodlink.admin.home.certificateIssue

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R

class AdminCertificateIssue : Fragment() {

    private lateinit var listofrequester: RecyclerView
    private lateinit var progressbar: ProgressBar
    private lateinit var nothingPresent: TextView

    private val requestList: ArrayList<CertificateRequestDataClass> = ArrayList()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_certificate_issue, container, false)

        listofrequester = view.findViewById(R.id.listofrequestedagency)
        progressbar = view.findViewById(R.id.progressbar)
        nothingPresent = view.findViewById(R.id.nothingPresent)

        listofrequester.layoutManager = LinearLayoutManager(requireContext())
        listofrequester.setHasFixedSize(true)
        listofrequester.adapter = AdminCertificateAdapterClass(requestList, requireContext())

        progressbar.visibility = View.VISIBLE
        getData()
        progressbar.visibility = View.GONE

        return  view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData(){
        val collectionRef = FirebaseFirestore.getInstance().collection("Request_Certificate")
        collectionRef.get().addOnSuccessListener { documents ->
            var count = 1
            requestList.clear()
            for (document in documents){
                val user = document.toObject(CertificateRequestDataClass::class.java)
                if (user.verify != "verified"){
                    requestList.add(user)
                    count = 2
                }
            }
            listofrequester.adapter?.notifyDataSetChanged()
            if (count == 1){
                nothingPresent.visibility = View.VISIBLE
            }
        }
    }
}