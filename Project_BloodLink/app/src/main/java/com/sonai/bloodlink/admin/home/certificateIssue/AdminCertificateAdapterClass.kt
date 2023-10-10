package com.sonai.bloodlink.admin.home.certificateIssue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R

class AdminCertificateAdapterClass(
    private val list: ArrayList<CertificateRequestDataClass>,
    private val context: Context
) : RecyclerView.Adapter<AdminCertificateAdapterClass.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val agencyName: TextView = itemView.findViewById(R.id.nameofagency)
        val eventname: TextView = itemView.findViewById(R.id.eventname)
        val date: TextView = itemView.findViewById(R.id.dateofevent)
        val place: TextView = itemView.findViewById(R.id.placeofevent)
        val issuebtn: Button = itemView.findViewById(R.id.issuebtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_certificate_issue_recyclerview_item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]

        holder.agencyName.text = currentItem.name
        holder.eventname.text = currentItem.event
        holder.date.text = currentItem.date
        holder.place.text = currentItem.place

        val username = currentItem.username
        val campid = currentItem.campid

        holder.issuebtn.setOnClickListener {
            val dataToUpdate = mapOf("verify" to "verified")
            FirebaseFirestore.getInstance().collection("Request_Certificate")
                .document("$username-$campid").update(dataToUpdate)
            holder.issuebtn.text = "Issued"
            holder.issuebtn.setBackgroundColor(ContextCompat.getColor(context,R.color.black))
            Toast.makeText(context, "Issued", Toast.LENGTH_SHORT).show()
        }
    }
}

