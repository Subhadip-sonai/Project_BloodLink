package com.sonai.bloodlink.admin.home.donroList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sonai.bloodlink.R

class DonorListAdapterClass(private val donorList: ArrayList<DonorListDataClass>) :
    RecyclerView.Adapter<DonorListAdapterClass.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val donorname: TextView = itemView.findViewById(R.id.donorname)
        val donorphone: TextView = itemView.findViewById(R.id.donorphone)
        val donoraddress: TextView = itemView.findViewById(R.id.donoraddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_donorlist_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return donorList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = donorList[position]
        holder.donorname.text = currentItem.name
        holder.donorphone.text = currentItem.phone
        holder.donoraddress.text = "${currentItem.house},${currentItem.district},${currentItem.state}"
    }


}