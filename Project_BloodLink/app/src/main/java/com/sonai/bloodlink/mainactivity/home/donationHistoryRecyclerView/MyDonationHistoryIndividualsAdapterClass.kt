package com.sonai.bloodlink.mainactivity.home.donationHistoryRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sonai.bloodlink.R

class MyDonationHistoryIndividualsAdapterClass(
    private val individualList: ArrayList<MyDonationHistoryIndividualsDataClass>
) : RecyclerView.Adapter<MyDonationHistoryIndividualsAdapterClass.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val hospitalName: TextView = itemView.findViewById(R.id.hospitalName)
        val dateOfDonation: TextView = itemView.findViewById(R.id.dateOfDonation)
        val addressOfHospital: TextView = itemView.findViewById(R.id.addressOfHospital)
        val downloadbtn: Button = itemView.findViewById(R.id.downloadbtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_donation_history, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return individualList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = individualList[position]

       holder.hospitalName.text = currentItem.hospitalName
       holder.dateOfDonation.text = currentItem.date
       holder.addressOfHospital.text = currentItem.address

        holder.downloadbtn.setOnClickListener {

        }
    }
}