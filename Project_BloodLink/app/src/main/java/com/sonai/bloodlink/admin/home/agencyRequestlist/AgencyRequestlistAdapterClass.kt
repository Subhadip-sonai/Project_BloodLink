package com.sonai.bloodlink.admin.home.agencyRequestlist

import android.annotation.SuppressLint
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

class AgencyRequestlistAdapterClass(
    private val list: ArrayList<AgencyRequestlistDataClass>,
    private val context: Context
) : RecyclerView.Adapter<AgencyRequestlistAdapterClass.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val agencyname: TextView = itemView.findViewById(R.id.agencyname)
        val agencyphone: TextView = itemView.findViewById(R.id.agencyphone)
        val agencyaddress: TextView = itemView.findViewById(R.id.agencyaddress)
        val acceptbtn: Button = itemView.findViewById(R.id.acceptbtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_agency_requestlist_item, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]

        holder.agencyname.text = currentItem.name
        holder.agencyphone.text = currentItem.phone
        holder.agencyaddress.text =
            "${currentItem.house},${currentItem.district},${currentItem.state},${currentItem.pincode}"

        holder.acceptbtn.setOnClickListener {
            val dataToUpdate = mapOf("verify" to "verified")
            FirebaseFirestore.getInstance().collection("Agency_List")
                .document("${currentItem.name}-${currentItem.pan_no}").update(dataToUpdate)
            holder.acceptbtn.text = "Accepted"
            holder.acceptbtn.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
            Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}