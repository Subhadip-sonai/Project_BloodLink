package com.sonai.bloodlink.mainactivity.home.requestListRecycleiew

import android.annotation.SuppressLint
import android.content.Context
import com.sonai.bloodlink.R
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.utilityClasses.LocationGets
import java.time.LocalDate
import kotlin.math.*

class RequestListMyAdapter(
    private var data: ArrayList<RequestListUserData>,
    private val context: Context
) :
    RecyclerView.Adapter<RequestListMyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_list_recycleview_item_list, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = data[position]

        holder.progressbar.visibility = View.VISIBLE

        val lat = currentItem.latitude
        val long = currentItem.longitude

        val l = LocationGets(context)
        l.locationRequest()
        l.getCurrentLocation { address ->
            val distance = calculateDistance(address!!.latitude, address.longitude, lat!!, long!!)
            holder.distance.text = String.format("%.3f", distance)
        }

        holder.progressbar.visibility = View.GONE
        holder.address.text = currentItem.address
        holder.phonetxt.text = currentItem.phone

        holder.rejectbtn.setOnClickListener {
            val nodeId = currentItem.id
            deleteNode(nodeId!!, currentItem.username!!)
            data.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, data.size)
        }

        holder.acceptbtn.setOnClickListener {
            val call = Intent(Intent.ACTION_DIAL)
            call.data = Uri.parse("tel:${currentItem.phone}")
            context.startActivity(call)
        }

    }

    @SuppressLint("NewApi")
    private fun deleteNode(nodeId: String, username: String) {
        val dbRef = FirebaseFirestore.getInstance().collection("Request_for_Blood").document(username)
            .collection(LocalDate.now().toString()).document(nodeId)
        dbRef.delete()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val address: TextView = itemView.findViewById(R.id.addresstxt)
        val distance: TextView = itemView.findViewById(R.id.distancetxt)
        val progressbar: ProgressBar = itemView.findViewById(R.id.progressbar)
        val phonetxt: TextView = itemView.findViewById(R.id.phonetxt)

        val rejectbtn: Button = itemView.findViewById(R.id.rejectbtn)
        val acceptbtn: Button = itemView.findViewById(R.id.acceptbtn)

    }



    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0 // Earth's radius in kilometers

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)

        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c
    }

}
