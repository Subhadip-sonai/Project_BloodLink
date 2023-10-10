package com.sonai.bloodlink.mainactivity.home.findDonorRecyclerview

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.sonai.bloodlink.R
import com.sonai.bloodlink.utilityClasses.LocationGets
import com.sonai.bloodlink.utilityClasses.OTPVerification
import java.time.LocalDate


class FindDonorMyAdapter(
    private val userlist: ArrayList<FindDonorDataData>,
    private val context: Context
) : RecyclerView.Adapter<FindDonorMyAdapter.MyViewHolder>() {

    var username: String = ""
    private var num: String = ""

    private val l = LocationGets(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.find_donor_recycleview_item_list, parent, false)
        return MyViewHolder(itemView) // Pass the itemView (CardView) to the MyViewHolder constructor
    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    @SuppressLint("SuspiciousIndentation", "CutPasteId", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var sp = "null"
        try {
            sp =
                PreferenceManager.getDefaultSharedPreferences(context).getString("username", "null")
                    .toString()
        } catch (_: Exception) {
        }

        val curretitem = userlist[position]
        holder.fullname.text = curretitem.name
        if (sp != "null") {
            holder.phoneno.text = curretitem.phone
            holder.address.text = "${curretitem.house},${curretitem.city},${curretitem.district}"
        }

        l.locationRequest()

        holder.requestbtn.setOnClickListener {
            l.locationRequest()

            val getPhone =
                PreferenceManager.getDefaultSharedPreferences(context).getString("phone", null)

            if (getPhone != null) {
                num = getPhone
                storeLocationInDatabase(curretitem.username!!)
                holder.requestbtn.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )
                holder.requestbtn.text = "Requested"
            } else {
                val box = Dialog(context)
                box.setContentView(R.layout.requesr_number_dialogbox)
                box.show()
                val get = OTPVerification(context)

                val sendButton: Button = box.findViewById(R.id.sendButton)
                val cancelButton: Button = box.findViewById(R.id.cancelButton)
                val getnum: EditText = box.findViewById(R.id.numberEditText)
                val verify: Button = box.findViewById(R.id.verify)
                val progressbar: ProgressBar = box.findViewById(R.id.progressbar)

                // Set click listeners for the buttons in the dialog
                sendButton.setOnClickListener {
                    sendButton.visibility = View.GONE
                    cancelButton.visibility = View.GONE
                    progressbar.visibility = View.VISIBLE
                    PreferenceManager.getDefaultSharedPreferences(context).edit().remove("count")
                        .apply()
                    getnum.error = null
                    val x = getnum.text.toString()
                    if (x.isNotEmpty() && x.length == 10) {
                        get.sendOTP("+91$x")
                        val count = PreferenceManager.getDefaultSharedPreferences(context)
                            .getString("count", null).toString()

                        if (count == "1") {
                            getnum.error = "Enter valid number"
                            sendButton.visibility = View.VISIBLE
                            cancelButton.visibility = View.VISIBLE
                            progressbar.visibility = View.GONE
                        } else {
                            num = getnum.text.toString()
                            Handler().postDelayed(
                                {
                                    getnum.text.clear()
                                    getnum.hint = "Enter OTP"
                                    progressbar.visibility = View.GONE
                                    sendButton.visibility = View.GONE
                                    verify.visibility = View.VISIBLE
                                    cancelButton.visibility = View.VISIBLE
                                    PreferenceManager.getDefaultSharedPreferences(context).edit().remove("count").apply()
                                }, 2500
                            )
                        }
                    }
                }

                verify.setOnClickListener {

                    verify.visibility = View.GONE
                    cancelButton.visibility = View.GONE
                    progressbar.visibility = View.VISIBLE

                    getnum.error = null
                    val y = getnum.text.toString()
                    Log.d("", y)
                    if (y.isNotEmpty() && y.length == 6) {
                        val verificationId = PreferenceManager.getDefaultSharedPreferences(context)
                            .getString("verificationId", null).toString()
//                        Log.d("verificationId: ", verificationId)
                        val credential = PhoneAuthProvider.getCredential(verificationId, y)

                        get.signInWithPhoneCredential(credential)

                        val count = PreferenceManager.getDefaultSharedPreferences(context)
                            .getString("count", null).toString()

                        if (count == "2") {
                            getnum.error = "Invalid OTP"
                            verify.visibility = View.VISIBLE
                            cancelButton.visibility = View.VISIBLE
                            progressbar.visibility = View.GONE
                        } else {
                            verify.visibility = View.GONE
                            cancelButton.visibility = View.GONE
                            progressbar.visibility = View.VISIBLE
                            username = curretitem.username!!

                            storeLocationInDatabase(username)
                            FirebaseFirestore.getInstance().collection("User_Information_PhoneNo")
                                .document(num).get().addOnSuccessListener { task ->
                                    if (task.exists()) {
                                        PreferenceManager.getDefaultSharedPreferences(context)
                                            .edit()
                                            .putString(
                                                "username",
                                                task.data?.get("username").toString()
                                            )
                                            .apply()
                                    } else {
                                        saveToLoginInfo(num)
                                        PreferenceManager.getDefaultSharedPreferences(context)
                                            .edit()
                                            .putString("username", num).apply()
                                    }
                                }

                            PreferenceManager.getDefaultSharedPreferences(context).edit()
                                .putString("phone", num).apply()

                            Handler().postDelayed(
                                {
                                    box.dismiss()
                                    holder.requestbtn.setBackgroundColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.black
                                        )
                                    )
                                    holder.requestbtn.text = "Requested"
                                }, 3000
                            )
                        }
                    } else {
                        getnum.error = "Invalid OTP"
                        verify.visibility = View.VISIBLE
                        cancelButton.visibility = View.VISIBLE
                        progressbar.visibility = View.GONE
                    }
                }
                cancelButton.setOnClickListener {
                    box.dismiss()
                }
            }
        }
    }

    private fun saveToLoginInfo(number: String) {
        val data_username = mapOf(
            "name" to "Your Name",
            "username" to number,
            "phone" to number,
            "password" to "default",
            "email" to "default",
            "govtId" to "default",
            "govtName" to "default",
            "house" to "default",
            "district" to "default",
            "state" to "default",
            "city" to "default",
            "pinCode" to "default",
            "gender" to "default",
            "bloodGroup" to "default",
            "lat" to 0.0,
            "long" to 0.0
        )
        val data_phone = mapOf(
            "username" to number
        )
        FirebaseFirestore.getInstance().collection("User_Information").document(number)
            .set(data_username)
        FirebaseFirestore.getInstance().collection("User_Information_PhoneNo").document(number)
            .set(data_phone)
    }

    @SuppressLint("NewApi")
    private fun storeLocationInDatabase(username: String) {

        val currentDate = LocalDate.now().toString()
        var fullAddress: String
        var lat: Double
        var long: Double

        l.getCurrentLocation { address ->
            if (address != null) {
                fullAddress =
                    "${address.thoroughfare}, ${address.locality}, ${address.adminArea}, ${address.postalCode}"
                lat = address.latitude
                long = address.longitude

                val deRef =
                    FirebaseFirestore.getInstance().collection("Request_for_Blood")
                        .document(username)
                        .collection(currentDate)
                        .document()

                val data = mapOf(
                    "address" to fullAddress,
                    "latitude" to lat,
                    "longitude" to long,
                    "id" to deRef.id,
                    "username" to username,
                    "phone" to num
                )

                deRef.set(data)
            }
        }


    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullname: TextView = itemView.findViewById(R.id.fullnamerecycler)
        val phoneno: TextView = itemView.findViewById(R.id.phonerecycler)
        val address: TextView = itemView.findViewById(R.id.addressrecycler)
        val requestbtn: Button = itemView.findViewById(R.id.requestbtn)
    }

}


