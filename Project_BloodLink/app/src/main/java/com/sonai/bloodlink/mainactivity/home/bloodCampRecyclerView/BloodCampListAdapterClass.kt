package com.sonai.bloodlink.mainactivity.home.bloodCampRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sonai.bloodlink.R

class BloodCampListAdapterClass(
    private val agencyList: ArrayList<BloodCampListDataClass>
) : RecyclerView.Adapter<BloodCampListAdapterClass.MyViewHolder>()  {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameofagency: TextView = itemView.findViewById(R.id.nameofagency)
        val eventname: TextView = itemView.findViewById(R.id.eventname)
        val dateofevent: TextView = itemView.findViewById(R.id.dateofevent)
        val placeofevent: TextView = itemView.findViewById(R.id.placeofevent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_camp_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return agencyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = agencyList[position]

        holder.nameofagency.text = currentItem.agencyName
        holder.eventname.text = currentItem.campName
        holder.placeofevent.text = currentItem.address
        holder.dateofevent.text = currentItem.date
    }

}