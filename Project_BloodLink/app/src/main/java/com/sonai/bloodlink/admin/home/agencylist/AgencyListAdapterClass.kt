package com.sonai.bloodlink.admin.home.agencylist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sonai.bloodlink.R

class AgencyListAdapterClass(
    private val agencyList: ArrayList<AgencyListDataClass>
) : RecyclerView.Adapter<AgencyListAdapterClass.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val agencyname: TextView = itemView.findViewById(R.id.agencyname)
        val agencyphone: TextView = itemView.findViewById(R.id.agencyphone)
        val agencyaddress: TextView = itemView.findViewById(R.id.agencyaddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_agencylist_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return agencyList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = agencyList[position]

        holder.agencyname.text = currentItem.name
        holder.agencyphone.text = currentItem.phone_no
        holder.agencyaddress.text = "${currentItem.house},${currentItem.district},${currentItem.state},${currentItem.pincode}"
    }

}